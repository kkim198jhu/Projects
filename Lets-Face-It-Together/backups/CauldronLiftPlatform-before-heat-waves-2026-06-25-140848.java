package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private Texture[] windTextures = new Texture[4];
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

            platformSpritesLoaded = true;
            System.out.println("Loaded cauldron lift platform sprites.");
        } catch (GdxRuntimeException e) {
            platformSpritesLoaded = false;
            System.out.println("Could not load moving platform idle sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 1; i <= 4; i++) {
                windTextures[i - 1] = new Texture("assets/sprites/objects/Wind/Wind (Phase " + i + ").png");
                windTextures[i - 1].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            }

            windSpritesLoaded = true;
            System.out.println("Loaded boiling wind sprites.");
        } catch (GdxRuntimeException e) {
            windSpritesLoaded = false;
            System.out.println("Could not load boiling wind sprites. Using placeholder.");
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

    public void drawWind(ShapeRenderer shapeRenderer, float heatRatio) {
        if (heatRatio <= 0.02f) return;

        // Visual steam column only ABOVE the cauldron.
        // Cauldron in TestLevel is currently y=90, height=105, so rim top is about 195.
        float cauldronTopY = 195.0f;
        float steamBottomY = cauldronTopY + 8.0f;
        float steamTopY = y - 10.0f;

        if (steamTopY <= steamBottomY) return;

        float centerX = x + width / 2.0f;
        float totalH = steamTopY - steamBottomY;

        shapeRenderer.setColor(0.70f, 0.82f, 1.0f, 0.55f * heatRatio);

        int bands = Math.max(1, Math.round(4 * heatRatio));
        for (int i = 0; i < bands; i++) {
            float t = bands == 1 ? 0.0f : (float)i / (float)(bands - 1);
            float bandY = steamBottomY + totalH * t;
            float bandW = 24.0f + 20.0f * t;
            shapeRenderer.rect(centerX - bandW / 2.0f, bandY, bandW, 5.0f);
        }
    }

    public void drawWind(SpriteBatch batch, float heatRatio) {
        if (heatRatio <= 0.02f) return;

        // Visual steam column only ABOVE the cauldron.
        // This avoids steam appearing on top of the cauldron body.
        float cauldronTopY = 195.0f;
        float steamBottomY = cauldronTopY + 8.0f;
        float steamTopY = y - 10.0f;

        if (steamTopY <= steamBottomY) return;

        float centerX = x + width / 2.0f;
        float totalH = steamTopY - steamBottomY;

        int bands = Math.max(1, Math.round(4 * heatRatio));

        for (int i = 0; i < bands; i++) {
            if (windTextures[i] == null) continue;

            float t = bands == 1 ? 0.0f : (float)i / (float)(bands - 1);
            float drawY = steamBottomY + totalH * t;

            // Keep the sprites small and centered so they look like rising wisps.
            float drawW = 28.0f + 12.0f * t;
            float drawH = 16.0f;

            batch.draw(windTextures[i], centerX - drawW / 2.0f, drawY, drawW, drawH);
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
