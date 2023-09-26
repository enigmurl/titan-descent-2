package enigmadux2d.core.quadRendering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.SparseIntArray;

/** This is where the actual texture of the quad is stored. As well as instance specfic info
 *
 * @author Manu Bhat
 * @version BETA
 */
public class TextureLoader {


    /** Rather than having multiple quads with the same texture, this maps Android texture pointers ( R.drawable.x), to
     * openGL ones. This way we don't assign the duplicate memory
     *
     */
    private static SparseIntArray androidToGLTextureMap = new SparseIntArray();


    /** load an ANDROID texture pointer (R.drawable.*)
     *
     * @param context any context that can get resources
     * @param texturePointer the ANDROID pointer to the image
     */
    public static int[] loadAndroidTexturePointer(Context context,int texturePointer){
        int[] returnArr = new int[1];

        int indexOfPointer = TextureLoader.androidToGLTextureMap.indexOfKey(texturePointer);
        if (indexOfPointer < 0) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPremultiplied = false;
            Bitmap texture = BitmapFactory.decodeResource(context.getResources(),texturePointer,options);
            texture.setHasAlpha(true);


            texture = Bitmap.createBitmap(texture);


            //create a texture id at the specified location in the array
            GLES30.glGenTextures(1, returnArr, 0);

            //now bind it with the array
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, returnArr[0]);

            // create nearest filtered texture
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);


            // Use Android GLUtils to specify a two-dimensional texture image from our bitmap
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, texture, 0);


            //bitmap no longer needed
            texture.recycle();

            //add it to our banking
            TextureLoader.androidToGLTextureMap.put(texturePointer,returnArr[0]);

//            Log.d("Drawable","Generating Resource");

        } else {
            returnArr[0] = TextureLoader.androidToGLTextureMap.get(texturePointer);
        }

        return returnArr;
    }


    /** Deletes the sparse int array, in the event that textures need to be reloaded
     *
     */
    public static void resetTextures(){

        androidToGLTextureMap = new SparseIntArray();
    }


    public static void recycleAll(){
        int[] allTextures = new int[androidToGLTextureMap.size()];
        for (int i = 0;i < allTextures.length;i++) allTextures[i] = androidToGLTextureMap.valueAt(i);

        GLES30.glDeleteTextures(allTextures.length,allTextures,0);
    }

}
