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
        // Intentionally empty.
        // This object is a heat/standing detection zone only.
        // It must never move, snap, push, clamp, or teleport the player.
    }

    public boolean isPlayerStandingOn(Player player) {
        float playerCenterX = player.x + player.width / 2.0f;
        float playerBottom = player.y;
        float playerTop = player.y + player.height;

        boolean centerInside =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean bodyInsideZone =
            playerTop > y &&
            playerBottom < y + height;

        // Also require player to be above floor level so walking in front does not heat it.
        boolean notJustWalkingInFront =
            playerBottom > 95.0f || !player.onGround;

        return centerInside && bodyInsideZone && notJustWalkingInFront;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        // Debug-visible heat zone inside cauldron.
        // Since it is non-solid, this is just showing where Angry heating works.
        shapeRenderer.setColor(new Color(0.20f, 0.08f, 0.04f, 0.65f));
        shapeRenderer.rect(x, y, width, height);
    }
}
