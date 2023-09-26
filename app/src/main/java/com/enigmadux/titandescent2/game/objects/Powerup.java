package com.enigmadux.titandescent2.game.objects;

import android.media.SoundPool;
import android.opengl.Matrix;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.TitanCollectionElem;
import com.enigmadux.titandescent2.util.SoundLib;

public abstract class Powerup extends TitanCollectionElem {

    protected float[] instanceTransform = new float[16];

    public Powerup(int instanceID,float x, float y,float w,float h) {
        super(instanceID);

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void update(long dt, World world) {
        Ship s = world.getShip();
        if (s.intersects(x, y, w, h)){
            this.onCollect(world,s);
        }


    }

    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0, x, y,0);
        Matrix.scaleM(instanceTransform,0, w, h,0);

        Matrix.multiplyMM(blankInstanceInfo,0,uMVPMatrix,0,instanceTransform,0);
    }






    abstract void onCollect(World world,Ship s);

}
