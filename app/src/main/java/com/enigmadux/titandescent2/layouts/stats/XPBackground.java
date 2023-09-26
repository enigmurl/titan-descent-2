package com.enigmadux.titandescent2.layouts.stats;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class XPBackground extends Node implements GUIRenderable {
    public static final float WIDTH = 1f;
    public static final float HEIGHT = WIDTH/8;

    private int id;
    public XPBackground() {
        super(1 - WIDTH * LayoutConsts.SCALE_X/2, 1 - HEIGHT/2, WIDTH * LayoutConsts.SCALE_X, HEIGHT);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.xp_bar)[0];
    }
    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 0.5f;
    }

    @Override
    public float getCornerSize() {
        return 0;
    }

    @Override
    public float getAspectRatio() {
        return this.w/this.h * LayoutConsts.SCREEN_WIDTH/LayoutConsts.SCREEN_HEIGHT;
    }


    @Override
    public float[] getShader() {
        return DEFAULT_SHADER;
    }

    @Override
    public int getTextureID() {
        return id;
    }

}
