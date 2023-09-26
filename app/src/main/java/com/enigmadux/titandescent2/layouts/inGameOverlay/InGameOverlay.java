package com.enigmadux.titandescent2.layouts.inGameOverlay;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.layouts.stats.Stats;
import com.enigmadux.titandescent2.layouts.stats.XPBackground;

public class InGameOverlay extends Layout {


    private PauseButton pauseButton;
    private RevertToCheckpoint revertToCheckpoint;
    private Stats stats;
    private PlatformHelper platformHelper;
    private World world;
    public InGameOverlay(){

    }

    @Override
    public void setUpChildren() {
        world = guiData.getLayout(World.class);

        pauseButton = new PauseButton();
        revertToCheckpoint = new RevertToCheckpoint();
        this.stats = new Stats();
        this.platformHelper = new PlatformHelper();

        this.addChild(pauseButton);
        this.addChild(revertToCheckpoint);
        this.addChild(stats);
        this.addChild(platformHelper);


        this.clickables.add(revertToCheckpoint);
        this.clickables.add(pauseButton);
    }




    @Override
    public void bufferChildren() {
        this.pauseButton.bufferChildren();
        this.revertToCheckpoint.bufferChildren();
        this.stats.bufferChildren();
        this.platformHelper.bufferChildren();
    }

    @Override
    public void onShow() {
        super.onShow();
        if (stats != null){
            this.stats.update();
        }
    }

    public void update(){
        this.stats.update();
        this.platformHelper.update(world);
    }
}
