package com.enigmadux.titandescent2.gamelib;

import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.values.LayoutConsts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIData {


    public ArrayList<Layout> allLayouts;

    public List<Layout> currentLayouts;

    private final Object LOCK = new Object();

    public GUIData(Layout ... layouts){
        this.allLayouts = new ArrayList<>(Arrays.asList(layouts));

        this.currentLayouts = new ArrayList<>();

        for (Layout l: layouts){
            l.setData(null,this);
            l.setUpChildren();
            l.compileClickables();
        }
    }

    public void setScene(Class<? extends Layout> cls){
        try {
            synchronized (LOCK) {
                Layout l = this.getLayout(cls);

                boolean contains = false;
                for (int i = 0; i < currentLayouts.size(); i++) {
                    if (currentLayouts.get(i).getClass() != cls) {
                        currentLayouts.get(i).onHide();
                    } else {
                        contains = true;
                    }
                }
                currentLayouts = new ArrayList<>();
                currentLayouts.add(this.getLayout(cls));
                if (!contains) {
                    l.onShow();
                }
            }
        } catch (Exception e){
            //
        }


    }

    //the first scene is on the bottom, later scenes on top
    @SafeVarargs
    public final void stackScene(Class<? extends Layout>... cls){
        try {

            synchronized (LOCK) {
                List<Layout> oldLayouts = new ArrayList<>(currentLayouts);

                currentLayouts = new ArrayList<>();
                for (Class<? extends Layout> tmpcls : cls) {
                    Layout l = this.getLayout(tmpcls);
                    currentLayouts.add(l);
                }

                for (Layout l : currentLayouts) {
                    if (!oldLayouts.contains(l)) {
                        l.onShow();
                    }
                }

                for (Layout l : oldLayouts) {
                    if (!currentLayouts.contains(l)) {
                        l.onHide();
                    }
                }
//                Log.d("GUI","List: " + Arrays.toString(cls));
            }
        } catch (Exception e){
            //
        }


//        Log.d("GUI DATA:" ,"Stacking Scenes:" + Arrays.toString(cls));


    }

    public <T extends Layout> T getLayout(Class<T> cls){

        for (int i =0;i< allLayouts.size();i++){
            Layout l = allLayouts.get(i);
            if (cls.isInstance(l)){
                return cls.cast(l);
            }
        }
        //throw new IllegalArgumentException("No Scene of Class: " + cls + " Exists!");
        return null;
    }


    public void bufferRendering(){
        synchronized (LOCK) {
            for (int i = 0; i < currentLayouts.size(); i++) {
                currentLayouts.get(i).bufferChildren();
            }
        }
    }


    public void onTouch(MotionEvent e){
        synchronized (LOCK) {
            for (int i = currentLayouts.size() - 1; i >= 0; i--) {
                if (currentLayouts.get(i).onTouch(e)) {
                    return;
                }
            }
        }
    }

    public int getStackNum(Layout layout){
        synchronized (LOCK) {
            for (int i = 0; i < currentLayouts.size(); i++) {
                if (currentLayouts.get(i) == layout) {
                    return i;
                }
            }
            return -1;
        }
    }

    public boolean contains(Class<? extends Layout> cls){
        synchronized (LOCK) {
            for (int i = 0; i < currentLayouts.size(); i++) {
                if (currentLayouts.get(i).getClass().equals(cls)) {
                    return true;
                }
            }
            return false;
        }
    }


    public List<Layout> getCurrentLayouts() {
        return currentLayouts;
    }
}
