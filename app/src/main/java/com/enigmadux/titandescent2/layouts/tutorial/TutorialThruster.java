package com.enigmadux.titandescent2.layouts.tutorial;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Thruster;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class TutorialThruster extends Node implements GUIRenderable {
    private static final float ASPECT_RATIO = 300/90f;
    public static final float RADIUS = 1.5f;

    private int id;
    private World world;
    private float[] shader = new float[]{1,1,1,0.5f};

    public TutorialThruster(float x, float y,World world) {
        super(x  + (x < 0 ? 0:-0) * Thruster.ANCHOR[0] * RADIUS * LayoutConsts.SCALE_X, y, RADIUS, RADIUS/( ASPECT_RATIO));

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.thruster)[0];
        this.world = world;


    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 1.5f;
    }

    @Override
    public float getCornerSize() {
        return 0;
    }

    @Override
    public float getAspectRatio() {
        return LayoutConsts.SCREEN_WIDTH * w/ LayoutConsts.SCREEN_HEIGHT/h;
    }

    @Override
    public float[] getShader() {
        return shader;
    }

    @Override
    public int getTextureID() {
        return id;
    }

    @Override
    public float[] getInstanceTransform() {
        Thruster t = (x < 0)  ? world.getShip().getLeftThruster() : world.getShip().getRightThruster();

        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y,0);
        Matrix.scaleM(instanceTransform,0,LayoutConsts.SCALE_X,1,1);
        Matrix.rotateM(instanceTransform,0, t.getDegrees(),0,0,1);
        Matrix.translateM(instanceTransform,0,-Thruster.ANCHOR[0] * w ,-Thruster.ANCHOR[1] * h ,0);
        Matrix.scaleM(instanceTransform,0,w,h,1);


        return super.getInstanceTransform();
    }
}
