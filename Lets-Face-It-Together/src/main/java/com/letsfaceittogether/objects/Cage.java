package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Cage {
    public float x;
    public float y;
    public float width;
    public float height;

    private boolean closed = false;

    private Texture openedTexture;
    private Texture closedTexture;
    private boolean spritesLoaded = false;

    public Cage(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            openedTexture = new Texture("assets/sprites/objects/Doors/Jail Door (Opened).png");
            closedTexture = new Texture("assets/sprites/objects/Doors/Jail Door (Closed).png");
            spritesLoaded = true;
            System.out.println("Loaded jail door sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load jail door sprites. Using placeholder cage.");
        }
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (spritesLoaded) {
            return;
        }

        if (closed) {
            shapeRenderer.setColor(Color.DARK_GRAY);
        } else {
            shapeRenderer.setColor(Color.GRAY);
        }

        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) {
            return;
        }

        Texture texture = closed ? closedTexture : openedTexture;

        float visualWidth = width;
        float visualHeight = height;

        batch.draw(texture, x, y, visualWidth, visualHeight);
    }
}
