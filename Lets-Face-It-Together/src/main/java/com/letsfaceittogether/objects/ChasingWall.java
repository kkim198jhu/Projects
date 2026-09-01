package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class ChasingWall {
    public float x;
    public float y;
    public float width;
    public float height;

    private float startX;
    private float startY;

    private float speed = 330f;

    private boolean caughtPlayer = false;

    public ChasingWall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.startX = x;
        this.startY = y;
    }

    public void update(float delta, Player player) {
        caughtPlayer = false;

        x += speed * delta;

        if (Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        )) {
            caughtPlayer = true;
        }
    }

    public boolean caughtPlayer() {
        return caughtPlayer;
    }

    public void reset() {
        x = startX;
        y = startY;
        caughtPlayer = false;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.PURPLE);
        shapeRenderer.rect(x, y, width, height);
    }
}
