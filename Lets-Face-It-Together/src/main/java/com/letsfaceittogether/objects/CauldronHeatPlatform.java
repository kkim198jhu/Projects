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
        float platformTop = y + height;
        float playerCenterX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean fallingDown =
            player.velocityY <= 0.0f;

        // Swept top collision:
        // If the player's feet were above the platform last frame
        // and are now at/below the platform top, they landed on it.
        boolean crossedPlatformTop =
            player.previousY >= platformTop &&
            player.y <= platformTop;

        if (centerOverPlatform && fallingDown && crossedPlatformTop) {
            player.y = platformTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }

        // Important:
        // No side collision.
        // No bottom collision.
        // No overlap push-out.
        // This prevents the cauldron from snapping/teleporting the player.
    }

    public boolean isPlayerStandingOn(Player player) {
        float platformTop = y + height;
        float playerCenterX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean feetOnPlatform =
            Math.abs(player.y - platformTop) <= 4.0f;

        return player.onGround && centerOverPlatform && feetOnPlatform;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        // Visible small platform inside the cauldron for testing.
        // Once it feels right, we can hide it or make it match the cauldron art.
        shapeRenderer.setColor(new Color(0.20f, 0.08f, 0.04f, 0.85f));
        shapeRenderer.rect(x, y, width, height);
    }
}
