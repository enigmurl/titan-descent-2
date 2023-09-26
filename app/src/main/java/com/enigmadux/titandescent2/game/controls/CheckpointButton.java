package com.enigmadux.titandescent2.game.controls;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.Camera;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.filestreams.Currency;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class CheckpointButton extends Button {
    private static final int COST = 400;


    public static final float X = 1.5f;
    private static final float Y = 0;
    private static final float W = 1.5f;

    public  boolean isVisible;

    private World world;

    private Text textMesh;

    public CheckpointButton(World world) {
        super(null, R.drawable.checkpoint, X,Y,W,W, 0);
        this.isVisible = false;
        this.world = world;

        this.textMesh = new Text("COST: " + COST,-W/4,-W/2.5f,0.03f);
        this.textMesh.setShader(1,1,1,1);
    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);
        this.textMesh.setData(this,guiData);
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
        world.checkPointLastPlatform();
        Currency.addCurrency(-COST);
        world.getCurrency().writeCurrency();

        this.isVisible = false;
    }

    @Override
    public boolean onTouch(MotionEvent e) {

        if (isVisible && Currency.getCurrency() >= COST){
            return super.onTouch(e);
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
            textMesh.getTextMesh().setCords((this.x - world.getCamera().getCameraX())/World.CAMERA_Z,  -0.03f + (this.y - world.getCamera().getCameraY() - W/2.5f)/(LayoutConsts.SCALE_X *World.CAMERA_Z) );

            super.bufferChildren();
            textMesh.bufferChildren();
        }
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 4f;
    }

    public void updateVisibility(){
        textMesh.updateText("COST: " + COST);

        this.isVisible = !world.getShip().hasLeftLastPlatform() && !world.getShip().getLastPlatform().isCheckpoint();
        if (Currency.getCurrency() >= COST) {
            this.setShader(0,1,0,1);
        } else {
            this.setShader(1,0,0,1);
        }

    }
}
