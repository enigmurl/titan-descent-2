package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;
import android.util.Log;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Node;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class Thruster extends Node implements QuadRenderable {
    private static final float FORCE =  7.5f;

    private static final float EXHAUST_EXTENSION = 1.1f;

    private static final float TORQUE_FORCE = 900;


    static final float[] LEFT_THRUSTER_OFFSET = new float[] {-0.3f,0.1f};
    static final float[] RIGHT_THRUSTER_OFFSET = new float[] {-LEFT_THRUSTER_OFFSET[0],LEFT_THRUSTER_OFFSET[1]};

    //where it rotates around in reference to a node of size 1 x 1 so x is in range of [-0.5,0.5], and same for y
    public  static final float[] ANCHOR = new float[] {-0.4f,0};

    private int id;

    private float degrees;

    private Ship s;

    private boolean enabled = false;


    public Thruster(Ship s, float x, float y, float w, float h) {
        super(x, y, w, h);
        this.s = s;

        this.id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.thruster)[0];
    }

    public void updateRotation(float degrees){
        this.degrees = degrees;

    }

    public void update(World world,long dt){
        if (enabled && world.getShip().getFuel() > 0){
            float radians = (float) Math.toRadians(this.degrees);
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);

            float vx = - cos * FORCE * dt/1000;
            float vy = - sin * FORCE * dt/1000;

            //first calculate where the thrust is even coming from
            float nonRotx = 0.5f * w - ANCHOR[0] * w;//where it starts
            float nonRoty = 0f * h - ANCHOR[1] * h;


            float x = cos * nonRotx - nonRoty * sin;
            float y = cos * nonRoty + nonRotx * sin;

            x += this.x;
            y += this.y;

            x *= this.s.getW();
            y *= this.s.getH();

            //two dimensional cross product
            float torque = x * vy - vx * y;

            this.s.addTorque((float) Math.toDegrees(torque) * TORQUE_FORCE * dt/1000);
            this.s.addRelativeForce(vx,vy);

            x *= EXHAUST_EXTENSION;
            y *= EXHAUST_EXTENSION;

            float sCos = (float) Math.cos(Math.toRadians(s.getRotation()));
            float sSin = (float) Math.sin(Math.toRadians(s.getRotation()));

            float tX = sCos * x - y * sSin;
            float tY = sCos * y + x * sSin;

            float tCos = cos * sCos - sin * sSin;
            float tSin = cos * sSin + sin * sCos;

            float vX = tCos * Exhaust.SPEED + s.getvX();
            float vY = tSin * Exhaust.SPEED + s.getvY();

            Exhaust.addInstance(tX + s.getX(),tY + s.getY(),vX,vY);
        }
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.quadRenderer.buffer(this);
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
        return parentLayout.getLayer() + 0.01f;
    }

    @Override
    public float[] getInstanceTransform() {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,x,y,0);
        Matrix.rotateM(instanceTransform,0,degrees,0,0,1);
        Matrix.translateM(instanceTransform,0,-ANCHOR[0] * w,-ANCHOR[1] * h ,0);
        Matrix.scaleM(instanceTransform,0,w,h,1);

        return super.getInstanceTransform();
    }

    public float getDegrees() {
        return degrees;
    }

    public void setState(boolean state){
        this.enabled = state;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
