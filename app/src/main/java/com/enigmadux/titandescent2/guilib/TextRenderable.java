package com.enigmadux.titandescent2.guilib;

import com.enigmadux.titandescent2.guilib.dynamicText.DynamicText;

public interface TextRenderable {


    void setVisibility(boolean visibility);
    boolean isVisible();

    void renderText(DynamicText renderer, float[] parentMatrix);
    void updateText(String text, float fontSize);
    float getFontSize();
}
