package com.enigmadux.titandescent2.layouts.inGameOverlay;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.revertconfirmation.ConfirmationLayout;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class RevertToCheckpoint extends Button {
    public RevertToCheckpoint(){
        super(null, R.drawable.revert,-1 + 0.6f * LayoutConsts.SCALE_X,0.8f,LayoutConsts.SCALE_X * 0.3f,0.3f,0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);
        synchronized (TitanRenderer.LOCK) {
            guiData.stackScene(World.class, InGameOverlay.class, ConfirmationLayout.class);

            World world = guiData.getLayout(World.class);
            world.getBackendThread().setPaused(true);
        }
    }
}
