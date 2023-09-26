package com.enigmadux.titandescent2.layouts.title;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.filestreams.Currency;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.RoundedButton;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.layouts.stats.Stats;
import com.enigmadux.titandescent2.main.TitanActivity;
import com.enigmadux.titandescent2.values.LayoutConsts;

import enigmadux2d.core.renderEngine.Renderer;

public class TitleLayout extends Layout {



    private TutorialButton tutorialButton;
    private PlayButton titanLevel;
    private PlayButton enceladus;
    private PlayButton europa;

    private TextMesh title;

    private Stats stats;

    private TitanActivity context;

    public TitleLayout(TitanActivity context) {
        this.context = context;

        float width = 0.5f;
        float height = width/LayoutConsts.SCALE_X;

        this.tutorialButton = new TutorialButton(context, R.drawable.tutorial_button,-0.75f,0,width,height,false);
        this.titanLevel = new PlayButton(context, R.drawable.titan_button,-0.25f,0,width,height,false);
        this.enceladus = new PlayButton(context,R.drawable.enceladus_button,0.25f,0,width,height,true);
        this.europa = new PlayButton(context,R.drawable.europa_button,0.75f,0,width,height,true);

    }


    @Override
    public void setUpChildren() {
        this.title = RendererAdapter.dynamicText.generateTextMesh("Titan Descent 2",-0.2f,0.8f,0.175f);
        this.title.setShader(154/255f,93/255f,28/255f,1);

        this.stats = new Stats();

        this.addChild(tutorialButton);
        this.addChild(titanLevel);
        this.addChild(enceladus);
        this.addChild(europa);
        this.addChild(title);
        this.addChild(stats);

        this.clickables.add(enceladus);
        this.clickables.add(europa);

        this.clickables.add(tutorialButton);
        this.clickables.add(titanLevel);
    }

    @Override
    public void bufferChildren() {
        this.tutorialButton.bufferChildren();
        this.titanLevel.bufferChildren();
        this.enceladus.bufferChildren();;
        this.europa.bufferChildren();
        this.title.bufferChildren();
        this.stats.bufferChildren();
    }

    @Override
    public void onShow() {
        super.onShow();
        if (stats != null){
            this.stats.update();
        }

        this.context.onTitleShown();
    }

    @Override
    public void onHide() {
        super.onHide();

        this.context.onTitleHide();
    }
}
