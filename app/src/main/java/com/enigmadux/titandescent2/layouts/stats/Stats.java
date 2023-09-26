package com.enigmadux.titandescent2.layouts.stats;

import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.filestreams.CollectedStars;
import com.enigmadux.titandescent2.game.filestreams.Currency;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.values.LayoutConsts;

public class Stats extends Layout {

    private XPBackground xpBackground;
    private Text xp;
    private Text stars;
    private Text numSaves;

    private World world;
    public Stats() {
        float w = XPBackground.WIDTH * LayoutConsts.SCALE_X;
        float leftX = 1 - w;
        float h = XPBackground.HEIGHT;

        xpBackground = new XPBackground();
        xp = new Text(" 0",leftX + 0.6f * w,1 - h/4,0.04f);
        stars = new Text("0/0",leftX + 0.25f * w,1 - h/4,0.04f);
        numSaves = new Text(" 0 ",leftX + 0.9f * w,1-h/4,0.04f);

    }

    @Override
    public void setUpChildren() {
        this.addChild(xp);
        this.addChild(stars);
        this.addChild(numSaves);
        this.addChild(xpBackground);

        world = guiData.getLayout(World.class);

    }

    @Override
    public void bufferChildren() {
        xpBackground.bufferChildren();
        xp.bufferChildren();
        stars.bufferChildren();
        numSaves.bufferChildren();
    }

    public void update(){
        stars.updateText( CollectedStars.totalCollected() + "/"+ CollectedStars.getTotalStars());
        xp.updateText("" + Currency.getCurrency());
        numSaves.updateText("" + world.getNumFreeReverts());
    }
}
