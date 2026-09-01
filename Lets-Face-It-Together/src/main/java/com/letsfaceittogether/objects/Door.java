package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class Door {
    public float x;
    public float y;
    public float width = 75;
    public float height = 120;

    private boolean open = false;

    private Texture closedTexture;
    private Texture openedTexture;
    private boolean spritesLoaded = false;

    public Door(float x, float y) {
        this.x = x;
        this.y = y;

        try {
            closedTexture = new Texture("assets/sprites/objects/Doors/Closed Door.png");
            openedTexture = new Texture("assets/sprites/objects/Doors/Opened Door.png");
            spritesLoaded = true;
            System.out.println("Loaded door sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load door sprites. Using placeholder.");
        }
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean playerTouches(Player player) {
        return Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );
    }

    public void draw(ShapeRenderer shapeRenderer) {
        // Temporary outline/debug frame so we can always see the door area.
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, y, width, height);

        if (!spritesLoaded) {
            if (open) {
                shapeRenderer.setColor(Color.SKY);
            } else {
                shapeRenderer.setColor(Color.BLUE);
            }

            shapeRenderer.rect(x + 4, y + 4, width - 8, height - 8);
        }
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) {
            return;
        }

        Texture currentTexture = open ? openedTexture : closedTexture;

        float visualWidth = 75;
        float visualHeight = 120;

        float visualX = x;
        float visualY = y;

        batch.draw(currentTexture, visualX, visualY, visualWidth, visualHeight);
    }
}
