package com.enigmadux.titandescent2.values;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/** Holds lots of colors, strings, and other constants related to the layout, that multiple classes may refer to
 *
 * @author Manu Bhat
 * @version BETA
 */
public class LayoutConsts {
    /** Serves as the color of the majority of the text, the color is a sort of brown
     *
     */
    public static final int CRATER_TEXT_COLOR = 0xFFFFFFAA;// 0xFF99462D;

    /** Serves as the color of the majority of the text, in float[] form
     *
     */
    public static final float[] CRATER_FLOAT_TEXT_COLOR = new float[] {1,1,0.664f,1};


    /** Serves as the color of the level buttons, the color is black
     *
     */
    public static final float[] LEVEL_FLOAT_TEXT_COLOR = new float[] {1,1,0.664f,1};// 0xFF99462D;

    /** The width of the screen in pixels, it is set by the Renderer
     *
     */
    public static int SCREEN_WIDTH = -1;
    /** The height of the screen in pixels, it is set by the Renderer
     *
     */
    public static int SCREEN_HEIGHT = -1;


    public static float SCALE_X = -1;
    public static float SCALE_Y = -1;

    public static void init(Context context){
        final WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        LayoutConsts.SCREEN_WIDTH = displayMetrics.widthPixels + LayoutConsts.getNavigationBarHeight(context);
        LayoutConsts.SCREEN_HEIGHT = displayMetrics.heightPixels;

        LayoutConsts.SCALE_X = 1;
        LayoutConsts.SCALE_Y = 1;
        if (LayoutConsts.SCREEN_WIDTH > LayoutConsts.SCREEN_HEIGHT) {
            LayoutConsts.SCALE_X = (float) (LayoutConsts.SCREEN_HEIGHT) / (LayoutConsts.SCREEN_WIDTH);
        } else {
            LayoutConsts.SCALE_Y = (float) (LayoutConsts.SCREEN_WIDTH) / LayoutConsts.SCREEN_HEIGHT;

        }
    }

    /** Gets the action bar height, used for finding screen width
     *
     * @return the amount of pixels in the action bar
     */
    private static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return  context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
