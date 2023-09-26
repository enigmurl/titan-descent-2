package com.enigmadux.titandescent2.guilib;

import android.view.MotionEvent;

import enigmadux2d.core.renderEngine.renderables.Renderable;

public interface Clickable extends Renderable {


    boolean onTouch(MotionEvent e);

}
