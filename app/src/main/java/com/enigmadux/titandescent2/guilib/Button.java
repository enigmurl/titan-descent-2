package com.enigmadux.titandescent2.guilib;

import android.content.Context;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.util.MathOps;
import com.enigmadux.titandescent2.util.SoundLib;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class Button extends Node implements GUIRenderable, QuadRenderable, Clickable {



    private int id;


    float[] textureTransform = new float[] {0,0,1,1};

    protected float[] shader = new float[] {1,1,1,1};

    private float cornerSize;

    private boolean isDown;


    public Button(Context context,int pointer,float x,float y,float w,float h,float cornerSize){
        super(x,y,w,h);
        id = TextureLoader.loadAndroidTexturePointer(context, pointer)[0];


        this.cornerSize = cornerSize;
    }



    @Override
    public int getTextureID() {
        return id;
    }

    @Override
    public float getCornerSize() {
        return cornerSize;
    }

    @Override
    public float getAspectRatio() {
        return this.w/this.h * LayoutConsts.SCREEN_WIDTH/LayoutConsts.SCREEN_HEIGHT;
    }

    @Override
    public float[] getTextureTransform() {
        return textureTransform;
    }

    @Override
    public float[] getShader() {
        return shader;
    }


    protected boolean intersects(float x,float y){
        return x < this.x + w/2 &&
                x > this.x - w/2 &&
                y < this.y + h/2 &&
                y > this.y - h/2;
    }

    protected boolean intersects(MotionEvent e){
        int pointerInd  = e.getActionIndex();

        float x = MathOps.getOpenGLX(e.getX(pointerInd));
        float y = MathOps.getOpenGLY(e.getY(pointerInd));
        return intersects(x,y);
    }


    @Override
    public boolean onTouch(MotionEvent e) {
        if (this.intersects(e)) {
            if (this.isDown && e.getActionMasked() == MotionEvent.ACTION_UP) {
                this.onHardRelease(e);
                return true;
            } else if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                this.onPress(e);
                return true;
            }
            return this.isDown;
        } else if (this.isDown) {
            this.onSoftRelease(e);
            return true;
        }
        return false;
    }


    protected void onPress(MotionEvent e){
        SoundLib.playButtonClicked();
        this.isDown = true;
        Matrix.scaleM(instanceTransform,0,0.8f,0.8f,1);
    }

    protected void onHardRelease(MotionEvent e){
        this.isDown = false;
        Matrix.scaleM(instanceTransform,0,1/0.8f,1/0.8f,1);

    }

    protected void onSoftRelease(MotionEvent e){
        this.isDown = false;
        Matrix.scaleM(instanceTransform,0,1/0.8f,1/0.8f,1);

    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 1;
    }

    public void setShader(float r,float g,float b,float a){
        this.shader[0] = r;
        this.shader[1] = g;
        this.shader[2] = b;
        this.shader[3] = a;
    }

}
