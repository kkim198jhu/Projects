package com.letsfaceittogether.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Fan {
    public float x;
    public float y;
    public float width;
    public float height;

    private Texture phase1;
    private Texture phase2;

    private float animationTimer = 0f;
    private boolean active = true;

    public Fan(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (Gdx.files.internal("assets/sprites/objects/Fan/Fan (Phase 1).png").exists()) {
            phase1 = new Texture("assets/sprites/objects/Fan/Fan (Phase 1).png");
        }

        if (Gdx.files.internal("assets/sprites/objects/Fan/Fan (Phase 2).png").exists()) {
            phase2 = new Texture("assets/sprites/objects/Fan/Fan (Phase 2).png");
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void update(float delta) {
        if (active) {
            animationTimer += delta;
        }
    }

    public void draw(SpriteBatch batch) {
        Texture current = null;

        if (!active) {
            current = phase1;
        } else if (phase1 != null && phase2 != null) {
            current = ((int)(animationTimer * 8) % 2 == 0) ? phase1 : phase2;
        } else if (phase1 != null) {
            current = phase1;
        } else if (phase2 != null) {
            current = phase2;
        }

        if (current != null) {
            batch.draw(current, x, y, width, height);
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (phase1 != null || phase2 != null) {
            return;
        }

        shapeRenderer.setColor(active ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
    }
}
