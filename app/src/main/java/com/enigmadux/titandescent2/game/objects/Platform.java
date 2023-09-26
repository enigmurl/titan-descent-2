package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;
import android.text.method.HideReturnsTransformationMethod;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.TitanCollectionElem;
import com.enigmadux.titandescent2.util.MathOps;

public class Platform extends TitanCollectionElem {

    private static final long FLASHING_MILLLIS = 1000;
    private static final long FLASHING_COUNT = 20;//amouont of flashes in the 2000 milli seconds

    public static final int BASE_ID = 0;
    public static final int END_ID = -1;

    private float[] instanceTransform = new float[16];


    private int platformID;
    private boolean isCheckpoint = false;
    private boolean isCompleted = false;

    private long flashingMillis = 0;

    private boolean hidden;

    public Platform(int instanceID,float x,float y,float w,float h,int platformID) {
        super(instanceID);
        if (platformID == BASE_ID){
            this.setCheckpoint();
        }


        this.x = x;
        this.y = y;
        this.w = w;
        this.h =h;

        this.platformID = platformID;

        this.resetShader();
    }

    @Override
    public void setShader(float r, float g, float b, float a) {
        super.setShader(r, g, b, a);

        if (a == 0){
            flashingMillis = 0;
        }
        this.hidden = a == 0;
    }

    @Override
    public void update(long dt, World world) {
        if (hidden) {
            this.shader[3] = 0;
        }
        if (this.shader[3] != 0 && flashingMillis > 0){
            flashingMillis -= dt;
            this.resetShader();
            //sin is -1 to 1, but sin^2 is 0 to 1
            float sin = getSin((float) (FLASHING_MILLLIS - flashingMillis)/FLASHING_MILLLIS);
            float t = sin * sin;
            this.shader[0] = t * shader[0] + 1 - t;
            this.shader[1] = t * shader[1] + 1 - t;
            this.shader[2] = t * shader[2] + 1 - t;
            if (flashingMillis <= 0){
                this.resetShader();
            }
        }

    }

    private float getSin(float t){
        return (float) Math.sin(t * FLASHING_COUNT);
    }



    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0, x, y,0);
        Matrix.scaleM(instanceTransform,0, w, h,0);

        Matrix.multiplyMM(blankInstanceInfo,0,uMVPMatrix,0,instanceTransform,0);
    }

    @Override
    public void resetShader() {
        if (platformID == END_ID){
            this.setShader(1,89/255f,0,1);
        } else if (isCheckpoint){
            this.setShader(0.04f, 0.696f, 0.125f, 1);
        } else if (isCompleted){
            this.setShader(0.047f,0.498f,0.6f,1);
        } else {
            this.setShader(0.91f,0.17f,0.17f,1);
        }
    }

    public int getPlatformID() {
        return platformID;
    }

    public void setCheckpoint(){
        this.isCheckpoint = true;
        this.resetShader();
    }

    public void setCompleted(){
        this.isCompleted = true;
        this.resetShader();

    }

    public boolean isCheckpoint() {
        return isCheckpoint;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void perfectLanding(){
        this.flashingMillis = FLASHING_MILLLIS;
    }
}
