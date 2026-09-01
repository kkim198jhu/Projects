package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class AnimatedLaser {
    private final float x;
    private final float y;
    private final int segments;
    private final float scale;

    private final float baseWidth = 36f;
    private final float baseHeight = 8f;

    private final Texture[] frames = new Texture[6];

    // 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1
    private static final int[] PING_PONG = {0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0};
    private static final long FRAME_DURATION_MS = 80;

    public AnimatedLaser(float x, float y, int segments, float scale) {
        this.x = x;
        this.y = y;
        this.segments = segments;
        this.scale = scale;

        for (int i = 0; i < 6; i++) {
            frames[i] = new Texture("assets/sprites/objects/Lazer/Lazer (Phase " + (i + 1) + ").png");
            frames[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        }
    }

    private int getCurrentFrameIndex() {
        long step = (TimeUtils.millis() / FRAME_DURATION_MS) % PING_PONG.length;
        return PING_PONG[(int) step];
    }

    public float getWidth() {
        return segments * baseWidth * scale;
    }

    public float getHeight() {
        return baseHeight * scale;
    }

    public boolean hits(Player player) {
        return Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, getWidth(), getHeight()
        );
    }

    public void draw(SpriteBatch batch) {
        Texture frame = frames[getCurrentFrameIndex()];

        float drawWidth = baseWidth * scale;
        float drawHeight = baseHeight * scale;

        for (int i = 0; i < segments; i++) {
            batch.draw(frame, x + i * drawWidth, y, drawWidth, drawHeight);
        }
    }
}
