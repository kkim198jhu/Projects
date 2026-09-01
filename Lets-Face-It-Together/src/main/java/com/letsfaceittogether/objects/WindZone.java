package com.letsfaceittogether.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Player;

public class WindZone {
    public float x;
    public float y;
    public float width;
    public float height;

    // -1 = blows left, +1 = blows right
    private float directionX;

    private float timeInWind = 0f;
    private float animationTimer = 0f;

    private boolean active = true;
    private boolean wasInsideTrackingZone = false;
    private boolean wasOnGround = false;

    private Texture[] windFrames;

    private float tileW = 90f;

    // This is the visible wind height.
    // Only this part pushes the player.
    private float visualHeight;

    public WindZone(float x, float y, float width, float height, float directionX) {
        this(x, y, width, height, directionX, height);
    }

    public WindZone(float x, float y, float width, float height, float directionX, float visualHeight) {
        this.x = x;
        this.y = y;
        this.width = width;

        // Tall invisible tracking zone.
        // The timer uses this so jumping does not reset wind buildup.
        this.height = height;

        this.directionX = directionX;

        // Visible wind strip.
        // The push uses only this smaller height.
        this.visualHeight = visualHeight;

        windFrames = new Texture[4];

        for (int i = 0; i < 4; i++) {
            String path = "assets/sprites/objects/Wind/Wind (Phase " + (i + 1) + ").png";

            if (Gdx.files.internal(path).exists()) {
                windFrames[i] = new Texture(path);
            } else {
                System.out.println("Missing wind sprite: " + path);
            }
        }
    }

    public void setActive(boolean active) {
        if (this.active && !active) {
            resetWindPower();
        }

        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    private void resetWindPower() {
        timeInWind = 0f;
        wasInsideTrackingZone = false;
    }

    public void update(float delta, Player player) {
        if (!active) {
            resetWindPower();
            wasOnGround = player.onGround;
            return;
        }

        animationTimer += delta;

        boolean insideTrackingZone = overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, height
        );

        boolean insideVisibleWind = overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, visualHeight
        );

        // Fully outside the fan's wind area:
        // no push and reset buildup.
        if (!insideTrackingZone) {
            timeInWind = 0f;
            wasInsideTrackingZone = false;
            wasOnGround = player.onGround;
            return;
        }

        // Just entered the fan's wind area: start weak.
        if (!wasInsideTrackingZone) {
            timeInWind = 0f;
        }

        // Landing/stepping on a block/floor inside the tracking zone resets strength.
        // Jumping does not reset it.
        boolean justLanded = player.onGround && !wasOnGround;
        if (justLanded) {
            timeInWind = 0f;
        }

        // Timer increases anywhere inside the tall tracking zone.
        // This prevents jumping above the visible strip from resetting the buildup.
        timeInWind += delta;

        // Only the visible wind strip physically pushes the player.
        if (insideVisibleWind) {
            float pushSpeed = calculatePushSpeed();

            player.x += directionX * pushSpeed * delta;

            if (player.x < 0) {
                player.x = 0;
            }

            player.previousX = player.x;
        }

        wasInsideTrackingZone = true;
        wasOnGround = player.onGround;
    }

    private float calculatePushSpeed() {
        float basePush = 28f;
        float growth = 1.85f;
        float maxPush = 620f;

        float pushSpeed = basePush * ((float)Math.exp(growth * timeInWind) - 1f);

        if (pushSpeed > maxPush) {
            pushSpeed = maxPush;
        }

        return pushSpeed;
    }

    public float getTimeInWind() {
        return timeInWind;
    }

    public void draw(SpriteBatch batch) {
        if (!active) {
            return;
        }

        int tileCount = Math.max(1, (int)Math.ceil(width / tileW));

        for (int i = 0; i < tileCount; i++) {
            float drawX = x + i * tileW;
            float currentW = Math.min(tileW, x + width - drawX);

            if (currentW <= 0) {
                continue;
            }

            int frameIndex = frameIndexForTile(i, tileCount);
            Texture frame = getFrame(frameIndex);

            if (frame == null) {
                continue;
            }

            batch.draw(frame, drawX, y, currentW, visualHeight);
        }
    }

    private int frameIndexForTile(int tileIndexFromLeft, int tileCount) {
        int distanceFromFan;

        if (directionX < 0) {
            distanceFromFan = tileCount - 1 - tileIndexFromLeft;
        } else {
            distanceFromFan = tileIndexFromLeft;
        }

        int animationOffset = ((int)(animationTimer * 8f)) % 4;

        return (distanceFromFan + animationOffset) % 4;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        // Invisible hitbox/tracking zone.
    }

    private Texture getFrame(int index) {
        if (index >= 0 && index < windFrames.length && windFrames[index] != null) {
            return windFrames[index];
        }

        for (Texture fallback : windFrames) {
            if (fallback != null) {
                return fallback;
            }
        }

        return null;
    }

    private boolean overlaps(float ax, float ay, float aw, float ah,
                             float bx, float by, float bw, float bh) {
        return ax < bx + bw &&
               ax + aw > bx &&
               ay < by + bh &&
               ay + ah > by;
    }
}
