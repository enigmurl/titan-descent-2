package com.enigmadux.titandescent2.game;

import com.enigmadux.titandescent2.game.objects.Ship;
import com.enigmadux.titandescent2.util.MathOps;

public class Camera {

    private static final float MAX_SPEED = 20;// + hypotSquared/10 for every additional
    private static final float HYPOT_SQUARE_COEFFECIENT = 1/10f;

    private Ship s;

    private float cameraX,cameraY;


    public Camera(Ship s){
        this.s = s;

    }


    public void reSync(){
        cameraX = s.getX();
        cameraY = s.getY();
    }




    public void update(long dt){
        float deltaX = s.getX() - cameraX;
        float deltaY = s.getY() - cameraY;

        float hypotSquared = MathOps.sqrDist(deltaX,deltaY);
        float maxSpeed = (MAX_SPEED + hypotSquared * HYPOT_SQUARE_COEFFECIENT) * dt/1000;
        if (hypotSquared > maxSpeed * maxSpeed){
            float hypot = (float) Math.sqrt(hypotSquared);
            cameraX += maxSpeed * deltaX/hypot;
            cameraY += maxSpeed * deltaY/hypot;
        } else {
            cameraX += deltaX;
            cameraY += deltaY;
        }
    }


    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }
}
