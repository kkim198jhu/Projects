package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;

public class Cauldron {
    public float x;
    public float y;
    public float width;
    public float height;

    private float heat = 0.0f;

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

    public void update(float delta, Player player, boolean standingInHeatZone) {
        if (standingInHeatZone && player.emotion == Emotion.ANGRY) {
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

    public boolean isPlayerOverCauldron(Player player) {
        float centerX = player.x + player.width / 2.0f;
        return centerX >= x + width * 0.05f && centerX <= x + width * 0.95f;
    }

    public boolean shouldDrawInFrontOfPlayer(Player player, boolean standingInHeatZone) {
        return standingInHeatZone || (!player.onGround && isPlayerOverCauldron(player));
    }

    public void blockPlayer(Player player) {
        // Hard disabled. This class must never move the player.
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (spritesLoaded) return;

        if (getPhase() == 0) shapeRenderer.setColor(Color.DARK_GRAY);
        else if (getPhase() == 1) shapeRenderer.setColor(Color.ORANGE);
        else if (getPhase() == 2) shapeRenderer.setColor(Color.RED);
        else shapeRenderer.setColor(Color.YELLOW);

        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) return;
        batch.draw(phaseTextures[getPhase()], x, y, width, height);
    }
}
