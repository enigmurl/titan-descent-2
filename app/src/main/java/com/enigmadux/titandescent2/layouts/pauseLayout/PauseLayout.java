package com.enigmadux.titandescent2.layouts.pauseLayout;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.guilib.Background;
import com.enigmadux.titandescent2.guilib.Button;
import com.enigmadux.titandescent2.guilib.Clickable;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.layouts.HomeButton;
import com.enigmadux.titandescent2.values.LayoutConsts;

import java.util.ArrayList;
import java.util.List;

public class PauseLayout extends Layout {

    private Background background;


    private HomeButton homeButton;

    private ResumeButton resumeButton;


    @Override
    public void setUpChildren() {
        background = new Background(0,0,1 * LayoutConsts.SCALE_X,1);
        resumeButton = new ResumeButton(0.125f,0,0.175f);
        this.homeButton = new HomeButton(-0.125f,0,0.175f);
        this.addChild(background);
        this.addChild(homeButton);
        this.addChild(resumeButton);

        this.clickables.add(homeButton);
        this.clickables.add(resumeButton);

    }

    @Override
    public void bufferChildren() {
        background.bufferChildren();
        this.homeButton.bufferChildren();
        this.resumeButton.bufferChildren();
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        super.onTouch(e);
        return true;
    }
}
