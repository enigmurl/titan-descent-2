package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;

import java.util.ArrayList;
import java.util.List;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class Background extends Node implements QuadRenderable {

    private int id;

    private List<BackgroundDynamics> backgroundDynamicsList = new ArrayList<>();

    public Background() {
        super(0,0,2,2);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.titanbackground)[0];


    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);

        World world = guiData.getLayout(World.class);
        backgroundDynamicsList.add(new BackgroundDynamics(world,R.drawable.mountains1,40));
        backgroundDynamicsList.add(new BackgroundDynamics(world,R.drawable.mountains2,30));


        for (BackgroundDynamics backgroundDynamics: backgroundDynamicsList){
            backgroundDynamics.setData(this,guiData);
        }
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.quadRenderer.buffer(this);
        for (int i = 0;i < backgroundDynamicsList.size();i++){
            backgroundDynamicsList.get(i).bufferChildren();
        }
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
    public float getLayer() {
        return parentLayout.getLayer() + 0.1f;
    }

    @Override
    public float[] getInstanceTransform() {
        this.recalculateTransform();
        return instanceTransform;
    }


}
