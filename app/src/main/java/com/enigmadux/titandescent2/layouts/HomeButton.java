package com.enigmadux.titandescent2.layouts;

import android.content.Context;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.homescreenconfirmation.HomeConfirmationLayout;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.title.TitleLayout;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class HomeButton  extends Button {

    //r in h
    public HomeButton(float x, float y, float r) {
        super(null, R.drawable.home_button, x, y, r * 2*    LayoutConsts.SCALE_X, r * 2,0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        World world = guiData.getLayout(World.class);

        synchronized (TitanRenderer.LOCK) {
            //if it has left platform
            if (world.getShip().hasLeftLastPlatform()) {

                guiData.stackScene(World.class, InGameOverlay.class, HomeConfirmationLayout.class);
            } else {

                guiData.setScene(TitleLayout.class);

                world.getBackendThread().setPaused(true);
            }
        }
    }
}
