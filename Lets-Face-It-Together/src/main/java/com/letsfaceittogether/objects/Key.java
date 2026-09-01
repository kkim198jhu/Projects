package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class Key {
    public float x;
    public float y;
    public float radius = 15;
    public boolean pickedUp = false;
    public boolean consumed = false;

    private Texture keyTexture;
    private boolean spriteLoaded = false;

    public Key(float x, float y) {
        this.x = x;
        this.y = y;

        try {
            keyTexture = new Texture("assets/sprites/objects/Key.png");
            spriteLoaded = true;
            System.out.println("Loaded key sprite.");
        } catch (GdxRuntimeException e) {
            spriteLoaded = false;
            System.out.println("Could not load key sprite. Using circle placeholder.");
        }
    }

    public void update(Player player) {
        if (consumed) {
            return;
        }

        if (!pickedUp) {
            boolean touchingPlayer = Collision.overlaps(
                    player.x, player.y, player.width, player.height,
                    x - radius, y - radius, radius * 2, radius * 2
            );

            if (touchingPlayer) {
                pickedUp = true;
                System.out.println("Key picked up!");
            }
        } else {
            // Float behind and above the player after pickup.
            // Direction depends on which way the player is facing.
            if (player.facingDirection == 1) {
                x = player.x - 35;
            } else {
                x = player.x + player.width + 35;
            }

            y = player.y + player.height + 45 + (float)Math.sin(System.currentTimeMillis() / 180.0) * 6;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (consumed) {
            return;
        }

        // If sprite loaded, SpriteBatch will draw it instead.
        if (spriteLoaded) {
            return;
        }

        if (!pickedUp) {
            shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.circle(x, y, radius);
        } else {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.circle(x, y, radius);
        }
    }

    public void draw(SpriteBatch batch) {
        if (consumed) {
            return;
        }

        if (!spriteLoaded) {
            return;
        }

        float visualSize = 84;
        float visualX = x - visualSize / 2f;
        float visualY = y - visualSize / 2f;

        batch.draw(keyTexture, visualX, visualY, visualSize, visualSize);
    }
}
