package com.enigmadux.titandescent2.guilib;

import android.opengl.Matrix;
import android.util.Log;

import com.enigmadux.titandescent2.gamelib.GUIData;
import com.enigmadux.titandescent2.gamelib.RendererAdapter;
import com.enigmadux.titandescent2.guilib.dynamicText.DynamicText;
import com.enigmadux.titandescent2.guilib.dynamicText.TextMesh;
import com.enigmadux.titandescent2.values.LayoutConsts;

import java.util.Arrays;

/** Any class that wants to be a clickable must extend this class.
 *
 *
 *
 * COMMON DEBUGS:
 * make sure the clickable is viewable (isVisible = true), this is crucial
 * isDown must be handled by the sub class, as in when it's pressed it must become true,
 *
 */
public class Text extends Node {





    private TextMesh textMesh;
    private float x;
    private float y;
    private float fontSize;

    private boolean moved = false;

    private float[] shader = new float[] {1,1,1,1};
    public Text(String text,float x, float y, float fontSize) {
        super(0,0,1,1);
        textMesh = RendererAdapter.dynamicText.generateTextMesh(text, x, y, fontSize);

        this.x = x;
        this.y = y;
        this.fontSize = fontSize;
    }

    @Override
    public void setData(Node parent, GUIData guiData) {
        super.setData(parent, guiData);

        textMesh.setData(parent,guiData);
    }

    @Override
    public void bufferChildren() {
        RendererAdapter.dynamicText.buffer(textMesh);
    }

    @Override
    public float getLayer() {
        return textMesh.getLayer();
    }

    public void updateText(String text){
        if (! text.equals(textMesh.getActualText()) || moved) {
            textMesh = RendererAdapter.dynamicText.generateTextMesh(text, x, y, fontSize);
            textMesh.setData(parentLayout, guiData);
            moved = false;
            textMesh.setShader(shader[0],shader[1],shader[2],shader[3]);
        }
    }

    public void setCords(float x,float y){
        this.x = x;
        this.y = y;
        this.recalculateTransform();
        moved = true;
    }

    public TextMesh getTextMesh() {
        return textMesh;
    }

    public void setShader(float r,float g,float b,float a) {
        this.shader[0] = r;
        this.shader[1] = g;
        this.shader[2] = b;
        this.shader[3] = a;

        textMesh.setShader(r, g, b, a);
    }
}


