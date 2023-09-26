package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.TitanCollectionElem;
import com.enigmadux.titandescent2.util.SoundLib;

public class Cloud extends TitanCollectionElem {


    private float[] instanceTransform = new float[16];

    public Cloud(int instanceID,float x, float y,float r) {
        super(instanceID);

        this.x = x;
        this.y = y;
        this.w = 2 * r;
        this.h = 2 * r;
    }

    @Override
    public void update(long dt, World world) {
        Ship s = world.getShip();
        if (s.intersects(x, y, w/2)){
            s.death(world);
            SoundLib.playCloudDeath();
        }

    }

    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0, x, y,0);
        Matrix.scaleM(instanceTransform,0, w, h,0);

        Matrix.multiplyMM(blankInstanceInfo,0,uMVPMatrix,0,instanceTransform,0);
    }

}
