package enigmadux2d.core.renderEngine.renderables;

import enigmadux2d.core.renderEngine.renderables.Renderable;

public interface GUIRenderable extends Renderable {

    float getCornerSize();

    //this.w/this.h * LayoutConsts.SCREEN_W/LayoutConsts.SCREEN_H
    float getAspectRatio();

    float[] getShader();

    float getLayer();
}
