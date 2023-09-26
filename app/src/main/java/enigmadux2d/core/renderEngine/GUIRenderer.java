package enigmadux2d.core.renderEngine;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;
import enigmadux2d.core.renderEngine.renderables.GUIRenderable;

import enigmadux2d.core.quadRendering.GuiMesh;
import enigmadux2d.core.quadRendering.QuadMesh;
import enigmadux2d.core.shaders.GUIShader;

public class GUIRenderer extends Renderer<GUIShader,GUIRenderable> {


    private final GuiMesh mesh;


    private float[] parent = new float[16];

    private float[] buffer = new float[16];


    public GUIRenderer(Context context) {
        super(new GUIShader(context, R.raw.gui_vertex_shader,R.raw.gui_fragment_shader));
        //create the shared mesh
        this.mesh = new GuiMesh(new float[] {
                -0.5f,0.5f,0,
                -1/6f,0.5f,0,
                +1/6f,0.5f,0,
                0.5f,0.5f,0,

                -0.5f,1/6f,0,
                -1/6f,1/6f,0,
                +1/6f,1/6f,0,
                0.5f,1/6f,0,

                -0.5f,-1/6f,0,
                -1/6f,-1/6f,0,
                +1/6f,-1/6f,0,
                0.5f,-1/6f,0,

                -0.5f,-0.5f,0,
                -1/6f,-0.5f,0,
                +1/6f,-0.5f,0,
                0.5f,-0.5f,0,
        });

        Matrix.setIdentityM(parent,0);
        Matrix.orthoM(parent,0,-1,1,-1,1,0.05f,1);

    }

    @Override
    void flush() {
        //bind the vao
        GLES30.glBindVertexArray(this.mesh.getVaoID());
        //enable the vertices
        GLES30.glEnableVertexAttribArray(QuadMesh.VERTEX_SLOT);


        int prevTexture = -1231231;

        while (! renderQ.isEmpty()){
            GUIRenderable r = renderQ.poll();
            if (r == null){
                continue;
            }


            Matrix.multiplyMM(buffer,0,parent,0,r.getInstanceTransform(),0);
            //set z to the layer
            //[ ...]
            //[...]
            //[0,0,0,layer]
            this.setLayer(buffer,r.getLayer());


            shaderProgram.writeMatrix(buffer);

            if (r.getTextureID() != prevTexture) {
                shaderProgram.writeTexture(r.getTextureID());
            }

            prevTexture = r.getTextureID();

            shaderProgram.writeCornerInfo(r.getCornerSize(),r.getAspectRatio());

            shaderProgram.writeShader(r.getShader());

            GLES30.glDrawElements(GLES30.GL_TRIANGLES,this.mesh.indices.capacity(),GLES30.GL_UNSIGNED_INT,this.mesh.indices);
        }


        //vertices no longer needed
        GLES30.glDisableVertexAttribArray(QuadMesh.VERTEX_SLOT);
        //vao no longer needed
        GLES30.glBindVertexArray(0);
    }
}
