package com.enigmadux.titandescent2.gamelib;

import android.content.Context;
import android.util.Log;


import com.enigmadux.titandescent2.game.World;
import com.enigmadux.titandescent2.guilib.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import enigmadux2d.core.renderEngine.Renderer;
import enigmadux2d.core.renderEngine.renderables.Renderable;

/** Holds items and their vertex data
 *
 */
public class TitanCollection<T extends TitanCollectionElem> extends Node implements Iterable<T>,Renderable {

    /** Vertex data of the item per instance
     *
     */
    private TitanVaoCollection vertexData;

    /** Actual Data about the game object
     *
     */
    private ArrayList<T> instanceData ;

    //so that theres no garbage collection
    private final float[] bufferData = new float[22];



    /** Default constructor
     *
     * @param maxInstances maximum amount of instances
     * @param positions x1,y1,z1 ...
     * @param textureCords u1,v1...
     * @param indices indices
     */
    public TitanCollection(int maxInstances,float[] positions,float[] textureCords, int[] indices,float layer){
        super(0,0,0,0);
        this.vertexData = new TitanVaoCollection(maxInstances,positions,textureCords,indices,layer);

        instanceData = new ArrayList<>(maxInstances);

    }


    public void update(long dt, World world){


        for (int i = 0; i < instanceData.size(); i++) {
            T craterCollectionElem = instanceData.get(i);
            craterCollectionElem.update(dt, world);
            if (!instanceData.contains(craterCollectionElem)) {
                i--;
            }
        }
    }

    public void prepareFrame(){
        float[] mvpMatrix = parentLayout.getInstanceTransform();

        for (int i = 0, size = instanceData.size(); i < size; i++) {
            this.instanceData.get(i).updateInstanceInfo(bufferData, mvpMatrix);
            Renderer.setLayer(bufferData,this.vertexData.getLayer());

            this.vertexData.updateInstance(this.instanceData.get(i).getInstanceID(), bufferData);
        }


    }


    //texture pointer = R.drawable.*;
    public void loadTexture(Context context,int texturePointer){
        this.vertexData.loadTexture(context, texturePointer);
    }

    public void updateVBO(){
        this.vertexData.updateInstancedVbo();
    }

    public void clear(){
        this.vertexData.clearInstanceData();
        this.instanceData.clear();
    }

    public int size(){
        return this.instanceData.size();
    }

    //creates a spot in the vao for an instance
    public int createVertexInstance() {
        return this.vertexData.addInstance();
    }

    //adds an actual instance
    public void addInstance(T instance){
        this.instanceData.add(instance);
    }


    public void delete(T objectToDelete){
        this.vertexData.deleteInstance(objectToDelete.getInstanceID());
        this.instanceData.remove(objectToDelete);
    }

    public TitanVaoCollection getVertexData(){
        return this.vertexData;
    }
    public ArrayList<T> getInstanceData(){
        return this.instanceData;
    }

    @Override
    public Iterator<T> iterator(){
        return this.instanceData.iterator();
    }

    @Override
    public void bufferChildren() {
//        this.prepareFrame();
//        this.updateVBO();

        RendererAdapter.meshRenderer.buffer(this);
    }

    @Override
    public int getTextureID() {
        return vertexData.getTextureID();
    }

    @Override
    public float getLayer() {
        return vertexData.getLayer();
    }
}
