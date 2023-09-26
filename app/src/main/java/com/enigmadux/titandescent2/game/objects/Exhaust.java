package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.TitanCollectionElem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Exhaust extends TitanCollectionElem {
    public static final int MAX_INSTANCES = 128;

    private static final long MILLIS = 100;
    private static final float R = 0.1f;
    static final float SPEED = 8;//per second

    private static final Queue<Exhaust> AVAILABLE_EXHAUST = new LinkedList<>();


    private float vX;
    private float vY;

    private long elapsedMillis = 0;
    private float x;
    private float y;


    private float[] instanceTransform = new float[16];

    private boolean finished = false;

    private float renderX;
    private float renderY;

    public Exhaust(int instanceID) {
        super(instanceID);
    }

    @Override
    public void update(long dt, World world) {
        if (finished) {
            return;
        }

        this.elapsedMillis += dt;
        if (elapsedMillis > MILLIS){
            AVAILABLE_EXHAUST.add(this);
            finished = true;
            return;
        }
        this.shader[3] = 1 - (float) elapsedMillis/MILLIS;

        this.x += vX * dt/1000;
        this.y += vY * dt/1000;


    }

    @Override
    public void updateInstanceTransform(float[] blankInstanceInfo, float[] uMVPMatrix) {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0, renderX, renderY,0);
        Matrix.scaleM(instanceTransform,0, finished ? 0 : R * 2, finished ? 0 : R * 2,0);

        Matrix.multiplyMM(blankInstanceInfo,0,uMVPMatrix,0,instanceTransform,0);
    }


    public static void init(World world){
        AVAILABLE_EXHAUST.clear();
        for (int i = 0;i <MAX_INSTANCES;i++){
            Exhaust e = new Exhaust(world.getThrust().createVertexInstance());
            AVAILABLE_EXHAUST.add(e);
            e.finished = true;
            world.getThrust().addInstance(e);

        }
    }

    public static void addInstance(float x,float y,float vX,float vY){
        Exhaust instance = AVAILABLE_EXHAUST.poll();
        if (instance == null){
            throw new IllegalStateException("More than: " + MAX_INSTANCES + " exhaust instances were requested");
        }

        instance.elapsedMillis = 0;
        instance.x = x;
        instance.y = y;
        instance.renderX = 120312030;
        instance.renderY = 123012031;
        instance.vX = vX;
        instance.vY = vY;
        instance.finished = false;


        instance.setShader(1,1,1,1);
    }

    public void startRendering(){
        renderX = x;
        renderY = y;
    }
}
