package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class FuelIndicator extends Node implements QuadRenderable {

    private int id;

    private float fuel;

    private float[] textureTransform = new float[] {0,0,1,1};
    public FuelIndicator(float x, float y, float w, float h) {
        super(x, y, w, h);

        this.id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.fuel_tank)[0];
    }
    @Override
    public void bufferChildren() {
        RendererAdapter.quadRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 0.01f;
    }

    @Override
    public float[] getTextureTransform() {
        return textureTransform;
    }

    @Override
    public float[] getShader() {
        return DEFAULT_SHADER;
    }

    @Override
    public int getTextureID() {
        return id;
    }

    public void updateFuel(float fuel){
        this.fuel = fuel;
    }

    @Override
    public float[] getInstanceTransform() {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y + (fuel-1) * h/2,0);
        Matrix.scaleM(instanceTransform,0,w,h * fuel,1);

        textureTransform[1] = 1-fuel;
        textureTransform[3] = fuel;

        return super.getInstanceTransform();
    }

}

