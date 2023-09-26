package com.enigmadux.titandescent2.layouts.title;

import android.content.Context;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.RoundedButton;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.main.TitanRenderer;

//square buttno no rounding
public class PlayButton extends Button {
    private boolean disabled;
    public PlayButton(Context context,int pointer, float x, float y, float w, float h,boolean disabled) {
        super(context,pointer, x, y, w, h,0);
        this.disabled = disabled;
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        if (disabled){
            return false;
        }
        return super.onTouch(e);
    }

    @Override
    protected void onHardRelease(MotionEvent e) {
        super.onHardRelease(e);

        synchronized (TitanRenderer.LOCK) {

            World world = guiData.getLayout(World.class);
            world.loadLevel();

            guiData.stackScene(World.class, InGameOverlay.class);
        }


    }
}
