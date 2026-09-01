package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class Cauldron {
    public float x;
    public float y;
    public float width;
    public float height;

    private float heat = 0.0f; // 0.0 to 3.0
    private Texture[] phaseTextures = new Texture[4];
    private boolean spritesLoaded = false;

    private final float angryHeatRate = 1.65f;
    private final float coolRate = 0.35f;

    public Cauldron(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            for (int i = 0; i <= 3; i++) {
                phaseTextures[i] = new Texture("assets/sprites/objects/Cauldron/Cauldron (phase " + i + ").png");
                phaseTextures[i].setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            }

            spritesLoaded = true;
            System.out.println("Loaded cauldron sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load cauldron sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public void update(float delta, Player player) {
        boolean playerInside = isPlayerInside(player);

        if (playerInside && player.emotion == Emotion.ANGRY) {
            heat += angryHeatRate * delta;
        } else {
            heat -= coolRate * delta;
        }

        if (heat < 0.0f) heat = 0.0f;
        if (heat > 3.0f) heat = 3.0f;
    }

    public float getHeatRatio() {
        return heat / 3.0f;
    }

    public int getPhase() {
        if (heat < 0.75f) return 0;
        if (heat < 1.50f) return 1;
        if (heat < 2.35f) return 2;
        return 3;
    }

    public boolean isPlayerInside(Player player) {
        float playerCenterX = player.x + player.width / 2.0f;

        boolean horizontallyInside =
            playerCenterX >= x + width * 0.18f &&
            playerCenterX <= x + width * 0.82f;

        // Walking on the floor in front of the cauldron should NOT count as inside.
        // The player counts as inside only once their feet/body are above the lower bowl area.
        boolean verticallyInside =
            player.y >= y + height * 0.22f &&
            player.y <= y + height * 1.20f;

        return horizontallyInside && verticallyInside;
    }

    public boolean shouldDrawInFrontOfPlayer(Player player) {
        float playerCenterX = player.x + player.width / 2.0f;

        boolean horizontallyInside =
            playerCenterX >= x + width * 0.12f &&
            playerCenterX <= x + width * 0.88f;

        // If the player is low/on the ground, they walk in front of the cauldron.
        // If they jump/stand into the bowl area, the cauldron rim/body draws in front.
        boolean highEnoughToBeInside =
            player.y >= y + height * 0.25f;

        return horizontallyInside && highEnoughToBeInside;
    }

    public void blockPlayer(Player player) {
        if (!Collision.overlaps(player.x, player.y, player.width, player.height, x, y, width, height)) {
            return;
        }

        float cauldronTop = y + height * 0.72f;

        boolean landingOnRim =
            player.velocityY <= 0.0f &&
            player.previousY >= cauldronTop &&
            player.y <= cauldronTop;

        if (landingOnRim) {
            player.y = cauldronTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (spritesLoaded) return;

        int phase = getPhase();

        if (phase == 0) shapeRenderer.setColor(Color.DARK_GRAY);
        else if (phase == 1) shapeRenderer.setColor(Color.ORANGE);
        else if (phase == 2) shapeRenderer.setColor(Color.RED);
        else shapeRenderer.setColor(Color.YELLOW);

        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) return;

        // Every cauldron phase is forced into the exact same rectangle
        // so phase sprites stay visually consistent in size.
        batch.draw(phaseTextures[getPhase()], x, y, width, height);
    }
}
