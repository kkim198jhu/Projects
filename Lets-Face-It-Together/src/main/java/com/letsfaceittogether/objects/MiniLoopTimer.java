package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MiniLoopTimer {
    private static final float LOOP_SECONDS = 4.0f;

    public float x;
    public float y;

    private float currentSeconds = LOOP_SECONDS;
    private boolean stopped = false;

    private Texture frameTexture;
    private boolean loaded = false;

    public MiniLoopTimer(float x, float y) {
        this.x = x;
        this.y = y;

        try {
            frameTexture = new Texture("assets/sprites/objects/Timer/Mini Timer.png");
            frameTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loaded = true;
            System.out.println("Loaded mini timer sprite.");
        } catch (GdxRuntimeException e) {
            loaded = false;
            System.out.println("Could not load mini timer sprite.");
        }
    }

    public void update(float delta) {
        if (stopped) return;

        currentSeconds -= delta;
        while (currentSeconds < 0.0f) {
            currentSeconds += LOOP_SECONDS;
        }
    }

    public void stop() {
        stopped = true;
    }

    public void reset() {
        currentSeconds = LOOP_SECONDS;
        stopped = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public int getCentis() {
        return Math.round(currentSeconds * 100.0f);
    }

    public void draw(SpriteBatch batch, SpriteNumberRenderer numbers) {
        if (loaded) {
            float drawW = 15f * 4f;
            float drawH = 10f * 4f;
            batch.draw(frameTexture, x, y, drawW, drawH);
            numbers.drawMiniTime(batch, getCentis(), x, y, 4f);
        }
    }
}
