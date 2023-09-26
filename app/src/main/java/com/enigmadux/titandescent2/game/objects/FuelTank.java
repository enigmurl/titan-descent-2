package com.enigmadux.titandescent2.game.objects;


import android.opengl.Matrix;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.util.SoundLib;

public class FuelTank extends Powerup {
    private boolean disabled;
    public FuelTank(int instanceID,float x,float y) {
        super(instanceID,x,y,0.75f,1);
    }

    @Override
    void onCollect(World world, Ship s) {
        if (! disabled) {
            s.setFuel(1);
            this.disabled = true;
            SoundLib.playFuelCollected();
        }
    }

    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        if (! disabled) {
            super.updateInstanceTransform(blankInstanceInfo, uMVPMatrix);
        } else {
            Matrix.scaleM(blankInstanceInfo,0,0,0,0);
        }
    }
    public void reEnable(){
        this.disabled =false;
    }

}
