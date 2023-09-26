package com.enigmadux.titandescent2.guilib;

import android.content.Context;

import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class RoundedRectangleGUI extends Node implements GUIRenderable {


    private int id;



    private float[] shader = new float[] {1,1,1,1};

    public RoundedRectangleGUI(Context context,int pointer,float x,float y,float w,float h){
        super(x, y, w, h);
        id = TextureLoader.loadAndroidTexturePointer(context, pointer)[0];

    }


    @Override
    public int getTextureID() {
        return id;
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
        return shader;
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 1;
    }
}
