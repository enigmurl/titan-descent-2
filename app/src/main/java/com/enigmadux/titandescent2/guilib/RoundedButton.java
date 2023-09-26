package com.enigmadux.titandescent2.guilib;

import android.content.Context;
import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;

public class RoundedButton extends Button {

    private static final float DEFAULT_ROUNDNESS = 92f/512f;

    private static final int DEFAULT_BACKGROUND = R.drawable.layout_background;



    public RoundedButton(Context context,int pointer,float x,float y,float w,float h,float cornerSize){
        super(context,pointer,x,y,w,h,cornerSize);



    }
    public RoundedButton(Context context,float x,float y,float w,float h){
        super(context,DEFAULT_BACKGROUND,x,y,w,h,DEFAULT_ROUNDNESS);

        Matrix.translateM(instanceTransform,0,0,0,0.1f);
    }

}
