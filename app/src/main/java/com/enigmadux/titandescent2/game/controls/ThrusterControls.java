package com.enigmadux.titandescent2.game.controls;

import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.loading.Hyperbola;
import com.enigmadux.titandescent2.util.MathOps;

public class ThrusterControls {
    public static final float RADIAN_SWEEP = (float)  Math.PI/8;

    public static final float[] LEFT_ROTATION_POINT = new float[] {-0.5f,0.4f};
    public static final float[] RIGHT_ROTATION_POINT = new float[] {-LEFT_ROTATION_POINT[0],LEFT_ROTATION_POINT[1]};


    private int leftPointer = -122312413;
    private int rightPointer = -132141232;

    private boolean leftEnabled;
    private boolean rightEnabled;

    public boolean onTouch(MotionEvent e, World world){
        int pointerInd  = e.getActionIndex();

        float x = MathOps.getOpenGLX(e.getX(pointerInd));

        if (e.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN || e.getActionMasked() == MotionEvent.ACTION_DOWN){
            if (x < 0) {
                world.getShip().setLeftThrusterState(true);

                leftPointer = e.getPointerId(e.getActionIndex());
                leftEnabled = true;

            } else {
                world.getShip().setRightThrusterState(true);

                rightPointer = e.getPointerId(e.getActionIndex());
                rightEnabled = true;
            }
        } else if ((e.getActionMasked() == MotionEvent.ACTION_POINTER_UP || e.getActionMasked()== MotionEvent.ACTION_UP) ){
            if (e.getPointerId(e.getActionIndex()) == rightPointer && rightEnabled) {
                world.getShip().setRightThrusterState(false);
                rightEnabled = false;
            } else if (e.getPointerId(e.getActionIndex()) == leftPointer && leftEnabled){
                world.getShip().setLeftThrusterState(false);
                leftEnabled = false;
            }
        }


        if (leftEnabled){
            x = MathOps.getOpenGLX(e.getX(e.findPointerIndex(leftPointer)));
            float y = MathOps.getOpenGLY(e.getY(e.findPointerIndex(leftPointer)));

            float angle = leftClip(this.getAngle(LEFT_ROTATION_POINT,x,y));

            world.getShip().setLeftThrusterAngle((float) Math.toDegrees(angle));
        }
        if (rightEnabled){
            x = MathOps.getOpenGLX(e.getX(e.findPointerIndex(rightPointer)));
            float y = MathOps.getOpenGLY(e.getY(e.findPointerIndex(rightPointer)));

            float angle = rightClip(this.getAngle(RIGHT_ROTATION_POINT,x,y));

            world.getShip().setRightThrusterAngle((float) Math.toDegrees(angle));
        }


        return true;
    }

    private float rightClip(float angle){
        if (angle > Math.PI/2 && angle < 3 * Math.PI/2){
            return 3 * (float) Math.PI/2;
        } else if (angle < Math.PI/2 || angle > 3 *Math.PI/2 + RADIAN_SWEEP){
            return 3 * (float) Math.PI/2 + RADIAN_SWEEP;
        }
        return angle;
    }
    private float leftClip(float angle){
        if (angle > Math.PI/2 && angle < 3 * Math.PI/2 - RADIAN_SWEEP){
            return 3 * (float) Math.PI/2 - RADIAN_SWEEP;
        } else if (angle < Math.PI/2 || angle > 3 *Math.PI/2 ){
            return 3 * (float) Math.PI/2;
        }
        return angle;
    }

    private float getAngle(float[] org,float x,float y){

        float hyp = (float) Math.hypot(org[0] - x,org[1] - y);
        if (hyp == 0){
            return 3 * (float)Math.PI/2;
        }
        return MathOps.getAngle((x - org[0])/hyp,(y-org[1])/hyp);
    }

    public void disengage(){
        this.leftEnabled = false;
        this.rightEnabled = false;
    }
}

