package com.enigmadux.titandescent2.guilib;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.gamelib.GUIData;

public abstract class Node {

    protected static final float[] DEFAULT_SHADER = new float[] {1,1,1,1};

    protected static final float[] DEFAULT_TEXTURE_TRANSFORM = new float[] {0,0,1,1};


    protected Node parentLayout;
    protected GUIData guiData;


    protected float x,y,w,h;

    protected float[] instanceTransform = new float[16];

    private float[] totalTransform = new float[16];

    public Node(float x,float y,float w,float h){

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.recalculateTransform();
    }


    protected void recalculateTransform(){
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y,0);
        Matrix.scaleM(instanceTransform,0,w,h,1);

    }


    public void setData(Node parent, GUIData guiData){
        this.parentLayout = parent;
        this.guiData = guiData;

    }


    public float[] getInstanceTransform() {
        if (parentLayout == null){
            return instanceTransform;
        }
        Matrix.multiplyMM(totalTransform,0,parentLayout.getInstanceTransform(),0,instanceTransform,0);
        return totalTransform;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }


    public abstract void bufferChildren();

    public abstract float getLayer();

}
