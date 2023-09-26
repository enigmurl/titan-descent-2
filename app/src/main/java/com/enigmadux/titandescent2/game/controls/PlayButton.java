package com.enigmadux.titandescent2.game.controls;

import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.Camera;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.main.TitanActivity;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class PlayButton extends Button {
    private static final float X = 0;
    private static final float Y = 0;
    private static final float W = 1.5f;

    public boolean isVisible;

    private World world;
    private CheckpointButton checkpointButton;
    public PlayButton(World world) {
        super(null, R.drawable.resume_button, X,Y,W,W, 0);
        this.isVisible = false;
        this.world = world;
    }

    public void setCheckpointButton(CheckpointButton checkpointButton) {
        this.checkpointButton = checkpointButton;
    }

    @Override
    protected boolean intersects(float x, float y) {
        Camera camera = world.getCamera();
        return super.intersects(x * World.CAMERA_Z + camera.getCameraX(), y * (LayoutConsts.SCALE_X * World.CAMERA_Z) + camera.getCameraY());
    }

    @Override
    protected void onPress(MotionEvent e) {
        super.onPress(e);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);
        World world = guiData.getLayout(World.class);

        world.getShip().leavePlatform();
        this.isVisible = false;
        this.checkpointButton.isVisible = false;

    }

    @Override
    public boolean onTouch(MotionEvent e) {
        if (isVisible){
            super.onTouch(e);
            return true;
        }

        return false;
    }

    public void setCords(float x,float y){
        this.x = x;
        this.y = y;
        this.recalculateTransform();
    }

    @Override
    public void bufferChildren() {
        if (isVisible) {
            super.bufferChildren();
        }
    }

    @Override
    public float getLayer() {
        return  parentLayout.getLayer() + 4.5f;
    }
}
