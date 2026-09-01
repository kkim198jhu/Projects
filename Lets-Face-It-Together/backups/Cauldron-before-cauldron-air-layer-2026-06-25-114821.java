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
        if (isPlayerInside(player) && player.emotion == Emotion.ANGRY) {
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
        float playerLeft = player.x;
        float playerRight = player.x + player.width;
        float playerBottom = player.y;
        float playerTop = player.y + player.height;

        // Bowl opening/interior volume.
        float bowlLeft = x + width * 0.16f;
        float bowlRight = x + width * 0.84f;
        float bowlBottom = y + height * 0.20f;
        float bowlTop = y + height * 1.05f;

        boolean horizontallyInside =
            playerRight > bowlLeft &&
            playerLeft < bowlRight;

        boolean verticallyInside =
            playerTop > bowlBottom &&
            playerBottom < bowlTop;

        // Important: walking on the ground in front of the cauldron should not count.
        boolean notJustWalkingOnFloor =
            playerBottom > y + height * 0.10f || playerTop > y + height * 0.70f;

        return horizontallyInside && verticallyInside && notJustWalkingOnFloor;
    }

    public boolean shouldDrawInFrontOfPlayer(Player player) {
        return isPlayerInside(player);
    }

    public void blockPlayer(Player player) {
        // Intentionally empty.
        // The cauldron is NOT solid. It should never push, teleport, or clamp the player.
        // The player can walk in front of it and jump/fall through the bowl.
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

        // Force every phase into the same rectangle so size stays consistent.
        batch.draw(phaseTextures[getPhase()], x, y, width, height);
    }
}
