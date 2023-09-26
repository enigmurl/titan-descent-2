package com.enigmadux.titandescent2.layouts.deathlayout;

import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Background;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.layouts.homescreenconfirmation.CancelButton;
import com.enigmadux.titandescent2.layouts.homescreenconfirmation.ConfirmHome;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.main.TitanActivity;
import com.enigmadux.titandescent2.values.LayoutConsts;


public class DeathLayout extends Layout {

    private Background background;


    private WatchButton watchAdButton;

    private DenyButton cancelButton;

    private DontAskButton dontAskButton;

    private TextMesh title;

    private TitanActivity titanActivity;

    private boolean finishedTouchEvent;

    public DeathLayout(TitanActivity titanActivity){
        this.titanActivity = titanActivity;
    }

    @Override
    public void setUpChildren() {


        cancelButton = new DenyButton(-0.2f,0f);
        this.watchAdButton = new WatchButton(titanActivity,this,0.2f,0f);
        final float fontsize = 0.04f;
        this.title = RendererAdapter.dynamicText.generateTextMesh("Refill \"Save Me\" by watching an ad?",0,0.5f,fontsize);
        this.title.setShader(0,0,0,1);
        this.dontAskButton = new DontAskButton("Don't ask for this session",0,-0.6f,0.75f,0.3f);


        background = new Background(0,0,this.title.getW() * LayoutConsts.SCALE_X * fontsize + 0.2f ,1.75f);



        this.addChild(background);
        this.addChild(watchAdButton);
        this.addChild(cancelButton);
        this.addChild(title);
        this.addChild(dontAskButton);

        this.clickables.add(watchAdButton);
        this.clickables.add(cancelButton);
        this.clickables.add(dontAskButton);

    }

    @Override
    public void bufferChildren() {
        background.bufferChildren();
        this.watchAdButton.bufferChildren();
        this.cancelButton.bufferChildren();
        this.title.bufferChildren();
        this.dontAskButton.bufferChildren();
    }


    @Override
    public boolean onTouch(MotionEvent e) {
        if (! finishedTouchEvent) {
            super.onTouch(e);
        }
        return true;
    }

    @Override
    public void onShow() {
        super.onShow();
        finishedTouchEvent = false;

    }

    void finishTouchEvent(){
        this.finishedTouchEvent = true;
    }
    void resetTouchEvent(){
        this.finishedTouchEvent = false;
    }
}
