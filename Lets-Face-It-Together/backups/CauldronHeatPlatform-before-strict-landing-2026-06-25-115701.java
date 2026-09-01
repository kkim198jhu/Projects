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

        boolean landingOnTop =
            player.velocityY <= 0.0f &&
            player.previousY >= platformTop - 4.0f &&
            player.y <= platformTop + 4.0f;

        boolean alreadyStandingOnTop =
            player.onGround &&
            Math.abs(player.y - platformTop) <= 10.0f &&
            player.x + player.width / 2.0f >= x &&
            player.x + player.width / 2.0f <= x + width;

        if (landingOnTop || alreadyStandingOnTop) {
            player.y = platformTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }

        // No side collision. This prevents the cauldron/platform from kicking
        // or teleporting the player sideways while jumping into the bowl.
    }

    public boolean isPlayerStandingOn(Player player) {
        float platformTop = y + height;
        float centerX = player.x + player.width / 2.0f;

        boolean centerOverPlatform =
            centerX >= x &&
            centerX <= x + width;

        boolean feetOnTop =
            Math.abs(player.y - platformTop) <= 10.0f;

        return player.onGround && centerOverPlatform && feetOnTop;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        // Small debug-visible platform inside the cauldron.
        // It will mostly be hidden by the cauldron sprite in foreground.
        shapeRenderer.setColor(new Color(0.20f, 0.08f, 0.04f, 0.85f));
        shapeRenderer.rect(x, y, width, height);
    }
}
