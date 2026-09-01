package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class LaserEmitter {
    public float x;
    public float y;
    public float width;
    public float height;

    private float laserLength;
    private boolean blocked = false;

    private Texture turretTexture;
    private Texture[] laserFrames = new Texture[6];

    private boolean turretLoaded = false;
    private boolean laserSpritesLoaded = false;

    // Original lazer art size.
    private final float laserPieceWidth = 36f;
    private final float laserPieceHeight = 8f;
    private final float laserScale = 5f;

    // Original turret art is 10x10, so scale it up.
    private final float turretScale = 6f;

    // 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1
    private static final int[] PHASES = {0, 1, 2, 3, 4, 5, 4, 3, 2, 1, 0};
    private static final long FRAME_TIME_MS = 80;

    public LaserEmitter(float x, float y, float width, float height, float laserLength) {
        this.x = x;
        this.y = y;

        // Turret is drawn using its sprite scale, not the constructor width/height.
        this.width = 10f * turretScale;
        this.height = 10f * turretScale;

        this.laserLength = laserLength;

        loadSprites();
    }

    private void loadSprites() {
        try {
            turretTexture = new Texture("assets/sprites/objects/Lazer Turret (Facing Left).png");
            turretTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            turretLoaded = true;
            System.out.println("Loaded lazer turret sprite.");
        } catch (GdxRuntimeException e) {
            turretLoaded = false;
            System.out.println("Could not load lazer turret sprite. Using placeholder.");
            System.out.println(e.getMessage());
        }

        try {
            for (int i = 0; i < 6; i++) {
                laserFrames[i] = new Texture("assets/sprites/objects/Lazer/Lazer (Phase " + (i + 1) + ").png");
                laserFrames[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            }

            laserSpritesLoaded = true;
            System.out.println("Loaded animated lazer sprites.");
        } catch (GdxRuntimeException e) {
            laserSpritesLoaded = false;
            System.out.println("Could not load animated lazer sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public boolean update(Player player) {
        // Sad can safely touch the lazer, but it does not block/remove it.
        return blocked;
    }

    public boolean hitsPlayer(Player player) {
        if (blocked) {
            return false;
        }

        return laserHits(player);
    }

    private boolean laserHits(Player player) {
        return Collision.overlaps(
                player.x, player.y, player.width, player.height,
                getLaserX(), getLaserY(), getLaserWidth(), getLaserHeight()
        );
    }

    private int getPieceCount() {
        float pieceW = laserPieceWidth * laserScale;
        return Math.max(1, Math.round(laserLength / pieceW));
    }

    private float getLaserX() {
        // Shoots left from the turret. Use full-sized pieces only.
        return x - getLaserWidth();
    }

    private float getLaserY() {
        // Center the lazer vertically with the turret.
        return y + height / 2f - getLaserHeight() / 2f;
    }

    private float getLaserWidth() {
        return getPieceCount() * laserPieceWidth * laserScale;
    }

    private float getLaserHeight() {
        return laserPieceHeight * laserScale;
    }

    private int getCurrentFrameIndex() {
        long step = (TimeUtils.millis() / FRAME_TIME_MS) % PHASES.length;
        return PHASES[(int) step];
    }

    public void draw(SpriteBatch batch) {
        drawLazer(batch);
        drawTurret(batch);
    }

    private void drawLazer(SpriteBatch batch) {
        if (blocked || !laserSpritesLoaded) {
            return;
        }

        Texture frame = laserFrames[getCurrentFrameIndex()];

        float pieceW = laserPieceWidth * laserScale;
        float pieceH = laserPieceHeight * laserScale;

        float startX = getLaserX();
        float laserY = getLaserY();

        int pieces = getPieceCount();

        // Draw every piece at the same size.
        // No squished partial tile at the beginning/end.
        for (int i = 0; i < pieces; i++) {
            float drawX = startX + i * pieceW;
            batch.draw(frame, drawX, laserY, pieceW, pieceH);
        }
    }

    private void drawTurret(SpriteBatch batch) {
        if (!turretLoaded) {
            return;
        }

        batch.draw(turretTexture, x, y, width, height);
    }

    public void draw(ShapeRenderer shapeRenderer) {
        // Fallback turret rectangle if sprite fails.
        if (!turretLoaded) {
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(x, y, width, height);
        }

        // Fallback lazer if sprites fail.
        if (!laserSpritesLoaded && !blocked) {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(getLaserX(), getLaserY(), getLaserWidth(), getLaserHeight());
        }
    }
}
