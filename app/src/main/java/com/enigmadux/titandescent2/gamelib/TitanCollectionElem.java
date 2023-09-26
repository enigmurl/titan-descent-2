package com.enigmadux.titandescent2.gamelib;


import com.enigmadux.titandescent2.game.World;

import enigmadux2d.core.gameObjects.CollectionElem;

/** This is a collection element tailored to Titan Guardians. That is,
 * each instance contains 22 floats. 16 for the uMVPMatrix, 4 for the RGBA filter, and 2 for the texture coordinates offset.
 *
 * @author Manu Bhat
 * @version BETA
 */
public abstract class TitanCollectionElem extends CollectionElem {
    /** How much to translate the vertices to the right in openGL terms
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float x;
    /** How much to translate the vertices to the top in openGL terms
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float y;

    /** How much to scale the width, since it starts at 1, it just is the width.
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float w;
    /** How much to scale the height, since it starts at 1, it just is the height.
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float h;


    /** How much to translate the texture cords to the right in openGL terms
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float deltaTextureX;
    /** How much to translate the texture cords to the bottom in openGL terms
     * Remember that for texture cords, the y axis is flipped
     * This variable does nothing by itself, the child class must use it appropriately
     */
    protected float deltaTextureY;


    /** A shader that limits the R,G,B or A channels. They range from 0 to 1, where 1 means allow all of it,
     * and 0 means allow none of it
     */
    protected float[] shader = new float[]{1,1,1,1};

    protected float[] instanceTransform = new float[16];

    /** Default Constructor
     *
     * @param instanceID The id of this particular instance in the VaoCollection. It should be received using the VaoCollection.addInstance() method.
     */
    public TitanCollectionElem(int instanceID){
        super(instanceID);
    }


    /** Fully updates the instance
     *
     * dt is milliseconds
     * world hass al the information
     */
    public abstract void update(long dt, World world);

    /** Updates the instance info into the float[].
     *
     * @param blankInstanceInfo this is where the instance data should be written too. Rather than creating many arrays,
     *                          we can reuse the same one. Anyways, write all data to appropriate locations in this array,
     *                          which should match the format of the VaoCollection you are using
     * @param uMVPMatrix This is a the model view projection matrix. It performs all outside calculations, make sure to
     *                   not modify this matrix, as this will cause other instances to get modified in unexpected ways.
     *                   Rather use method calls like Matrix.translateM(blankInstanceInfo,0,uMVPMatrix,0,dX,dY,dZ), which
     *                   essentially leaves the uMVPMatrix unchanged, but the translated matrix is dumped into the blankInstanceInfo
     */
    public void updateInstanceInfo(float[] blankInstanceInfo,float[] uMVPMatrix){
        //first write the matrix from slots 0 to 15 (inclusive)
        this.updateInstanceTransform(blankInstanceInfo, uMVPMatrix);
        //now update the filter parameters
        blankInstanceInfo[16] = this.shader[0];
        blankInstanceInfo[17] = this.shader[1];
        blankInstanceInfo[18] = this.shader[2];
        blankInstanceInfo[19] = this.shader[3];
        //now update the delta texture cords
        blankInstanceInfo[20] = this.deltaTextureX;
        blankInstanceInfo[21] = this.deltaTextureY;
    }

    /** Gets the width variable. Note if the child classes does not use it appropriately, it amy not have any true effect
     *
     * @return the width variable
     */
    public float getW() {
        return this.w;
    }
    /** Gets the height variable. Note if the child classes does not use it appropriately, it amy not have any true effect
     *
     * @return the height variable
     */
    public float getH() {
        return this.h;
    }

    /** Gets the center position in openGL space
     *
     * @return the center x position distance from origin
     */
    public float getX() {
        return this.x;
    }

    /** Gets the center position in openGL space
     *
     * @return the center y position distance from origin
     */
    public float getY() {
        return this.y;
    }

    /** Sets the shader, which basically filters out specific channels of the texture.
     * 0 means none of that channel is shown (transparent), 1 is fully opaque
     *
     * @param r the filter of the red channel
     * @param g the filter of the green channel
     * @param b the filter of the blue channel
     * @param a the filter of the alpha channel
     */
    public void setShader(float r,float g,float b,float a){
        this.shader[0] = r;
        this.shader[1] = g;
        this.shader[2] = b;
        this.shader[3] = a;
    }

    public void resetShader(){
        this.shader = new float[]{1,1,1,1};
    }


    @Override
    public String toString() {
        return "ID : " + getInstanceID();
    }

}
