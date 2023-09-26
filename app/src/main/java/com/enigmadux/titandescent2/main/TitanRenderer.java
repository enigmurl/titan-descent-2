package com.enigmadux.titandescent2.main;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.gamelib.DrawablesLoader;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.layouts.deathlayout.DeathLayout;
import com.enigmadux.titandescent2.layouts.homescreenconfirmation.HomeConfirmationLayout;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.pauseLayout.PauseLayout;
import com.enigmadux.titandescent2.layouts.revertconfirmation.ConfirmationLayout;
import com.enigmadux.titandescent2.layouts.title.TitleLayout;
import com.enigmadux.titandescent2.layouts.tutorial.TutorialLayout;
import com.enigmadux.titandescent2.layouts.victorylayout.VictoryLayout;
import com.enigmadux.titandescent2.loading.Loader;
import com.enigmadux.titandescent2.util.SoundLib;
import com.enigmadux.titandescent2.values.LayoutConsts;

import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import enigmadux2d.core.EnigmaduxGLRenderer;
import enigmadux2d.core.renderEngine.LoadingRenderer;

public class TitanRenderer extends EnigmaduxGLRenderer {
    public static final Object LOCK = new Object();

    private TitanLoader loader;

    private GUIData guis;

    private TitanActivity titanActivity;

    private TitanBackendThread backendThread;

    private World world;

    private boolean loadingCompleted;


    //debug only
    private long lastMillis = System.currentTimeMillis();
    private int numFrames = 0;

    /**
     * Default constructor
     *
     * @param context A Context Object that is mainly of use to child class. Any non-null Context should work.
     */
    public TitanRenderer(TitanActivity context) {
        super(context);
        this.titanActivity = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        try {
            super.onSurfaceCreated(gl, config);

//        Log.d("FPS","RENDERING THREAD:" +  Thread.currentThread().getName());
            LayoutConsts.init(context);
            RendererAdapter.init(context);

            this.loader = new TitanLoader(RendererAdapter.loadingRenderer);
        } catch (Exception e){

        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!loadingCompleted){
            loader.load(context);
            RendererAdapter.render();
            return;
        } else {
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glClearColor(1,0.75f,0,1);

        try {
            this.guis.bufferRendering();

            long start = System.currentTimeMillis();
            if (System.currentTimeMillis() - start > 1000 / 120f) {
//                Log.d("FPS","Massive Synch Time: " + (System.currentTimeMillis()- start) + " Thread: " + t.getName() + " my thread: " + Thread.currentThread().getName());
            }

            if (guis.contains(World.class)) {
                synchronized (TitanRenderer.LOCK) {
                    world.startRendering();
                }
            }
            RendererAdapter.render();
        } catch (Exception e){
            //
        }

        //finaly render everything
        numFrames++;
        if (System.currentTimeMillis() - lastMillis > 10000){
//            Log.d("FRAME RATE","FPS: " + (1000 * numFrames/(System.currentTimeMillis() - lastMillis)));
            lastMillis = System.currentTimeMillis();
            numFrames = 0;
        }


    }

    public boolean onTouch(MotionEvent e){
        try {
            if (this.guis != null) {
                this.guis.onTouch(e);
            }
        } catch (Exception e1){
            //
        }
        return true;

    }

    public void onPause(){
        if (guis != null) {
            InGameOverlay  igo = guis.getLayout(InGameOverlay.class);
            List<Layout> currentLayouts = guis.getCurrentLayouts();
            if (this.backendThread != null) {
                synchronized (LOCK) {
                    if (currentLayouts.contains(world) && currentLayouts.contains(igo) && currentLayouts.size() == 2) {
                        guis.stackScene(World.class, InGameOverlay.class, PauseLayout.class);
                    }
                    this.backendThread.setPaused(true);
                }
            }
        }
    }

    public void onResume() {
    }

    public void onStart() {
        if (this.backendThread == null || ! this.backendThread.isAlive()){
            this.backendThread = new TitanBackendThread();
            if (world != null) {
                this.backendThread.setWorld(world);
            }
            this.backendThread.setPaused(true);
            this.backendThread.start();
        }
    }
    public void onStop() {
        if (this.backendThread != null){
            this.backendThread.endThread();
        }
    }

    public boolean loadStep(int step){
        if (step < 0) {
            return true;
        }

        switch (step){
            case 0:
                DrawablesLoader.reset();
                break;
            case 1:
                return DrawablesLoader.loadResource(context);
            case 2:
                try {
                    SoundLib.loadMedia(context);
                    SoundLib.setStateGameMusic(true);
                } catch (Exception e){
                    //
                }
                break;
            case 3:
                this.guis = new GUIData(new TitleLayout(titanActivity),
                        this.world = new World(titanActivity),
                        new InGameOverlay(),
                        new PauseLayout(),
                        new ConfirmationLayout(),
                        new DeathLayout(titanActivity),
                        new HomeConfirmationLayout(),
                        new VictoryLayout(),
                        new TutorialLayout()
                        );
                break;
            case 4:
                this.guis.setScene(TitleLayout.class);
                break;
            case 5:
                this.backendThread.setWorld(world);
                break;
            case 6:
                this.loadingCompleted = true;
                break;

        }
        return true;


    }

    private class TitanLoader extends Loader {
        //put some blank steps before so gl can keep the screen blank
        private int PADDING_STEPS = 120;
        //step number
        private int step = -PADDING_STEPS;

        public TitanLoader(LoadingRenderer loadingRenderer) {
            super(loadingRenderer);
        }

        @Override
        public boolean load(Context context) {
            super.load(context);
            if (loadStep(this.step)) {
                this.step++;
            }

            return true;
        }
    }
}
