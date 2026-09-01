package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

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
        if (!Collision.overlaps(player.x, player.y, player.width, player.height, x, y, width, height)) {
            return;
        }

        float platformTop = y + height;
        float playerCenterX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            playerCenterX >= x + 4.0f &&
            playerCenterX <= x + width - 4.0f;

        // Only catch the player while falling DOWN onto the hidden platform.
        // This prevents the platform from snapping/teleporting the player while jumping up into the cauldron.
        boolean fallingDown = player.velocityY <= 0.0f;

        boolean crossedPlatformTop =
            player.previousY >= platformTop &&
            player.y <= platformTop;

        if (centerOverPlatform && fallingDown && crossedPlatformTop) {
            player.y = platformTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }

        // No side collision and no upward collision.
        // The player can freely jump through the cauldron area.
    }

    public boolean isPlayerStandingOn(Player player) {
        float platformTop = y + height;
        float centerX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            centerX >= x + 4.0f &&
            centerX <= x + width - 4.0f;

        boolean feetOnTop =
            Math.abs(player.y - platformTop) <= 6.0f;

        return player.onGround && centerOverPlatform && feetOnTop;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        // Debug-visible small platform inside cauldron.
        shapeRenderer.setColor(new Color(0.20f, 0.08f, 0.04f, 0.85f));
        shapeRenderer.rect(x, y, width, height);
    }
}
