package com.enigmadux.titandescent2.layouts.tutorial;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.objects.Thruster;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class Circle extends Node implements GUIRenderable {

    private static final float RADIUS = 0.75f;
    private int id;

    public Circle(float x, float y) {
        super(x, y, RADIUS * 2 * LayoutConsts.SCALE_X, RADIUS* 2);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.thruster_circle)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer()+1.25f;
    }

    @Override
    public float getCornerSize() {
        return 0;
    }

    @Override
    public float getAspectRatio() {
        return this.w/this.h;
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
