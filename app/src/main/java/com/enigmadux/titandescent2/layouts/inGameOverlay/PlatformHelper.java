package com.enigmadux.titandescent2.layouts.inGameOverlay;

import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.game.objects.Thruster;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.util.MathOps;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

public class PlatformHelper extends Node implements GUIRenderable {

    //in height
    private static final float PADDING = 0.05f;
    private static final float RADIUS = 0.15f;
    private int id;

    private float[] shader = new float[] {1,1,1,0f};

    private float degrees;

    private boolean showing;
    public PlatformHelper() {
        super(0,0, RADIUS * 2 * LayoutConsts.SCALE_X, RADIUS* 2);

        id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.platform_helper)[0];
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
        return shader;
    }

    @Override
    public int getTextureID() {
        return id;
    }


    public void update(World world){
        Platform p = world.getShip().getNextPlatform();

        float relX = p.getX() - world.getCamera().getCameraX();
        float relY = p.getY() - world.getCamera().getCameraY();

        float screenX = relX/World.CAMERA_Z;
        float screenY = relY/(World.CAMERA_Z * LayoutConsts.SCALE_X);

        if (screenX < -1 || screenX > 1 || screenY < -1 || screenY > 1){
            float hypot = (float) Math.hypot(screenX,screenY);
            float cos  = screenX/hypot;
            float sin  = screenY/hypot;

            float rectHeight = 2 - 2 * (PADDING) - h;
            float rectWidth = 2 - 2 * (PADDING + h/2) * LayoutConsts.SCALE_X;

            this.x = intersectsX(cos,sin,rectWidth,rectHeight);
            this.y = intersectsY(cos,sin,rectWidth,rectHeight);

            this.degrees = (float) Math.toDegrees(MathOps.getAngle(cos,sin));
            this.shader[3] = 0.5f;
            this.showing = true;
        } else {
            this.showing = false;
            this.shader[3] = 0;
        }
    }

    @Override
    public float[] getInstanceTransform() {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y,0);
        Matrix.scaleM(instanceTransform,0,showing ? w:0,showing ? h:0,1);
        Matrix.rotateM(instanceTransform,0,degrees,0,0,1);


        return super.getInstanceTransform();
    }

    private static float intersectsX(float cos,float sin,float rectWidth,float rectHeight){
        if (cos == 0){
            return 0;
        } else if (sin == 0){
            return cos > 0 ? rectWidth/2 : -rectWidth/2;
        }
        float tan = sin/cos;
        if (tan < rectHeight/rectWidth && tan > -rectHeight/rectWidth && cos > 0){
            return rectWidth/2;
        }
        else if (tan < rectHeight/rectWidth && tan > -rectHeight/rectWidth && cos < 0){
            return -rectWidth/2;
        }

        //y = x * tan
        //x = y/tan
        float cot = cos /sin;

        return sin > 0 ? rectHeight/2 * cot : -rectHeight/2 * cot;

    }

    private static float intersectsY(float cos,float sin,float rectWidth,float rectHeight){
        if (cos == 0){
            return sin > 0 ? rectHeight/2 :-rectHeight/2;
        }


        float tan = sin/cos;
        //top
        if (tan > rectHeight/rectWidth && cos > 0 || tan < -rectHeight/rectWidth && cos < 0){
            return rectHeight/2;
        } //bottom
        else if (tan > rectHeight/rectWidth && cos < 0 || tan < -rectHeight/rectWidth && cos > 0){
            return -rectHeight/2;
        }

        return cos > 0 ? rectWidth/2 * tan : -rectWidth/2 * tan;

    }
}
