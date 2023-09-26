package com.enigmadux.titandescent2.game;

import android.content.Context;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.controls.CheckpointButton;
import com.enigmadux.titandescent2.game.controls.PlayButton;
import com.enigmadux.titandescent2.game.controls.ThrusterControls;
import com.enigmadux.titandescent2.game.filestreams.CheckPointPlatforms;
import com.enigmadux.titandescent2.game.filestreams.CollectedStars;
import com.enigmadux.titandescent2.game.filestreams.CompletedPlatforms;
import com.enigmadux.titandescent2.game.filestreams.Currency;
import com.enigmadux.titandescent2.game.filestreams.LastPlatform;
import com.enigmadux.titandescent2.game.objects.Background;
import com.enigmadux.titandescent2.game.objects.Cloud;
import com.enigmadux.titandescent2.game.objects.Exhaust;
import com.enigmadux.titandescent2.game.objects.FuelTank;
import com.enigmadux.titandescent2.game.objects.Platform;
import com.enigmadux.titandescent2.game.objects.Ship;
import com.enigmadux.titandescent2.game.objects.Star;
import com.enigmadux.titandescent2.gamelib.TitanCollection;
import com.enigmadux.titandescent2.guilib.Layout;
import com.enigmadux.titandescent2.guilib.Text;
import com.enigmadux.titandescent2.layouts.deathlayout.DeathLayout;
import com.enigmadux.titandescent2.layouts.inGameOverlay.InGameOverlay;
import com.enigmadux.titandescent2.layouts.stats.XPBackground;
import com.enigmadux.titandescent2.layouts.victorylayout.VictoryLayout;
import com.enigmadux.titandescent2.main.TitanActivity;
import com.enigmadux.titandescent2.main.TitanBackendThread;
import com.enigmadux.titandescent2.main.TitanRenderer;
import com.enigmadux.titandescent2.util.SoundLib;
import com.enigmadux.titandescent2.values.LayoutConsts;

/** Upper level class that contains data about the game state
 *
 * @author Manu Bhat
 * @version BETA
 */
public class World extends Layout {
    public static final float CAMERA_Z = 9;
    private static final int NUM_CHECKPOINTS_PER_AD = 16;

    private static final long WIFI_ERROR_MILLIS = 5000;

    //the vertices of a quad of size 1 by 1, centered around the origin
    private static final float[] QUAD_VERTICES = new float[] {
            -0.5f, 0.5f,0,
            -0.5f,-0.5f,0,
            0.5f,0.5f,0,
            0.5f,-0.5f,0,

    };
    //the texture coordinates of a quad of size 1 by 1 centered around the origin
    private static final float[] QUAD_TEXTURE_CORDS = new float[] {
            0,0,
            0,1,
            1,0,
            1,1
    };
    //the indices of which vertex to use for a quad
    private static final int[] QUAD_INDICES = new int[] {
            0,1,2,
            1,2,3
    };

    //file streams
    private CollectedStars collectedStars;
    private LastPlatform lastPlatform;
    private CheckPointPlatforms checkPointPlatforms;
    private CompletedPlatforms completedPlatforms;
    private Currency currency;

    private TitanBackendThread backendThread;
    //loaders
    private LevelLoader levelLoader = new LevelLoader();

    private Background background;

    private Camera camera;

    private Ship ship;

    private TitanCollection<FuelTank> fuelTanks;
    private TitanCollection<Star> stars;
    private TitanCollection<Platform> platforms;
    private TitanCollection<Cloud> clouds;
    private TitanCollection<Exhaust> thrust;

    //controls
    private ThrusterControls thrusterControls = new ThrusterControls();
    public PlayButton playButton;
    public CheckpointButton checkpointButton;
    private Text xpGain;

    private Text wifiError;
    private long millisWifiErrorLeft;

    private float[] cameraMatrix = new float[16];

    private float[] projMatrix = new float[16];

    private TitanActivity context;


    private InGameOverlay inGameOverlay;

    private boolean promptAd = true;

    private int numFreeReverts = 5;

    public World(TitanActivity context) {
        this.context = context;
        this.background = new Background();

        //controls
        this.playButton = new PlayButton(this);
        this.checkpointButton = new CheckpointButton(this);
        this.playButton.setCheckpointButton(checkpointButton);
        this.xpGain = new Text(" ",0,0,0.06f);
        this.xpGain.setShader(0,0,0,1);
        this.wifiError = new Text(" WiFi error loading ad. Restart game to re enable free \"Save Me\"",0,0.9f,0.03f);
        this.wifiError.setShader(0,0,0,1);

        this.initializeVAOS();

        this.initializeFileStreams();

        this.camera = new Camera(ship);

        Matrix.setIdentityM(projMatrix,0);
        Matrix.orthoM(projMatrix, 0, -CAMERA_Z, CAMERA_Z, -CAMERA_Z * LayoutConsts.SCREEN_HEIGHT / LayoutConsts.SCREEN_WIDTH, CAMERA_Z * LayoutConsts.SCREEN_HEIGHT / LayoutConsts.SCREEN_WIDTH, 0.2f, 5f);
    }

    private void initializeFileStreams(){
        collectedStars = new CollectedStars(context);
        collectedStars.loadCollectedStars();

        this.lastPlatform = new LastPlatform(context);
        lastPlatform.readLastPlatform();

        this.checkPointPlatforms = new CheckPointPlatforms(context);
        this.checkPointPlatforms.readCheckpoints();

        this.completedPlatforms = new CompletedPlatforms(context);
        this.completedPlatforms.readCompleted();

        this.currency = new Currency(context);
        this.currency.readCurrency();


    }
    private void initializeVAOS(){
        this.ship = new Ship(0,0,0.25f);

        fuelTanks = new TitanCollection<>(32,QUAD_VERTICES,QUAD_TEXTURE_CORDS,QUAD_INDICES,2f);
        fuelTanks.loadTexture(context, R.drawable.fuel_tank);

        stars = new TitanCollection<>(32,QUAD_VERTICES,QUAD_TEXTURE_CORDS,QUAD_INDICES,2f);
        stars.loadTexture(context,R.drawable.star);

        platforms = new TitanCollection<>(32,QUAD_VERTICES,QUAD_TEXTURE_CORDS,QUAD_INDICES,2f);
        platforms.loadTexture(context,R.drawable.platform);

        clouds = new TitanCollection<>(128,QUAD_VERTICES,QUAD_TEXTURE_CORDS,QUAD_INDICES,2f);
        clouds.loadTexture(context,R.drawable.cloud);

        thrust = new TitanCollection<>(256,QUAD_VERTICES,QUAD_TEXTURE_CORDS,QUAD_INDICES,2f);
        thrust.loadTexture(context,R.drawable.thrust);
    }

    private Platform getPlatformById(int id){
        for (int i = 0; i < platforms.size();i++){
            if (platforms.getInstanceData().get(i).getPlatformID() == id){
                return platforms.getInstanceData().get(i);
            }
        }
        return null;
    }

    public void loadLevel(){
        this.reset();

        levelLoader.loadLevel(this,context,R.raw.titan_world_data);
        for (Star s: stars){
            if (collectedStars.isCollected(s.getStarNumber())){
                s.permamentlyDisable();
            }
        }

        Platform p = null;
        Platform c = null;
        for (Platform platform: this.platforms){
            if (platform.getPlatformID() == LastPlatform.getLastPlatformId()){
                p = platform;
            }
            if (platform.getPlatformID() == LastPlatform.getLastCheckpointId()){
                c = platform;
            }

            if (platform.getPlatformID() == Platform.BASE_ID || platform.getPlatformID() == Platform.END_ID){
                checkPointPlatforms.addCheckpoint(platform.getPlatformID());
                checkPointPlatforms.writeCheckpoints();
            }

            if (checkPointPlatforms.isCheckpoint(platform.getPlatformID())){
                platform.setCheckpoint();

            }
            if (completedPlatforms.isCompleted(platform.getPlatformID())){
                platform.setCompleted();
            }
            platform.resetShader();



        }

        Exhaust.init(this);

        if (c != null){
            ship.setLastCheckpoint(c);
        }
        if (p != null){
            if (p.getPlatformID() == Platform.BASE_ID){
                this.numFreeReverts = NUM_CHECKPOINTS_PER_AD;
            }

            p.setCompleted();
            ship.setCurrentPlatform(p);
            camera.reSync();//locks
        }

    }


    public void setBackendThread(TitanBackendThread backendThread){
        this.backendThread = backendThread;
    }

    public void update(long dt){
        synchronized (TitanRenderer.LOCK) {
            this.ship.update(this, dt);
            this.camera.update(dt);
            this.thrust.update(dt, this);
        }

        this.fuelTanks.update(dt, this);
        this.platforms.update(dt, this);
        this.clouds.update(dt, this);
        this.stars.update(dt, this);


        inGameOverlay.update();

        this.millisWifiErrorLeft-= dt;
    }

    @Override
    public void setUpChildren() {
        this.addChild(background);

        this.addChild(fuelTanks);
        this.addChild(stars);
        this.addChild(platforms);
        this.addChild(clouds);
        this.addChild(thrust);

        this.addChild(ship);

        this.addChild(playButton);
        this.addChild(checkpointButton);
        this.addChild(xpGain);
        this.addChild(wifiError);

        //other scenes
        this.inGameOverlay = guiData.getLayout(InGameOverlay.class);
    }

    @Override
    public void bufferChildren() {
        this.background.bufferChildren();
        this.fuelTanks.bufferChildren();
        this.platforms.bufferChildren();
        this.clouds.bufferChildren();
        this.thrust.bufferChildren();
        this.stars.bufferChildren();
        this.ship.bufferChildren();

        this.playButton.bufferChildren();
        this.checkpointButton.bufferChildren();
        if (playButton.isVisible) {
            this.xpGain.bufferChildren();
        }
        if (millisWifiErrorLeft > 0){
            wifiError.bufferChildren();
        }


    }

    @Override
    public boolean onTouch(MotionEvent e) {

        //important that checkpoint is first bc it doesnt always return true
        return checkpointButton.onTouch(e) || playButton.onTouch(e) || thrusterControls.onTouch(e,this);
    }

    @Override
    public void onShow() {
        super.onShow();
        backendThread.setPaused(false);
        this.onNewPlatform();
    }


    @Override
    public void onHide() {
        super.onHide();
        backendThread.setPaused(true);
    }

    public TitanBackendThread getBackendThread(){
        return backendThread;
    }

    public Ship getShip(){
        return ship;
    }

    public TitanCollection<FuelTank> getFuelTanks(){
        return fuelTanks;
    }

    public TitanCollection<Star> getStars() {
        return stars;
    }

    public TitanCollection<Platform> getPlatforms() {
        return platforms;
    }

    public TitanCollection<Cloud> getClouds() {
        return clouds;
    }

    public TitanCollection<Exhaust> getThrust(){
        return thrust;
    }

    @Override
    public float[] getInstanceTransform() {
        return super.getInstanceTransform();
    }

    public void reset(){
        this.platforms.clear();
        this.fuelTanks.clear();
        this.stars.clear();
        this.clouds.clear();
        this.thrust.clear();

        this.getShip().reset();
        this.camera.reSync();
    }

    public void onPlayerDeath(){
        for (int i = 0; i < fuelTanks.size();i++){
            fuelTanks.getInstanceData().get(i).reEnable();
        }
        for (int i =0;i < stars.size();i++){
            stars.getInstanceData().get(i).onPlayerDeath();
        }
//        Log.d("World","DEATH: Setting checkpoint to " + (ship.getLastCheckpoint().getPlatformID()));
        this.xpGain.setCords(ship.getLastPlatform().getX()-2.5f, ship.getLastPlatform().getY() - 0.5f);
        this.xpGain.updateText(" ");



        if (ship.getLastPlatformAtDeath() != ship.getLastCheckpoint()){
            this.ship.setLeftThrusterState(false);
            this.ship.setRightThrusterState(false);
            SoundLib.updateLeftThrusterState(false);
            SoundLib.updateRightThrusterState(false);

            if (numFreeReverts <= 0 && promptAd && context.isLoaded()) {
                guiData.stackScene(World.class, InGameOverlay.class, DeathLayout.class);
            } else if (numFreeReverts > 0){
                revert();
            } else {
                if (! context.isLoaded() && promptAd){
                    this.showWifiError();
                }
                this.onNewPlatform();
            }
            if (ship.getLastPlatformAtDeath().getPlatformID() == Platform.BASE_ID){
                this.numFreeReverts = NUM_CHECKPOINTS_PER_AD;
            }
        } else {
            this.onNewPlatform();
        }
    }

    public void revert(){
        for (int i =0;i < stars.size();i++){
            stars.getInstanceData().get(i).onPlayerDeath();
        }

        Platform target = ship.getLastPlatformAtDeath();
        ship.setCurrentPlatform(target);

        onNewPlatform();
        backendThread.setPaused(false);

        guiData.stackScene(World.class, InGameOverlay.class);

        if (!target.isCheckpoint()) {
            this.numFreeReverts--;
        }
    }
    public void killStars(){
        for (int i =0;i < stars.size();i++){
            stars.getInstanceData().get(i).onPlayerDeath();
        }
    }


    public void onAdRewarded(){
        this.numFreeReverts = NUM_CHECKPOINTS_PER_AD;
    }

    public void onPlayerSurvive(){
        int xp = Currency.COMPLETION_CURRENCY;
        for (int i =0;i < stars.size();i++){
            stars.getInstanceData().get(i).onPlayerSurvive(this);
        }
        if (! ship.getLastPlatform().isCompleted()){
            xp *= 5f;
//            Log.d("Currency","Landed on a new platform");
        } else {
            xp *= Math.max(1,ship.getLastPlatform().getPlatformID())/Currency.PLATFORM_DIVISOR;
        }
        if (ship.getStabilizingMillis() < Currency.MAX_MILLIS_FOR_STABILIZATION){
            xp *= 1.5f;
            ship.getLastPlatform().perfectLanding();
//            Log.d("Currency","Landed PERFECTLY: " + ship.getStabilizingMillis());
        } else {
//            Log.d("Currency","Landed BADLY : " + ship.getStabilizingMillis());
        }
        float w = XPBackground.WIDTH * LayoutConsts.SCALE_X;
        float leftX = 1 - w;
        float h = XPBackground.HEIGHT;
        this.xpGain.setCords((leftX + w * 0.5f) ,  (1 - h * 2) );
        this.xpGain.updateText("+ " + xp + " XP");


        Currency.addCurrency(xp);
        currency.writeCurrency();


        collectedStars.updateCollectedStars();
//        Log.d("World","SURIVE: Setting checkpoint to " + (ship.getLastCheckpoint().getPlatformID()));
        LastPlatform.setLastPlatformId(ship.getLastPlatform().getPlatformID());
        LastPlatform.setLastCheckpointId(ship.getLastCheckpoint().getPlatformID());
        this.lastPlatform.writeLastPlatform();

        ship.getLastPlatform().setCompleted();
        this.completedPlatforms.addCompleted(ship.getLastPlatform().getPlatformID());
        this.completedPlatforms.writeCompleted();


        this.onNewPlatform();
    }

    public void onNewPlatform(){
        LastPlatform.setLastCheckpointId(ship.getLastCheckpoint().getPlatformID());
        LastPlatform.setLastPlatformId(ship.getLastPlatform().getPlatformID());
        this.lastPlatform.writeLastPlatform();

        for (int i = 0; i < fuelTanks.size();i++) {
            fuelTanks.getInstanceData().get(i).reEnable();
        }

        int nextId = ship.getLastPlatform().getPlatformID() + 1;
        Platform next = this.getPlatformById(nextId == platforms.size()-1 ? -1: nextId);
        if (next == null){
            next = ship.getLastPlatform();
        }
        this.ship.setNextPlatform(next);
        this.ship.setLeftThrusterState(false);
        this.ship.setRightThrusterState(false);
        SoundLib.updateLeftThrusterState(false);
        SoundLib.updateRightThrusterState(false);

        this.thrusterControls.disengage();


        if (ship.getLastPlatform().getPlatformID() == Platform.END_ID){
            if (! Thread.holdsLock(TitanRenderer.LOCK)){
                synchronized (TitanRenderer.LOCK){
                    guiData.stackScene(World.class, VictoryLayout.class);
                }
            } else {
                guiData.stackScene(World.class, VictoryLayout.class);
            }
        } else {

            this.checkpointButton.updateVisibility();
            this.checkpointButton.setCords(ship.getLastPlatform().getX() + CheckpointButton.X, ship.getLastPlatform().getY());
            this.playButton.isVisible = true;
            this.playButton.setCords(ship.getLastPlatform().getX() - CheckpointButton.X, ship.getLastPlatform().getY());

        }
    }


    public void checkPointLastPlatform(){
        Platform p = ship.getLastPlatform();
        p.setCheckpoint();
        ship.setLastCheckpoint(p);

        this.checkPointPlatforms.addCheckpoint(p.getPlatformID());
        this.checkPointPlatforms.writeCheckpoints();

    }

    public TitanActivity getContext() {
        return context;
    }

    public CollectedStars getCollectedStars(){
        return collectedStars;
    }

    public LastPlatform getLastPlatform() {
        return lastPlatform;
    }

    public ThrusterControls getThrusterControls() {
        return thrusterControls;
    }

    public CheckPointPlatforms getCheckPointPlatforms() {
        return checkPointPlatforms;
    }

    public CompletedPlatforms getCompletedPlatforms() {
        return completedPlatforms;
    }

    public int getNumFreeReverts() {
        return numFreeReverts;
    }

    public Currency getCurrency(){
        return currency;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setPromptAd(boolean promptAd) {
        this.promptAd = promptAd;
    }

    public void startRendering(){
        ship.startRendering();
        float cameraX = this.camera.getCameraX();
        float cameraY = this.camera.getCameraY();
        Matrix.setLookAtM(this.cameraMatrix, 0, cameraX, cameraY, 1, cameraX, cameraY, 0, 0, 1, 0);

        Matrix.multiplyMM(instanceTransform, 0, projMatrix, 0, cameraMatrix, 0);
        for (int i = 0;i < thrust.size();i++){
            thrust.getInstanceData().get(i).startRendering();
        }
    }

    public void showWifiError(){
        this.millisWifiErrorLeft = WIFI_ERROR_MILLIS;
    }
}
