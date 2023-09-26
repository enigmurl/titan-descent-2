package enigmadux2d.core.renderEngine;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;

import com.enigmadux.titandescent2.R;

import java.util.Comparator;

import enigmadux2d.core.quadRendering.QuadMesh;
import enigmadux2d.core.renderEngine.renderables.QuadRenderable;
import enigmadux2d.core.renderEngine.renderables.Renderable;
import enigmadux2d.core.shaders.QuadShader;

public class QuadRenderer extends Renderer<QuadShader, QuadRenderable> {


    private final QuadMesh mesh;


    private float[] parent = new float[16];

    private float[] buffer = new float[16];


    public QuadRenderer(Context context) {
        super(new QuadShader(context, R.raw.quad_vertex_shader,R.raw.quad_frag_shader));
        //create the shared mesh
        this.mesh = new QuadMesh(new float[] {
                -0.5f,0.5f,0,
                -0.5f,-0.5f,0,
                0.5f,0.5f,0,
                0.5f,-0.5f,0
        });

        Matrix.setIdentityM(parent,0);

    }



    @Override
    void flush() {
        //bind the vao
        GLES30.glBindVertexArray(this.mesh.getVaoID());
        //enable the vertices
        GLES30.glEnableVertexAttribArray(QuadMesh.VERTEX_SLOT);


        int prevTexture = -12312312;

        while (! renderQ.isEmpty()){
            QuadRenderable r = renderQ.poll();
            if (r == null){
                continue;
            }

            Matrix.multiplyMM(buffer,0,parent,0,r.getInstanceTransform(),0);
            //set z to the layer
            //[ ...]
            //[...]
            //[0,0,0,layer]
            setLayer(buffer,r.getLayer());


            shaderProgram.writeMatrix(buffer);

            if (r.getTextureID() != prevTexture) {
                shaderProgram.writeTexture(r.getTextureID());
            }
            prevTexture = r.getTextureID();

            shaderProgram.writeShader(r.getShader());
            //write texture cord date
            shaderProgram.writeTextureCord(r.getTextureTransform());


            GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP,0,this.mesh.getVertexCount());

        }


        //vertices no longer needed
        GLES30.glDisableVertexAttribArray(QuadMesh.VERTEX_SLOT);
        //vao no longer needed
        GLES30.glBindVertexArray(0);
    }
}
