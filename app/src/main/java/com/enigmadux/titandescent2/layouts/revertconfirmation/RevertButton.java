package com.enigmadux.titandescent2.layouts.revertconfirmation;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.util.MathOps;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class RevertButton extends Button {
    static final float RESYNC_DIST = 40;
    private static final float WIDTH = 0.8f;
    private TextMesh suhHeader;
    public RevertButton( float x, float y) {
        super(null, R.drawable.checkpoint, x, y, WIDTH * LayoutConsts.SCALE_X, WIDTH, 0);
        suhHeader = RendererAdapter.dynamicText.generateTextMesh("Revert to checkpoint",x,y-WIDTH * 0.5f,0.04f);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {
            World world = guiData.getLayout(World.class);

            Platform target = world.getShip().getLastCheckpoint();
            world.getShip().setCurrentPlatform(target);
            world.killStars();
            world.onNewPlatform();
            if (MathOps.sqrDist(world.getCamera().getCameraX() - world.getShip().getX(),world.getCamera().getCameraY() - world.getShip().getY()) > RESYNC_DIST * RESYNC_DIST){
                world.getCamera().reSync();
            }
            world.getBackendThread().setPaused(false);

            guiData.stackScene(World.class, InGameOverlay.class);
        }
    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);
        suhHeader.setData(parent,guiData);
    }

    @Override
    public void bufferChildren() {
        super.bufferChildren();
        RendererAdapter.dynamicText.buffer(suhHeader);
    }
}
