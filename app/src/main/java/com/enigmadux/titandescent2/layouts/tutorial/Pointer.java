package com.enigmadux.titandescent2.layouts.tutorial;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.controls.ThrusterControls;
import com.enigmadux.titandescent2.game.objects.Thruster;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class Pointer extends Node implements GUIRenderable {

    private static final float RADIUS = 0.15f;
    private int id;

    private long start = System.currentTimeMillis();

    public Pointer(float x, float y) {
        super(x, y, RADIUS * 2 , RADIUS* 2);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.tutorial_pointer)[0];
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.guiRenderer.buffer(this);
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer()+1.55f;
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

    @Override
    public float[] getInstanceTransform() {

        final long totalTravel = 1250;
        long delta = Math.min(totalTravel,(System.currentTimeMillis() - start) % (2 * totalTravel));

        float rotation = 270  + (float) Math.toDegrees((x < 0 ? -1 : 1) * delta * ThrusterControls.RADIAN_SWEEP/totalTravel);

        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y,0);
        Matrix.scaleM(instanceTransform,0,LayoutConsts.SCALE_X,1,1);
        Matrix.rotateM(instanceTransform,0,rotation,0,0,1);
        Matrix.translateM(instanceTransform,0,TutorialThruster.RADIUS * LayoutConsts.SCALE_X,0 ,0);
        Matrix.scaleM(instanceTransform,0,w,h,1);

        return super.getInstanceTransform();
    }
}
