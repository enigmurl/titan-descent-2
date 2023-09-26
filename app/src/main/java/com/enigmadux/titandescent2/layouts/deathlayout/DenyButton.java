package com.enigmadux.titandescent2.layouts.deathlayout;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.revertconfirmation.ConfirmationLayout;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class DenyButton extends Button {
    private static final float WIDTH = 0.6f;

    public DenyButton( float x, float y) {
        super(null, R.drawable.cancel, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {
            if (guiData.contains(ConfirmationLayout.class)) {
                guiData.stackScene(World.class, InGameOverlay.class, ConfirmationLayout.class);
            } else {
                revertToCheckpoint();
            }
        }
    }

    void revertToCheckpoint(){
        World world = guiData.getLayout(World.class);

        Platform target = world.getShip().getLastCheckpoint();
        world.getShip().death(world);
        world.getShip().setCurrentPlatform(target);
        world.onNewPlatform();
        world.getCamera().reSync();
        world.getBackendThread().setPaused(false);

        guiData.stackScene(World.class, InGameOverlay.class);
    }


}
