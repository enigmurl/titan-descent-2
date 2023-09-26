package enigmadux2d.core.renderEngine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import enigmadux2d.core.renderEngine.renderables.Renderable;
import enigmadux2d.core.shaders.ShaderProgram;

public abstract class Renderer<T extends ShaderProgram,R extends Renderable> {

    protected static final float MAX_Z = 256;

    PriorityQueue<R> renderQ ;

    T shaderProgram;
    public Renderer(T shaderProgram){
        this.shaderProgram = shaderProgram;

        this.renderQ = new PriorityQueue<>(this.getTextureComparator());
    }

    protected Comparator<Renderable> getTextureComparator(){
        return new Comparator<Renderable>() {
            @Override
            public int compare(Renderable renderable, Renderable t1) {
                return (int) Math.copySign(1,renderable.getLayer() - t1.getLayer());
            }
        };
    }

    public void buffer(R r){
        renderQ.add(r);
    }

    @SafeVarargs
    public final void buffer(R... r){
        renderQ.addAll(Arrays.asList(r));
    }



    abstract void flush();

    public void render(){
        if (this.renderQ.isEmpty()){
            return;
        }

        shaderProgram.useProgram();
        this.flush();
        renderQ.clear();
    }

    public void recycle(){
        shaderProgram.recycle();
    }


    public static void setLayer(float[] buffer,float layer){
        buffer[2] = buffer[6] = buffer[10] = 0;
        buffer[14] = (MAX_Z-layer)/MAX_Z;
    }
}
