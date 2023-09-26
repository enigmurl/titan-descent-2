package com.enigmadux.titandescent2.main;

import android.util.Log;
import android.view.MotionEvent;

import enigmadux2d.core.EnigmaduxGLSurfaceView;
import enigmadux2d.core.quadRendering.TextureLoader;

public class TitanGLSurfaceView extends EnigmaduxGLSurfaceView {

    //The renderer that does the actual drawing
    private TitanRenderer mRenderer;


    /** Default Constructor
     *
     * @param context Context used to reference resources, and other attributes of it. Any crater activity should work
     */
    public TitanGLSurfaceView(TitanActivity context) {
        super(context);

    }


    /** Not a usual setter method as it takes no parameters, it's meant to be called once by the parent class
     *
     */
    @Override
    public void setRenderer(){
        if (this.context instanceof TitanActivity) {
            mRenderer = new TitanRenderer((TitanActivity) this.context);
            setRenderer(mRenderer);
        }
    }


    /** Called every time there is a touch event
     *
     * @param e the MotionEvent describing how the user interacted with the screen
     * @return whether or not you are interested in the rest of that event (everything from ACTION_DOWN to ACTION_UP or ACTION_CANCEL) (true means interested, false means not, other views get to read the event)
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return mRenderer.onTouch(e);
    }


    /** Called whenever the app is paused
     *
     *  SUPER ON PAUSE IS NOT CALLED AS THIS DUMPS THE CONTEXT OUT OF MEMORY
     *
     */
    @Override
    public void onPause() {
        mRenderer.onPause();
    }


    /** Called whenever the app is resumed
     *
     *
     * SUPER ON RESUME IS NOT CALLED AS THIS DUMPS THE CONTEXT OUT OF MEMORY
     */
    @Override
    public void onResume() {
        mRenderer.onResume();
    }

    public void onStart(){
        mRenderer.onStart();
    }

    public void onStop(){
        mRenderer.onStop();
    }


    @Override
    public void onDestroy(){
//        Log.d("MEMORY","STARTING CLEARING");
        this.queueEvent(new Runnable() {
            @Override
            public void run() {
                try {
                    TextureLoader.recycleAll();
                    TextureLoader.resetTextures();
//                Log.d("MEMORY","CLEARING: " + Thread.currentThread());
                } catch (Exception e){
//                    Log.d("Exception"," GL SURFACE VIEW Failed",e);
                }
            }
        });
    }
}
