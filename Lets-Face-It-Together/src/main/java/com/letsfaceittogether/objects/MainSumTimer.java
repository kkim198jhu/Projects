package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MainSumTimer {
    public float x;
    public float y;

    private Texture frameTexture;
    private boolean loaded = false;

    public MainSumTimer(float x, float y) {
        this.x = x;
        this.y = y;

        try {
            frameTexture = new Texture("assets/sprites/objects/Timer/Main Timer.png");
            frameTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loaded = true;
            System.out.println("Loaded main timer sprite.");
        } catch (GdxRuntimeException e) {
            loaded = false;
            System.out.println("Could not load main timer sprite.");
        }
    }

    public void draw(SpriteBatch batch, SpriteNumberRenderer numbers, int totalCentis) {
        if (loaded) {
            float drawW = 31f * 4f;
            float drawH = 20f * 4f;
            batch.draw(frameTexture, x, y, drawW, drawH);
            numbers.drawMainTime(batch, totalCentis, x, y, 4f);
        }
    }
}
