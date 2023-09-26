package com.enigmadux.titandescent2.guilib.dynamicText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.enigmadux.titandescent2.R;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import enigmadux2d.core.renderEngine.Renderer;
import enigmadux2d.core.shaders.TextShader;


/** Draws text on the spot rather than having to reload
 *
 * DEBUG MAJOR NOTICE
 * MAJOR NOTICE:::: ********||||||||||||||||||||||||||***************||||||||||||||*********
 * FONT CREATES SPACES AS " ", WHICH THE SCANNER SKIPS OVER, SO YOU MUST RENAME IT TOO &space; for it too work
 *
 * https://github.com/andryblack/fontbuilder/downloads
 * https://github.com/andryblack/fontbuilder/downloads
 * https://github.com/andryblack/fontbuilder/downloads
 * https://github.com/andryblack/fontbuilder/downloads
 * @author Manu Bhat
 * @version BETA
 *
 */
public class DynamicText {

    /** xml escape characters
     *
     */
    private static final String[] ESCAPE_CHARACTERS = new String[] {"&lt;", "&amp;", "&gt;", "&quot;","&apos;","space"};

    /** String counter parts
     *
     */
    private static final char[] ESCAPE_COUNTERPARTS = new char[] {'<','&','>','\"','\'',' '};

    /** Max length of a string (in characters)
     *
     */
    private static final int MAX_CHARACTER_LENGTH = 128;
    /** Max length of a string (in vertices) = num charactres
     *
     * for 1 its 4
     * for 2 its 6
     * so 2n + 2
     */
    private static final int MAX_VERTICES_LENGTH = 2*(MAX_CHARACTER_LENGTH + 1);


    /** Stores that actual atlas of all the characters
     *
     */
    private int[] texture = new int[1];

    /** The width of the texture in pixels
     *
     */
    private int w;
    /** THe height of the texture atlas in pixels
     *
     */
    private int h;

    /** Maps characters with the corresponding info
     *
     */
    private HashMap<Character,DynamicChar> characterData = new HashMap<>();

    /** Shader that helps with writing of the variables
     *
     */
    private TextShader shader;

    private Queue<TextMesh> renderQ = new LinkedList<>();

    private float[] parent = new float[16];
    private float[] buffer = new float[16];

    /** The max joystick_icon size is generated, and then mipmaps are used from there
     *
     * @param context a context object used to load resources
     * @param imagePointer a R.drawable.* that points to the texture atlas
     * @param characterDataPointer a R.raw.* that points to an xml file that tells about data of each character
     */
    public DynamicText(Context context, int imagePointer, int characterDataPointer){
        this.createAtlas(context,imagePointer);
        this.parseXMLCharacterData(context,characterDataPointer);

        this.shader = new TextShader(context, R.raw.text_vertex_shader,R.raw.text_frag_shader);

        Matrix.setIdentityM(parent,0);

    }

    /** Creates the texture atlas
     *
     * @param context a context object used to load resources
     * @param imagePointer a R.drawable.* that points to the texture atlas
     */
    private void createAtlas(Context context, int imagePointer){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPremultiplied = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),imagePointer,options);
        bitmap.setHasAlpha(true);

        this.w = bitmap.getWidth();
        this.h = bitmap.getHeight();

        bitmap = Bitmap.createBitmap(bitmap);


        GLES30.glGenTextures(1, this.texture, 0);

        //now bind it with the array
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, this.texture[0]);

        // create nearest filtered texture
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);


        // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);


        //bitmap no longer needed
        bitmap.recycle();

    }

    /** Parses character data
     *
     * @param context a context object used to load resources
     * @param characterDataPointer a R.raw.* that points to an xml file that tells about data of each character
     */
    private void parseXMLCharacterData(Context context, int characterDataPointer) {
        Scanner characterData = new Scanner(context.getResources().openRawResource(characterDataPointer));

        //first line is xml stuff no use
        characterData.nextLine();
        //second line we need to get the font size
        String firstData = characterData.nextLine();
        firstData = firstData.replace('\"',' ');

        Scanner internalScanner = new Scanner(firstData);
        //get rid of "Font", and "size = ";
        internalScanner.next();
        internalScanner.next();

        int fontSize = internalScanner.nextInt();

        while (! internalScanner.hasNextInt()){
            internalScanner.next();
        }

        int fontHeight = internalScanner.nextInt();

        internalScanner.close();

        while (characterData.hasNextLine()){
            String data = characterData.nextLine();

            //it's the end of the file
            if (!characterData.hasNextLine()) break;

            //replace the quotes to make scanning easier
            data = data.replace('\"',' ');
            internalScanner = new Scanner(data);

            //get rid of <Char, and "width = "
            internalScanner.next();
            internalScanner.next();
            float xTravel = internalScanner.nextInt();

            //get rid of offset=
            internalScanner.next();
            float offsetX = internalScanner.nextInt();
            float offsetY = internalScanner.nextInt();

            //get rid of "rect ="
            internalScanner.next();

            //the amount from the left edge
            float rectX = internalScanner.nextInt();
            //the amount from top
            float rectY = internalScanner.nextInt();
            //the width
            float rectW = internalScanner.nextInt();
            //the height
            float rectH = internalScanner.nextInt();

            //get rid of "code = "
            internalScanner.next();

            //the code, but will be converted to a character pretty soon
            String code = internalScanner.next();
            char charCode = (char) -1;

            if (code.length() == 1){
                charCode = code.charAt(0);
            } else {
                for (int i = 0; i < ESCAPE_CHARACTERS.length; i++) {
                    if (code.equals(ESCAPE_CHARACTERS[i])) {
                        charCode = ESCAPE_COUNTERPARTS[i];
                        break;
                    }
                }
            }



            internalScanner.close();


            this.characterData.put(charCode,new DynamicChar(
                    xTravel/fontSize,
                    offsetX/fontSize,
                    (offsetY - fontHeight)/fontSize,
                    rectX/this.w,
                    rectY/this.h,
                    rectW/this.w,
                    rectH/this.h,
                    fontSize,
                    this.w,
                    this.h
            ));




        }


        characterData.close();
    }

    private void parseFntCharacterData(Context context, int characterDataPointer) {

    }


    /** GEts the text mesh that should be used for rendering
     *
     * @param text the text that needs to be renderered (no escape characters) SUpported characters
     *              !"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{|}
     * @return a text mesh that contains the proper texture coordinates and vertices
     */
    public TextMesh generateTextMesh(String text,float dX,float dY,float fontSize){
        float[] positions = new float[4 * 3 * text.length()];//48
        float[] textureCords = new float[4 * 2 * text.length()];
        float offsetX = 0;


        for (int i = 0;i < text.length();i++){
            DynamicChar dynamicChar = this.characterData.get(text.charAt(i));
            if (dynamicChar == null) continue;

            textureCords[8*i] = dynamicChar.rectX;
            textureCords[8*i+1] = dynamicChar.rectY;
            textureCords[8*i+2] = dynamicChar.rectX;
            textureCords[8*i+3] = dynamicChar.rectY + dynamicChar.rectH;
            textureCords[8*i+4] = dynamicChar.rectX + dynamicChar.rectW;
            textureCords[8*i+5] = dynamicChar.rectY;
            textureCords[8*i+6] = dynamicChar.rectX + dynamicChar.rectW;
            textureCords[8*i+7] = dynamicChar.rectY + dynamicChar.rectH;

            float x = offsetX + dynamicChar.xOffset;
            float y = -dynamicChar.yOffset;
            float w = dynamicChar.rectFontSizeW;
            float h = dynamicChar.rectFontSizeH;

            //every third one we can leave 0
            positions[12*i] = x;
            positions[12*i + 1] = y;
            positions[12*i + 3] = x;
            positions[12*i + 4] = y - h;
            positions[12*i + 6] = x + w;
            positions[12*i + 7] = y;
            positions[12*i + 9] = x + w;
            positions[12*i + 10] = y - h;


            offsetX += dynamicChar.xTravel;

        }


        return new TextMesh(text,dX,dY,fontSize,positions,textureCords);
    }

    public void buffer(TextMesh textMesh){
        this.renderQ.add(textMesh);
    }
    public void render(){
        this.shader.useProgram();

        while (! renderQ.isEmpty()) {
            TextMesh textMesh = renderQ.poll();
            if (textMesh == null) continue;;

            this.shader.writeVertexData(textMesh.getVertices(), textMesh.getTextureCords());

            Matrix.multiplyMM(buffer,0,parent,0,textMesh.getInstanceTransform(),0);
            Renderer.setLayer(buffer,textMesh.getLayer());

            this.shader.writeMatrix(buffer);
            this.shader.writeTexture(this.texture[0]);
            this.shader.writeShader(textMesh.getShader());

            IntBuffer elementArray = textMesh.getElementArray();
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, elementArray.capacity(), GLES30.GL_UNSIGNED_INT, elementArray);

        }
        this.shader.finish();
    }



}
