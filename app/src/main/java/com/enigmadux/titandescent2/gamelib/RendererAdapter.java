package com.enigmadux.titandescent2.gamelib;

import android.content.Context;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.guilib.dynamicText.DynamicText;

import enigmadux2d.core.renderEngine.GUIRenderer;
import enigmadux2d.core.renderEngine.LoadingRenderer;
import enigmadux2d.core.renderEngine.MeshRenderer;
import enigmadux2d.core.renderEngine.QuadRenderer;

public class RendererAdapter {
    private RendererAdapter(){

    }


    public static LoadingRenderer loadingRenderer;

    public static GUIRenderer guiRenderer;

    public static QuadRenderer quadRenderer;

    public static MeshRenderer meshRenderer;

    public static DynamicText dynamicText;


    public static void init(Context context){
        loadingRenderer = new LoadingRenderer(context);
        guiRenderer = new GUIRenderer(context);
        quadRenderer = new QuadRenderer(context);
        meshRenderer = new MeshRenderer(context);
        dynamicText = new DynamicText(context, R.drawable.orbitron_texture_atlas,R.raw.orbitron_texture_atlas);
    }


    public static void render(){
        loadingRenderer.render();
        quadRenderer.render();
        meshRenderer.render();
        guiRenderer.render();
        dynamicText.render();
    }
}
