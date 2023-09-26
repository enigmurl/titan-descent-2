package com.enigmadux.titandescent2.layouts.inGameOverlay;

import android.content.Context;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.layouts.pauseLayout.PauseLayout;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class PauseButton extends Button {
    public PauseButton(){
        super(null, R.drawable.pause_button,-1 + 0.2f * LayoutConsts.SCALE_X,0.8f,LayoutConsts.SCALE_X * 0.3f,0.3f,0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);
        synchronized (TitanRenderer.LOCK) {
            guiData.stackScene(World.class, InGameOverlay.class, PauseLayout.class);
            World world = guiData.getLayout(World.class);
            world.getBackendThread().setPaused(true);
        }
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer() + 14.5f;
    }
}
