package com.enigmadux.titandescent2.game.objects;

import android.opengl.Matrix;
import android.util.Log;

import com.enigmadux.titandescent2.R;
import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.game.filestreams.LastPlatform;
import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.gamelib.TitanCollection;
import com.enigmadux.titandescent2.guilib.Node;
import com.enigmadux.titandescent2.util.MathOps;
import com.enigmadux.titandescent2.util.SoundLib;

import enigmadux2d.core.quadRendering.TextureLoader;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;

public class Ship extends Node implements QuadRenderable {
    private static final float GRAVITY = 6.75f;
    private static final float MIN_Y = -500;

    private static final float ROTATION_ACCEPTANCE_DEGREES = 2;
    private static final float ROTATION_RESONANCE = 0.995f;

    private static final float MAX_CRASH_SPEED = 4;
    private static final float GLOW_EXPONENT = 2.25f;

    private static final float EPSILON = 0.001f;

    private static final float FUEL_PER_MILLI_PER_THRUSTER = 1/4375f;


    private static final float[] LEFT_BOTTOM_BUMPER = new float[] {-0.3333f,-0.5f};
    private static final float[] RIGHT_BOTTOM_BUMPER = new float[] {- LEFT_BOTTOM_BUMPER[0],LEFT_BOTTOM_BUMPER[1]};

    private static final float[] THRUSTER_SIZE = new float[] {(Thruster.LEFT_THRUSTER_OFFSET[1] + 0.5f)/ (0.5f - Thruster.ANCHOR[0]),(Thruster.LEFT_THRUSTER_OFFSET[1] + 0.5f)/ (0.5f - Thruster.ANCHOR[0]) * 90f/300f};

    //degrees
    private float rotationSpeed;
    private float rotation;

    private float vX,vY;

    private int id;


    private float[] shader = new float[] {1,1,1,1};

    private float[] textureTransform = new float[] {0,0,1,1};

    //0 to 1
    private float fuel;

    private Thruster leftThruster;
    private Thruster rightThruster;
    private FuelIndicator fuelIndicator;
    private RedGlow speedIndicator;


    private Platform lastPlatform = null;
    private Platform lastPlatformAtDeath = null;
    private Platform lastCheckpoint = null;
    private Platform nextPlatform = null;
    private boolean hasLeftLastPlatform = false;

    //amount of millis it takes to fully stabilize
    private long stabilizingMillis = 0;
    private Platform lockedPlatform = null;

    private float renderX;
    private float renderY;
    private float renderR;

    public Ship(float x,float y,float r){
        super(x, y, 2 * r, 2 * r);
        this.id = TextureLoader.loadAndroidTexturePointer(null, R.drawable.ship_body)[0];
    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);

        leftThruster = new Thruster(this,Thruster.LEFT_THRUSTER_OFFSET[0],Thruster.LEFT_THRUSTER_OFFSET[1],THRUSTER_SIZE[0],THRUSTER_SIZE[1]);
        rightThruster = new Thruster(this,Thruster.RIGHT_THRUSTER_OFFSET[0],Thruster.RIGHT_THRUSTER_OFFSET[1],THRUSTER_SIZE[0],THRUSTER_SIZE[1]);
        fuelIndicator = new FuelIndicator(0,-0.1f,0.3f,0.5f);
        speedIndicator = new RedGlow(this,-0.2f,0,2.25f,1.75f);

        leftThruster.setData(this,guiData);
        rightThruster.setData(this,guiData);
        fuelIndicator.setData(this,guiData);
        speedIndicator.setData(this,guiData);

        float down = 270;

        this.setLeftThrusterAngle(down);
        this.setRightThrusterAngle(down);
    }

    public void update(World world,long dt){
        SoundLib.updateLeftThrusterState(leftThruster.isEnabled() && fuel > 0);
        SoundLib.updateRightThrusterState(rightThruster.isEnabled() && fuel > 0);
        if (this.y < MIN_Y){
            this.death(world);
            world.getCamera().reSync();
        }

        this.platformCheck(world, dt);
        fuelIndicator.updateFuel(this.fuel);

        speedIndicator.setAlpha(MathOps.clip((float) Math.pow((vX * vX + vY * vY)/(MAX_CRASH_SPEED* MAX_CRASH_SPEED),GLOW_EXPONENT),0,1));

        if (! hasLeftLastPlatform){
            return;
        }
        if (leftThruster.isEnabled() && fuel > 0){
            this.fuel -= FUEL_PER_MILLI_PER_THRUSTER * dt;
        }
        if (rightThruster.isEnabled() && fuel > 0){
            this.fuel -= FUEL_PER_MILLI_PER_THRUSTER * dt;
        }


        leftThruster.update(world,dt);
        rightThruster.update(world, dt);
        this.vY -= GRAVITY * dt/1000;

        this.y += vY * dt/1000;
        this.x += vX * dt/1000;



        this.rotation += rotationSpeed * dt/1000;

        this.rotationSpeed *= ROTATION_RESONANCE;
    }

    private void platformCheck(World world,long dt){
        if (! hasLeftLastPlatform && lastPlatform != null){
            this.rotationSpeed = rotation = 0;

            this.vY = 0;
            this.vX = 0;
            return;
        }



        float radians = (float) Math.toRadians(rotation);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        float x0 = LEFT_BOTTOM_BUMPER[0] * w;
        float y0 = LEFT_BOTTOM_BUMPER[1] * h;

        float x1 = RIGHT_BOTTOM_BUMPER[0] * w;
        float y1 = RIGHT_BOTTOM_BUMPER[1] * h;

        float rotX0 = cos * x0 - sin * y0 + x;
        float rotY0 = cos * y0 + sin * x0 + y;

        float rotX1 = cos * x1 - sin * y1 + x;
        float rotY1 = cos * y1 + sin * x1 + y;

        TitanCollection<Platform> platforms = world.getPlatforms();
        for (int i= 0;i < platforms.size();i++){
            Platform p = platforms.getInstanceData().get(i);
            if (p == this.lastPlatform && hasLeftLastPlatform){
                continue;
            }
            if (x > p.getX() - p.getW()/2 && x < p.getX() + p.getW()/2 && y > p.getY() + p.getH()/2) {
                boolean p1 = false;
                boolean p2 = false;
                if (MathOps.pointInRect(rotX0, rotY0,p.getX(),p.getY(),p.getW(),p.getH())){
                    if (vX * vX + vY * vY > MAX_CRASH_SPEED * MAX_CRASH_SPEED){
                        this.death(world);
                        SoundLib.playCrash();
                        return;
                    }
                    p1 = true;
                    //vXy - vYx, but vX is 0 so its just -vY * X
                    float torque = GRAVITY * Math.copySign(Math.abs(rotX0-x) + EPSILON,rotX0 - x) * w;
                    this.addTorque(torque);

                    this.vY = 0;
                    this.vX *= 0.99;
                }
                if (MathOps.pointInRect(rotX1, rotY1,p.getX(),p.getY(),p.getW(),p.getH())){
                    if (vX * vX + vY * vY > MAX_CRASH_SPEED * MAX_CRASH_SPEED){
                        this.death(world);
                        SoundLib.playCrash();
                        return;
                    }
                    p2 = true;
                    //vXy - vYx, but vX is 0 so its just -vY * X
                    float torque = GRAVITY * Math.copySign(Math.abs(rotX1-x) + EPSILON,rotX1 - x) * w;
                    this.addTorque(torque);

                    this.vY = 0;
                    this.vX *= 0.99;
                }
                if (p1 || p2){
                    this.stabilizingMillis += dt;
                    this.lockedPlatform = p;
                } else if (lockedPlatform == p)  {
                    this.stabilizingMillis = 0;
                    this.lockedPlatform = null;

                }

                if (p1 && p2 || ((p1 || p2) && Math.abs(rotation ) < ROTATION_ACCEPTANCE_DEGREES)){
                    this.setLastPlatform(p);
                    SoundLib.playGoodLanding();
                    world.onPlayerSurvive();
                    this.stabilizingMillis = 0;
                    return;
                } else if (! p1 && ! p2 && circleBodyIntersection(p.getX(),p.getY(),p.getW(),p.getH())){
                    this.death(world);
                    SoundLib.playCrash();
                    return;
                } else if (p1){
                    this.y += (p.getY() + p.getH()/2 - EPSILON) - rotY0;
                } else if (p2){
                    this.y += (p.getY() + p.getH()/2 - EPSILON) - rotY1;
                }
            } else if (circleBodyIntersection(p.getX(),p.getY(),p.getW(),p.getH())){
                this.death(world);
                SoundLib.playCrash();
                return;
            }
        }
    }


    @Override
    public int getTextureID() {
        return id;
    }

    @Override
    public float getLayer() {
        return parentLayout.getLayer()+1f;
    }

    public void bufferChildren(){
        leftThruster.bufferChildren();
        rightThruster.bufferChildren();
        fuelIndicator.bufferChildren();
        speedIndicator.bufferChildren();
        RendererAdapter.quadRenderer.buffer(this);


    }

    //basically sees if it intersects a circle OR if it intersects the bottom
    public boolean intersects(float x,float y,float w,float h){

        return circleBodyIntersection(x,y,w,h);
    }

    public boolean intersects(float x,float y,float r){
        float dist = r + this.w/2;
        return MathOps.sqrDist(this.x - x,this.y - y) < dist * dist;
    }

    private boolean circleBodyIntersection(float x, float y, float w, float h){
        float r = this.w/2 ;

        float circDistX = Math.abs(this.x - x);
        float circDistY = Math.abs(this.y - y);

        if (circDistX > (w/2 + r)) { return false; }
        if (circDistY > (h/2 + r)) { return false; }

        if (circDistX <= (w/2)) { return true; }
        if (circDistY <= (h/2)) { return true; }

        float dx = (circDistX - w/2);
        float dy = (circDistY - h/2);
        float cornerDistance_sq = dx * dx + dy * dy;

        return (cornerDistance_sq <= (r * r));
    }


    public void death(World world){
        if (this.lastPlatform != null) {
            this.lastPlatform.resetShader();
            this.lastPlatformAtDeath = lastPlatform;
        }
        this.fuel = 1;

        this.hasLeftLastPlatform = false;

        this.vX = vY = 0;

        this.rotation = rotationSpeed = 0;

        this.leftThruster.updateRotation(270);
        this.rightThruster.updateRotation(270);

        if (lastCheckpoint != null){
            this.x = lastCheckpoint.getX();
            this.y = lastCheckpoint.getY() + lastCheckpoint.getH()/2 + h/2;

            this.lastPlatform = this.lastCheckpoint;
        } else{
            this.x = 0;
            this.y = 0;
        }
        this.stabilizingMillis = 0;

        world.onPlayerDeath();
    }

    public void setFuel(float fuel){
        this.fuel = fuel;
    }

    @Override
    public float[] getTextureTransform() {
        return textureTransform;
    }

    @Override
    public float[] getShader() {
        return shader;
    }

    @Override
    public float[] getInstanceTransform() {
        Matrix.setIdentityM(instanceTransform,0);
        Matrix.translateM(instanceTransform,0,renderX,renderY,0);
        Matrix.rotateM(instanceTransform,0,renderR,0,0,1);
        Matrix.scaleM(instanceTransform,0,w,h,1);

        return super.getInstanceTransform();
    }



    private void setLastPlatform(Platform platform){
        this.lastPlatformAtDeath = platform;
        if (this.lastPlatform != null) {
            this.lastPlatform.resetShader();
        }
        if (platform.isCheckpoint()){
            this.lastCheckpoint = platform;
        }

        this.fuel = 1;
        this.lastPlatform = platform;
        this.hasLeftLastPlatform = false;
        this.vX = vY = 0;

        this.rotation = rotationSpeed = 0;

        this.leftThruster.updateRotation(270);
        this.rightThruster.updateRotation(270);
    }

    public void setLastCheckpoint(Platform platform){
        this.lastCheckpoint = platform;
    }


    public void setCurrentPlatform(Platform platform){
        this.hasLeftLastPlatform = false;
        if (this.lastPlatform != null) {
            this.lastPlatform.resetShader();
        }

        this.lastPlatform = platform;
        this.lastPlatformAtDeath = platform;
        if (platform.isCheckpoint()){
            this.lastCheckpoint = platform;
        }

        this.x = platform.getX();
        this.y = platform.getY() + platform.getH()/2 + this.getH()/2;

        this.fuel = 1;

        this.vX = vY = 0;

        this.rotation = rotationSpeed = 0;

        this.leftThruster.updateRotation(270);
        this.rightThruster.updateRotation(270);
    }



    //called on world reset (right when play is pressed)
    public void reset(){
        this.fuel = 1;

        this.x = y = 0;
        this.vX = vY = 0;

        this.hasLeftLastPlatform = false;
        this.lastPlatform = null;
        this.lastCheckpoint = null;
        this.nextPlatform = null;


        this.rotation = rotationSpeed = 0;

        this.leftThruster.updateRotation(270);
        this.rightThruster.updateRotation(270);
    }

    public void setLeftThrusterState(boolean state){
        leftThruster.setState(state);
    }
    public void setRightThrusterState(boolean state){
        rightThruster.setState(state);
    }

    public void leavePlatform(){
        if (! hasLeftLastPlatform){
            LastPlatform.setLastPlatformId(lastCheckpoint == null ? -1:lastCheckpoint.getPlatformID());
            LastPlatform.setLastCheckpointId(lastCheckpoint == null ? -1:lastCheckpoint.getPlatformID());
            ((World) parentLayout).getLastPlatform().writeLastPlatform();
        }
        hasLeftLastPlatform = true;
        if (lastPlatform != null){
            lastPlatform.setShader(1,1,1,0);
        }

    }

    public void setLeftThrusterAngle(float leftThrusterAngle) {
        if (! hasLeftLastPlatform){
            return;
        }
        leftThruster.updateRotation(leftThrusterAngle);
    }

    public void setRightThrusterAngle(float rightThrusterAngle) {
        if (! hasLeftLastPlatform){
            return;
        }
        rightThruster.updateRotation(rightThrusterAngle);
    }

    public void setNextPlatform(Platform nextPlatform) {
        this.nextPlatform = nextPlatform;
    }

    public void addRelativeForce(float vX, float vY){
        float radians = (float) Math.toRadians(rotation);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        this.vX += vX * cos - vY * sin;
        this.vY += cos * vY + vX * sin;
    }
    public float getFuel(){
        return fuel;
    }

    public boolean hasLeftLastPlatform() {
        return hasLeftLastPlatform;
    }

    public void addTorque(float rotationSpeed){
        this.rotationSpeed += rotationSpeed;
    }

    public float getvX() {
        return vX;
    }

    public float getvY() {
        return vY;
    }

    public float getRotation(){
        return rotation;
    }

    public Thruster getLeftThruster() {
        return leftThruster;
    }

    public Thruster getRightThruster() {
        return rightThruster;
    }

    public Platform getLastPlatform(){
        return lastPlatform;
    }

    public Platform getLastCheckpoint() {
        return lastCheckpoint;
    }

    public Platform getLastPlatformAtDeath() {
        return lastPlatformAtDeath;
    }

    public Platform getNextPlatform(){
        return nextPlatform;
    }

    public long getStabilizingMillis() {
        return stabilizingMillis;
    }

    public void startRendering(){
        this.renderX = x;
        this.renderY = y;
        this.renderR = rotation;

    }
}
