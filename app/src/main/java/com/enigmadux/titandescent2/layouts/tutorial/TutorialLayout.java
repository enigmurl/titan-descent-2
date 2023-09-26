package com.enigmadux.titandescent2.layouts.tutorial;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.controls.ThrusterControls;
import com.enigmadux.titandescent2.guilib.Layout;

public class TutorialLayout extends Layout {

    private int state = -1;
    private Helper overallHelper;
    private Helper fuelHelper;
    private Helper checkpointHelper;

    private ThrusterBackground thrusterBackground;
    private TutorialThruster leftThruster;
    private TutorialThruster rightThruster;

    private Circle leftcircle;
    private Circle rightcircle;

    private Pointer leftPointer;
    private Pointer rightPointer;

    private World world;

    public TutorialLayout() {


    }
    @Override
    public void setUpChildren() {
        world = guiData.getLayout(World.class);

        this.overallHelper = new Helper(R.drawable.overall_helper);
        this.fuelHelper = new Helper(R.drawable.fuel_helper);
        this.checkpointHelper = new Helper(R.drawable.checkpoint_helper);
        this.thrusterBackground = new ThrusterBackground();
        this.leftThruster = new TutorialThruster(ThrusterControls.LEFT_ROTATION_POINT[0],ThrusterControls.LEFT_ROTATION_POINT[1],world);
        this.rightThruster = new TutorialThruster(ThrusterControls.RIGHT_ROTATION_POINT[0],ThrusterControls.RIGHT_ROTATION_POINT[1],world);

        this.leftcircle = new Circle(ThrusterControls.LEFT_ROTATION_POINT[0],ThrusterControls.LEFT_ROTATION_POINT[1]);
        this.rightcircle = new Circle(ThrusterControls.RIGHT_ROTATION_POINT[0],ThrusterControls.RIGHT_ROTATION_POINT[1]);

        this.leftPointer = new Pointer(ThrusterControls.LEFT_ROTATION_POINT[0],ThrusterControls.LEFT_ROTATION_POINT[1]);
        this.rightPointer = new Pointer(ThrusterControls.RIGHT_ROTATION_POINT[0],ThrusterControls.RIGHT_ROTATION_POINT[1]);


        this.addChild(leftThruster);
        this.addChild(rightThruster);
        this.addChild(checkpointHelper);
        this.addChild(fuelHelper);
        this.addChild(overallHelper);
        this.addChild(thrusterBackground);
        this.addChild(leftcircle);
        this.addChild(rightcircle);
        this.addChild(leftPointer);
        this.addChild(rightPointer);
    }

    @Override
    public void bufferChildren() {
        switch (state){
            case 0:
                this.overallHelper.bufferChildren();
                break;
            case 1:
                this.fuelHelper.bufferChildren();
                break;
            case 2:
                this.checkpointHelper.bufferChildren();
                break;
            case 3:
                this.thrusterBackground.bufferChildren();
                leftcircle.bufferChildren();
                rightcircle.bufferChildren();
                rightPointer.bufferChildren();
                leftPointer.bufferChildren();
            default:
                this.leftThruster.bufferChildren();
                this.rightThruster.bufferChildren();
        }
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_UP){
            state++;
            if (state == 4){
                world.getShip().leavePlatform();

                world.playButton.isVisible = false;
                world.checkpointButton.isVisible = false;
            }
        }
        if (state == 0 || state == 1 || state == 2 || state == 3){
            super.onTouch(e);
            return true;
        }


        return false;
    }

    @Override
    public void onShow() {
        super.onShow();
        this.state = 0;
    }

    @Override
    public void onHide() {
        super.onHide();
        this.state = -1;
    }
}
