package com.letsfaceittogether.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class MovingPlatform {
    public float x;
    public float y;
    public float width;
    public float height;

    private float minX;
    private float maxX;
    private float speed;
    private float direction = -1.0f;

    private float previousX;
    private float deltaX;

    private Texture left1;
    private Texture left2;

    private boolean spritesLoaded = false;
    private float animationTimer = 0.0f;

    public MovingPlatform(float x, float y, float width, float height, float minX, float maxX, float speed) {
        this.x = x;
        this.y = y;
        this.previousX = x;

        this.width = width;
        this.height = height;

        this.minX = minX;
        this.maxX = maxX;
        this.speed = speed;

        try {
            // Use the Moving Left sprites for BOTH directions.
            // When moving right, we flip these horizontally while drawing.
            left1 = new Texture("assets/sprites/objects/Moving Platform/Moving Left/Moving Platform (Phase 1) (Left).png");
            left2 = new Texture("assets/sprites/objects/Moving Platform/Moving Left/Moving Platform (Phase 2) (Left).png");

            left1.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            left2.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            spritesLoaded = true;
            System.out.println("Loaded moving platform sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load moving platform sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public void update(float delta) {
        previousX = x;

        animationTimer += delta;

        x += direction * speed * delta;

        if (x <= minX) {
            x = minX;
            direction = 1.0f;
        }

        if (x >= maxX) {
            x = maxX;
            direction = -1.0f;
        }

        deltaX = x - previousX;
    }

    public void blockPlayer(Player player) {
        boolean touchingPlayer = Collision.overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, height
        );

        if (!touchingPlayer) {
            return;
        }

        float platformTop = y + height;

        boolean landingOnPlatform =
            player.velocityY <= 0.0f &&
            player.previousY >= platformTop &&
            player.y <= platformTop;

        if (landingOnPlatform) {
            player.y = platformTop;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, platformTop);

            // Carry the player with the moving platform.
            player.x += deltaX;
            player.previousX = player.x;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (!spritesLoaded) {
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) {
            return;
        }

        Texture texture = ((int)(animationTimer * 6.0f) % 2 == 0) ? left1 : left2;

        if (direction < 0.0f) {
            // Moving left: draw normally.
            batch.draw(texture, x, y, width, height);
        } else {
            // Moving right: flip the same left-facing sprite horizontally.
            batch.draw(
                texture,
                x + width, y,
                -width, height
            );
        }
    }
}
