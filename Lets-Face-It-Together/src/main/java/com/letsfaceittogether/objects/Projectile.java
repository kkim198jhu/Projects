package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class Projectile {
    public float x;
    public float y;
    public float size;
    public float speed;
    public boolean active;
    public boolean blocked;

    private float startX;
    private float startY;

    public Projectile(float x, float y, float size, float speed) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.size = size;
        this.speed = speed;
        this.active = true;
        this.blocked = false;
    }

    public void update(float delta) {
        if (!active) return;

        x -= speed * delta;

        if (x < -50) {
            reset();
        }
    }

    public boolean hits(Player player) {
        if (!active) return false;

        return Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x - size, y - size, size * 2, size * 2
        );
    }

    public void block() {
        active = false;
        blocked = true;
    }

    public void reset() {
        x = startX;
        y = startY;
        active = true;
        blocked = false;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (active) {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.circle(x, y, size);
        }

        if (blocked) {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(x, y - size, 40, 40);
        }
    }
}
