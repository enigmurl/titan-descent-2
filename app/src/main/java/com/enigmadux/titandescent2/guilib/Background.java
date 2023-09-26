package com.enigmadux.titandescent2.guilib;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class Background extends Node implements GUIRenderable {
    private static final float DEFAULT_ROUNDNESS = 92f/512f;


    private int id;

    public Background(float x, float y, float w, float h) {
        super(x, y, w, h);

        this.id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.layout_background)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getCornerSize() {
        return DEFAULT_ROUNDNESS * this.w;
    }

    @Override
    public float getAspectRatio() {
        return this.w/this.h * LayoutConsts.SCREEN_WIDTH/LayoutConsts.SCREEN_HEIGHT;
    }

    @Override
    public float[] getShader() {
        return  DEFAULT_SHADER;
    }

    @Override
    public int getTextureID() {
        return id;
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 0.75f;
    }
}
