package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;
import android.util.Log;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.util.MathOps;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class BackgroundDynamics extends Node implements QuadRenderable {
    private static final float DIM_SCALE = 2f;


    private int id;
    private float z;
    private World world;


    public BackgroundDynamics(World world,int pointer, float z) {
        super(0,0, 1, 1);
        this.world = world;

        this.z = z;

        id = TextureLoader.loadAndroidTexturePointer(null,pointer)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.quadRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 1 -  z/100f;
    }

    @Override
    public float[] getTextureTransform() {
        return DEFAULT_TEXTURE_TRANSFORM;
    }

    @Override
    public float[] getShader() {
        return DEFAULT_SHADER;
    }

    @Override
    public int getTextureID() {
        return id;
    }

    @Override
    public float[] getInstanceTransform() {
        float cameraX = world.getCamera().getCameraX();

        //float[] buffer = parentLayout.getInstanceTransform();
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0, MathOps.clip(LayoutConsts.SCALE_X * -cameraX/z,1 - DIM_SCALE,-1 + DIM_SCALE),0,0);
        Matrix.scaleM(instanceTransform,0, 2* DIM_SCALE,2,0);

        return instanceTransform;
    }
}
