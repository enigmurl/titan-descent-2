package com.enigmadux.titandescent2.layouts.victorylayout;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class ReplayButton extends Button {
    private static final float WIDTH = 0.5f;

    public ReplayButton( float x, float y) {
        super(null, R.drawable.confirm, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {

            World world = guiData.getLayout(World.class);

            Platform target = world.getPlatforms().getInstanceData().get(0);
            for (Platform p : world.getPlatforms()) {
                if (p.getPlatformID() == Platform.BASE_ID) {
                    target = p;
                    break;
                }
            }

            world.getShip().death(world);
            world.getShip().setCurrentPlatform(target);
            world.onNewPlatform();
            world.getCamera().reSync();
            world.getBackendThread().setPaused(false);

            guiData.stackScene(World.class, InGameOverlay.class);
        }
    }
}
