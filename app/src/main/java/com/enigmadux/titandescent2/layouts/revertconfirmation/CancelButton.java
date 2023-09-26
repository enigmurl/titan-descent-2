package com.enigmadux.titandescent2.layouts.revertconfirmation;

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

public class CancelButton extends Button {
    static final float WIDTH = 0.8f;

    public CancelButton( float x, float y) {
        super(null, R.drawable.cancel, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
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
