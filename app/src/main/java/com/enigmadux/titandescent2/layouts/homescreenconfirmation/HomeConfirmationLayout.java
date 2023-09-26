package com.enigmadux.titandescent2.layouts.homescreenconfirmation;

import android.view.MotionEvent;

import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Background;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.values.LayoutConsts;


public class HomeConfirmationLayout extends Layout {

    private Background background;


    private ConfirmHome revertButton;

    private CancelButton cancelButton;


    private TextMesh title;

    @Override
    public void setUpChildren() {
        cancelButton = new CancelButton(-0.3f,-0.2f);
        this.revertButton = new ConfirmHome(0.3f,-0.2f);
        final float fontsize = 0.04f;
        this.title = RendererAdapter.dynamicText.generateTextMesh("Exiting will revert you to your last checkpoint. Continue?",0,0.5f,fontsize);
        this.title.setShader(0,0,0,1);


        background = new Background(0,0,this.title.getW() * LayoutConsts.SCALE_X * fontsize + 0.2f ,1.5f);



        this.addChild(background);
        this.addChild(revertButton);
        this.addChild(cancelButton);
        this.addChild(title);

        this.clickables.add(revertButton);
        this.clickables.add(cancelButton);

    }

    @Override
    public void bufferChildren() {
        background.bufferChildren();
        this.revertButton.bufferChildren();
        this.cancelButton.bufferChildren();
        this.title.bufferChildren();
    }

    @Override
    public boolean onTouch(MotionEvent e) {
        super.onTouch(e);
        return true;
    }
}
