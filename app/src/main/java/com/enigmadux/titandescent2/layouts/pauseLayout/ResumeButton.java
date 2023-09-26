package com.enigmadux.titandescent2.layouts.pauseLayout;

import android.content.Context;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class ResumeButton extends Button {
    public ResumeButton(float x, float y, float r) {
        super(null, R.drawable.resume_button, x, y, r * 2 * LayoutConsts.SCALE_X,2 * r,0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {
            World world = guiData.getLayout(World.class);
            world.getBackendThread().setPaused(false);

            guiData.stackScene(World.class, InGameOverlay.class);
        }
    }

}
