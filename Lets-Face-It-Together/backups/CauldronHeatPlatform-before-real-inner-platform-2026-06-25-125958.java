package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Player;

public class CauldronHeatPlatform {
    public float x;
    public float y;
    public float width;
    public float height;

    public CauldronHeatPlatform(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void blockPlayer(Player player) {
        // Hard disabled. This object must never move the player.
    }

    public boolean isPlayerStandingOn(Player player) {
        float centerX = player.x + player.width / 2.0f;
        float feetY = player.y;

        boolean centerInside = centerX >= x && centerX <= x + width;

        // Since this is non-solid, this is really a "heat zone" check.
        // It only counts when the player's feet are inside the cauldron bowl area.
        boolean feetInsideZone = feetY >= y && feetY <= y + height;

        return centerInside && feetInsideZone;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.20f, 0.08f, 0.04f, 0.55f));
        shapeRenderer.rect(x, y, width, height);
    }
}
