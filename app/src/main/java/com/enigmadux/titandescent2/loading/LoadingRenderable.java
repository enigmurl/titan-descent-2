package com.enigmadux.titandescent2.loading;

import android.content.Context;
import android.opengl.Matrix;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.Renderable;

public class LoadingRenderable implements Renderable {


    private int id;


    float[] instanceTransform = new float[16];

    float[] textureTransform = new float[] {0,0,1,1};

    public LoadingRenderable(Context context,int pointer){
        id = TextureLoader.loadAndroidTexturePointer(context, pointer)[0];
        Matrix.setIdentityM(instanceTransform,0);
    }


    //return -1 means that it has no fade out effect
    public float getClipR(){
        return 1;
    }


    public float[] getTextureTransform(){
        return textureTransform;
    }

    @Override
    public float[] getInstanceTransform() {
        return instanceTransform;
    }

    @Override
    public int getTextureID() {
        return id;
    }


    @Override
    public float getLayer() {
        return 1;
    }
}
