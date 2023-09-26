package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.util.MathOps;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class RedGlow extends Node implements QuadRenderable {

    private float[] shader = new float[] {1,1,1,0};

    private int id;


    private Ship s;
    public RedGlow(Ship s,float x, float y, float w, float h) {
        super(x, y, w, h);
        this.s = s;
        this.id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.red_heat)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.quadRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() +0.3f;
    }

    @Override
    public float[] getTextureTransform() {
        return DEFAULT_TEXTURE_TRANSFORM;
    }

    @Override
    public float[] getShader() {
        return shader;
    }

    @Override
    public int getTextureID() {
        return id;
    }

    public void setAlpha(float alpha){
        this.shader[3] = alpha;
    }


    @Override
    public float[] getInstanceTransform() {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.rotateM(instanceTransform,0,this.getRotation() - s.getRotation(),0,0,1);
        Matrix.scaleM(instanceTransform,0,w,h,1);
        Matrix.translateM(instanceTransform,0,x,y ,0);

        return super.getInstanceTransform();
    }


    //degrees
    private float getRotation(){
        float vX = s.getvX();
        float vY = s.getvY();

        float hypot = (float) Math.hypot(vX,vY);
        if (hypot == 0){
            return 0;
        }
        return (float) Math.toDegrees(MathOps.getAngle(vX/hypot,vY/hypot));
    }


}
