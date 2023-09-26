package com.enigmadux.titandescent2.main;

import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.util.Log;

import com.enigmadux.titandescent2.game.World;

public class TitanBackendThread extends Thread {

    private static float FPS = 120;

    private boolean running = true;

    private boolean paused = false;

    private World world;


    public TitanBackendThread(){

    }

    public void setWorld(World world) {
        this.world = world;
        this.world.setBackendThread(this);
    }

    @Override
    public void run() {
        super.run();

        long lastLoop = System.currentTimeMillis();
        while (running){

            long currentTime = System.currentTimeMillis();

            if (! paused) {
                try {
                    world.update(System.currentTimeMillis() - lastLoop);

                } catch (Exception e){
//                    Log.d("Exception","Update Failed",e);
                }
            }
            lastLoop = currentTime;

            long delta = System.currentTimeMillis() - currentTime;
            if (delta < 1000/TitanBackendThread.FPS) {
                try {
                    Thread.sleep((long) (1000/TitanBackendThread.FPS) - delta);
                } catch (Exception e){
                    //pass
                }
            }

        }


    }

    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public void endThread(){
        this.running =false;
    }
}
