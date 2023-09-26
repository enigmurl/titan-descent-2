package com.enigmadux.titandescent2.gamelib;

import android.content.Context;

import com.enigmadux.titandescent2.R;

import enigmadux2d.core.quadRendering.TextureLoader;

public class DrawablesLoader {
    private static final int[] RESOURCES = new int[]{
            //IN GAME
            R.drawable.ship_body,
            R.drawable.fuel_tank,
            R.drawable.thruster,
            R.drawable.red_heat,
            R.drawable.titanbackground,
            R.drawable.mountains1,
            R.drawable.mountains2,

            //GUI BUTTONS
            R.drawable.titan_button,
            R.drawable.enceladus_button,
            R.drawable.europa_button,
            R.drawable.home_button,
            R.drawable.pause_button,
            R.drawable.resume_button,
            R.drawable.cancel,
            R.drawable.revert,
            R.drawable.confirm,
            R.drawable.watch_button,
            R.drawable.checkpoint,
            //GUI LAYOUTs
            R.drawable.layout_background,
            R.drawable.xp_bar,
            R.drawable.platform_helper,
            //TUTORIAL
            R.drawable.overall_helper,
            R.drawable.fuel_helper,
            R.drawable.checkpoint_helper,
            R.drawable.thruster_background,
            R.drawable.thruster_circle,
            R.drawable.tutorial_pointer,

    };

    private static int resourceInd;

    //returns whether or not its finished
    public static boolean loadResource(Context context){
        if (resourceInd >= RESOURCES.length) return true;
//        Log.d("Drawable:","Loading: " + resourceInd);
        TextureLoader.loadAndroidTexturePointer(context,RESOURCES[resourceInd]);
        resourceInd++;
        return false;
    }

    public static void reset(){
        resourceInd = 0;
    }

}
