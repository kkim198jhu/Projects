package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class CauldronLiftPlatform {
    public float x;
    public float baseY;
    public float y;
    public float width;
    public float height;

    private float liftHeight;
    private float animationTimer = 0.0f;
    private int animationFrame = 0;

    private Texture platformPhase1;
    private Texture platformPhase2;

    private Texture heatRisePhase1;
    private Texture heatWavePhase2;
    private TextureRegion heatRiseRegion;
    private TextureRegion heatWaveRegion;
    private boolean platformSpritesLoaded = false;
    private boolean windSpritesLoaded = false;

    public CauldronLiftPlatform(float x, float baseY, float width, float height, float liftHeight) {
        this.x = x;
        this.baseY = baseY;
        this.y = baseY;
        this.width = width;
        this.height = height;
        this.liftHeight = liftHeight;

        try {
            platformPhase1 = new Texture("assets/sprites/objects/Moving Platform/Idle/Moving Platform (Phase 1).png");
            platformPhase2 = new Texture("assets/sprites/objects/Moving Platform/Idle/Moving Platform (Phase 2).png");

            platformPhase1.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            platformPhase2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            heatRisePhase1 = new Texture("assets/sprites/objects/Heat Waves/Heat Rise (Phase 1).png");
            heatWavePhase2 = new Texture("assets/sprites/objects/Heat Waves/Heat Wave (Phase 2).png");
            heatRisePhase1.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            heatWavePhase2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            // Crop out the orange base strip at the bottom of each heat-wave sprite.
            heatRiseRegion = cropHeatTexture(heatRisePhase1);
            heatWaveRegion = cropHeatTexture(heatWavePhase2);

            platformSpritesLoaded = true;
            System.out.println("Loaded cauldron lift platform sprites.");
        } catch (GdxRuntimeException e) {
            platformSpritesLoaded = false;
            System.out.println("Could not load moving platform idle sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 1; i <= 4; i++) {
            }

            windSpritesLoaded = true;
            System.out.println("Loaded heat wave sprites.");
        } catch (GdxRuntimeException e) {
            windSpritesLoaded = false;
            System.out.println("Could not load heat wave sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public void update(float delta, float heatRatio) {
        animationTimer += delta;

        if (animationTimer >= 0.18f) {
            animationTimer = 0.0f;
            animationFrame = (animationFrame + 1) % 2;
        }

        float targetY = baseY + liftHeight * heatRatio;

        // Smooth lift movement.
        y += (targetY - y) * Math.min(1.0f, delta * 5.0f);
    }

    public void blockPlayer(Player player) {
        float platformTop = y + height;
        float playerCenterX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean fallingDown =
            player.velocityY <= 0.0f;

        // Swept top collision:
        // If player's feet were above the platform last frame and are now
        // at/below the platform top, land on it.
        boolean crossedPlatformTop =
            player.previousY >= platformTop &&
            player.y <= platformTop;

        if (centerOverPlatform && fallingDown && crossedPlatformTop) {
            player.y = platformTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }

        // Important:
        // No side collision.
        // No bottom collision.
        // No overlap push-out.
        // This prevents snapping when jumping near/into the cauldron.
    }

    private TextureRegion cropHeatTexture(Texture texture) {
        int cropTop = 0;
        int cropBottomPixels = Math.max(1, texture.getHeight() / 4);
        int croppedHeight = Math.max(1, texture.getHeight() - cropBottomPixels);

        return new TextureRegion(
            texture,
            0,
            cropTop,
            texture.getWidth(),
            croppedHeight
        );
    }

    public void drawWind(ShapeRenderer shapeRenderer, float heatRatio) {
        if (heatRatio <= 0.02f) return;

        // Fallback heat-wave visualization if sprites fail.
        float cauldronTopY = 195.0f;
        float heatBottomY = cauldronTopY + 12.0f;
        float heatTopY = y - 8.0f;

        if (heatTopY <= heatBottomY) return;

        float centerX = x + width / 2.0f;
        float totalH = heatTopY - heatBottomY;

        shapeRenderer.setColor(1.0f, 0.45f, 0.12f, 0.55f * heatRatio);

        int waves = Math.max(1, Math.round(3 * heatRatio));
        for (int i = 0; i < waves; i++) {
            float t = waves == 1 ? 0.0f : (float)i / (float)(waves - 1);
            float drawY = heatBottomY + (totalH * 0.78f) * t;
            float drawW = 26.0f + 12.0f * t;
            shapeRenderer.rect(centerX - drawW / 2.0f, drawY, drawW, 4.0f);
        }
    }

    public void drawWind(SpriteBatch batch, float heatRatio) {
        if (heatRatio <= 0.02f) return;
        if (heatRiseRegion == null || heatWaveRegion == null) return;

        // Heat waves should only appear above the cauldron, not on the pot body.
        // TestLevel cauldron currently has y=90 and height=105, so top is about 195.
        float cauldronTopY = 195.0f;
        float heatBottomY = cauldronTopY + 12.0f;
        float heatTopY = y - 8.0f;

        if (heatTopY <= heatBottomY) return;

        float centerX = x + width / 2.0f;
        float totalH = heatTopY - heatBottomY;

        // Alternate between the two new heat-wave sprites.
        TextureRegion heatTexture = animationFrame == 0 ? heatRiseRegion : heatWaveRegion;

        int waveCount = Math.max(2, Math.round(6 * heatRatio));

        for (int i = 0; i < waveCount; i++) {
            float t = waveCount == 1 ? 0.0f : (float)i / (float)(waveCount - 1);

            float drawW = 52.0f + 22.0f * t;
            float drawH = 34.0f + 10.0f * t;

            float drawX = centerX - drawW / 2.0f;
            float drawY = heatBottomY + (totalH * 0.78f) * t;

            batch.draw(heatTexture, drawX, drawY, drawW, drawH);
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (platformSpritesLoaded) return;

        shapeRenderer.setColor(Color.SKY);
        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!platformSpritesLoaded) return;

        Texture texture = animationFrame == 0 ? platformPhase1 : platformPhase2;
        batch.draw(texture, x, y, width, height);
    }
}
