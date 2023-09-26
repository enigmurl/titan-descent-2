package com.enigmadux.titandescent2.layouts.tutorial;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class ThrusterBackground extends Node implements GUIRenderable {
    private static final float ASPECT_RATIO = 1024/400f;

    private int id;

    public ThrusterBackground() {
        super(0,0,2 * ASPECT_RATIO * LayoutConsts.SCALE_X,2);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.thruster_background)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getCornerSize() {
        return 0;
    }

    @Override
    public float getAspectRatio() {
        return this.w/this.h * LayoutConsts.SCREEN_WIDTH/LayoutConsts.SCREEN_HEIGHT ;
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
    public float getLayer() {
        return parentLayout.getLayer() + 1;
    }
}
