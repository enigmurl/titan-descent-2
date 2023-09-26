package com.enigmadux.titandescent2.layouts.revertconfirmation;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Background;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.values.LayoutConsts;


public class ConfirmationLayout extends Layout {

    private Background background;



    private TextMesh title;
    private RevertButton revertButton;
    private LastPlatformButton lastPlatform;
    private CancelButton cancelButton;


    @Override
    public void setUpChildren() {
        cancelButton = new CancelButton(-0.5f,-0.1f);
        this.revertButton = new RevertButton(0,-0.1f);
        this.lastPlatform = new LastPlatformButton(0.5f,-0.1f);

        final float fontsize = 0.04f;
        this.title = RendererAdapter.dynamicText.generateTextMesh("Revert Menu",0,0.5f,fontsize);
        this.title.setShader(0,0,0,1);
        background = new Background(0,0,1.0f +LayoutConsts.SCALE_X * CancelButton.WIDTH + 0.2f ,1.5f);

        this.addChild(lastPlatform);
        this.addChild(background);
        this.addChild(revertButton);
        this.addChild(cancelButton);
        this.addChild(title);

        this.clickables.add(revertButton);
        this.clickables.add(cancelButton);
        this.clickables.add(lastPlatform);



    }

    @Override
    public void bufferChildren() {
        background.bufferChildren();
        this.revertButton.bufferChildren();
        this.cancelButton.bufferChildren();
        this.lastPlatform.bufferChildren();;
        this.title.bufferChildren();
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        super.onTouch(e);
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();
    }
}
