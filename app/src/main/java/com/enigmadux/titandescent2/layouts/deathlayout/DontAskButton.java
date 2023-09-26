package com.enigmadux.titandescent2.layouts.deathlayout;

import android.content.Context;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.guilib.RoundedButton;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.revertconfirmation.ConfirmationLayout;
import com.enigmadux.titandescent2.main.TitanRenderer;

public class DontAskButton extends RoundedButton {

    private Text textMesh;
    public DontAskButton(String text,float x, float y, float w, float h) {
        super(null, x, y, w, h);
        this.textMesh = new Text(text,x,y + 0.04f,0.04f);
    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);
        textMesh.setData(this,guiData);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {
            World world = guiData.getLayout(World.class);
            world.setPromptAd(false);

            if (guiData.contains(ConfirmationLayout.class)) {
                guiData.stackScene(World.class, InGameOverlay.class, ConfirmationLayout.class);
            } else {


                Platform target = world.getShip().getLastCheckpoint();
                world.getShip().death(world);
                world.getShip().setCurrentPlatform(target);
                world.onNewPlatform();
                world.getCamera().reSync();
                world.getBackendThread().setPaused(false);

                guiData.stackScene(World.class, InGameOverlay.class);
            }
        }
    }

    @Override
    public void bufferChildren() {
        super.bufferChildren();
        textMesh.bufferChildren();
    }
}
