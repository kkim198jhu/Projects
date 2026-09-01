package com.letsfaceittogether.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.utils.ScreenCoords;

public class BackButton {
    public float x = 20;
    public float y = 485;
    public float width = 45;
    public float height = 35;

    public boolean clicked() {
        if (!Gdx.input.justTouched()) {
            return false;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + height;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.triangle(
                x + 12, y + height / 2,
                x + 30, y + height - 8,
                x + 30, y + 8
        );
    }
}
