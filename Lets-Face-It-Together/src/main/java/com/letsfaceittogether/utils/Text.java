package com.letsfaceittogether.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Text {
    public static void draw(SpriteBatch batch, BitmapFont font, String text, float x, float y) {
        font.draw(batch, text, x, y);
    }
}
