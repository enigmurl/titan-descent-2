package com.enigmadux.titandescent2.layouts.homescreenconfirmation;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.title.TitleLayout;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class ConfirmHome extends Button {
    private static final float WIDTH = 0.8f;

    public ConfirmHome(float x, float y) {
        super(null, R.drawable.home_button, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        World world = guiData.getLayout(World.class);

        guiData.setScene(TitleLayout.class);

        world.getBackendThread().setPaused(true);
    }
}
