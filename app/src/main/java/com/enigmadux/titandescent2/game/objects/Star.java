package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.util.SoundLib;

public class Star extends Powerup {
    private static final float WIDTH = 1f;
    private static final float ROT_SPEED = 300;//degrees per second

    private int starNumber;

    private float rotation;

    private boolean permanentlyDisabled;
    private boolean thisRunDisabled;

    public Star(int instanceID,int starNumber,float x,float y) {
        super(instanceID,x,y,WIDTH,WIDTH);
        this.starNumber = starNumber;
    }


    @Override
    public void update(long dt, World world) {
        super.update(dt, world);
        if (world.getCollectedStars().isCollected(starNumber)){
            this.permanentlyDisabled = true;
        }

        this.rotation += dt * ROT_SPEED/1000;
    }

    @Override
    void onCollect(World world, Ship s) {
        if (! thisRunDisabled && ! permanentlyDisabled){
            thisRunDisabled = true;
            SoundLib.playStarCollected();
        }
    }

    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        if (thisRunDisabled || permanentlyDisabled){
            Matrix.scaleM(blankInstanceInfo,0,0,0,0);
        } else {
            float radians = (float) Math.toRadians(rotation);
            Matrix.setIdentityM(instanceTransform, 0);
            Matrix.translateM(instanceTransform, 0, x, y, 0);
            Matrix.rotateM(instanceTransform, 0, this.rotation, (float) Math.cos(radians * 2), (float) Math.sin(radians * 2), 0);
            Matrix.scaleM(instanceTransform, 0, w, h, 0);

            Matrix.multiplyMM(blankInstanceInfo, 0, uMVPMatrix, 0, instanceTransform, 0);
        }
    }


    public void onPlayerDeath(){
        this.thisRunDisabled = false;
    }

    public void onPlayerSurvive(World world){
        if (thisRunDisabled){
            this.permanentlyDisabled = true;
            world.getCollectedStars().setCollected(starNumber);
        }
    }

    public int getStarNumber(){
        return starNumber;
    }

    public void permamentlyDisable(){
        this.permanentlyDisabled = true;
    }
}
