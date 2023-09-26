package com.enigmadux.titandescent2.guilib;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import enigmadux2d.core.renderEngine.renderables.Renderable;

public abstract class Layout extends Node {

    protected List<Node> children = new ArrayList<>();

    protected List<Clickable> clickables = new ArrayList<>();

    public Layout(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public Layout(){
        this(0,0,1,1);
    }


    protected void addChild(Node child){
        this.children.add(child);

        child.setData(this,guiData);
        if (child instanceof Layout){
            ((Layout) child).setUpChildren();
        }
    }

    public float getLayer(){
        return  parentLayout == null ? guiData.getStackNum(this) * 16: parentLayout.getLayer() + 1;
    }

    public abstract void setUpChildren();

    public abstract void bufferChildren();

    public boolean onTouch(MotionEvent e){
        for (int i = 0;i<clickables.size();i++){
            if (clickables.get(i).onTouch(e)) return true;
        }
        return false;
    }

    public void compileClickables(){
        Collections.sort(clickables, new Comparator<Clickable>() {
            @Override
            public int compare(Clickable renderable, Clickable t1) {
                return -(int) Math.copySign(1,renderable.getLayer() - t1.getLayer());
            }
        });
        for (Node n: children){
            if (n instanceof Layout){
                ((Layout)n ).compileClickables();
            }
        }
    }

    public void onShow(){
    }

    public void onHide(){

    }



}
