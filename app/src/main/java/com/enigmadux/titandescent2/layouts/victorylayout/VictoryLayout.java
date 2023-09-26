package com.enigmadux.titandescent2.layouts.victorylayout;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.layouts.HomeButton;

public class VictoryLayout extends Layout {

    private TextMesh victory;
    private TextMesh subtext;
    private HomeButton homeButton;
    private ReplayButton replayButton;
    public VictoryLayout() {


    }


    @Override
    public void setUpChildren() {
        this.homeButton = new HomeButton(0,-0.65f,0.25f);
        this.victory = RendererAdapter.dynamicText.generateTextMesh("Congratulations! ",0,0.8f,0.125f);
        this.subtext = RendererAdapter.dynamicText.generateTextMesh("You beat Titan! Would you like to restart? ",0,0.5f,0.075f);
        this.replayButton = new ReplayButton(0,0);

        this.addChild(homeButton);
        this.addChild(subtext);
        this.addChild(replayButton);
        this.addChild(this.victory);
        this.clickables.add(homeButton);
        this.clickables.add(replayButton);
    }

    @Override
    public void bufferChildren() {
        this.homeButton.bufferChildren();
        this.replayButton.bufferChildren();
        this.victory.bufferChildren();
        this.subtext.bufferChildren();
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        super.onTouch(e);
        return true;
    }
}
