package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class HiddenPathWall {
    public float x;
    public float y;
    public float width;
    public float height;

    private float hiddenY;
    private float hiddenHeight;

    public HiddenPathWall(float x, float y, float width, float height, float hiddenY, float hiddenHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hiddenY = hiddenY;
        this.hiddenHeight = hiddenHeight;
    }

    public void blockPlayer(Player player) {
        boolean surpriseMode = player.emotion == Emotion.SURPRISE;

        boolean touchingWholeWall = Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );

        if (!touchingWholeWall) {
            return;
        }

        boolean touchingHiddenPassage = Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, hiddenY, width, hiddenHeight
        );

        // If the player switches out of surprise while inside the hidden passage,
        // reset them so they do not get trapped inside the wall.
        if (!surpriseMode && touchingHiddenPassage) {
            resetPlayer(player);
            return;
        }

        // While surprised, the hidden passage is open.
        // But the bottom of the passage should still behave like a floor.
        if (surpriseMode && touchingHiddenPassage) {
            if (landOnHiddenPassageFloor(player)) {
                return;
            }

            // Otherwise, allow the player to move through the hidden passage.
            return;
        }

        resolveSolidWallCollision(player);
    }

    private boolean landOnHiddenPassageFloor(Player player) {
        float previousBottom = player.previousY;
        float currentBottom = player.y;

        boolean horizontallyOverWall =
                player.x + player.width > x &&
                player.x < x + width;

        boolean fallingOntoHiddenFloor =
                player.velocityY <= 0 &&
                previousBottom >= hiddenY &&
                currentBottom <= hiddenY;

        if (horizontallyOverWall && fallingOntoHiddenFloor) {
            player.y = hiddenY;
            player.velocityY = 0;
            player.onGround = true;
            return true;
        }

        return false;
    }

    private void resolveSolidWallCollision(Player player) {
        float previousBottom = player.previousY;
        float previousTop = player.previousY + player.height;
        float previousRight = player.previousX + player.width;
        float previousLeft = player.previousX;

        float playerBottom = player.y;
        float playerTop = player.y + player.height;
        float playerRight = player.x + player.width;
        float playerLeft = player.x;

        float wallTop = y + height;
        float wallBottom = y;
        float wallLeft = x;
        float wallRight = x + width;

        // Landing on top of the whole wall.
        if (previousBottom >= wallTop && playerBottom <= wallTop) {
            player.y = wallTop;
            player.velocityY = 0;
            player.onGround = true;
            return;
        }

        // Hitting underside.
        if (previousTop <= wallBottom && playerTop >= wallBottom) {
            player.y = wallBottom - player.height;
            player.velocityY = 0;
            return;
        }

        // Hitting left side.
        if (previousRight <= wallLeft && playerRight >= wallLeft) {
            player.x = wallLeft - player.width;
            return;
        }

        // Hitting right side.
        if (previousLeft >= wallRight && playerLeft <= wallRight) {
            player.x = wallRight;
            return;
        }

        // Failsafe.
        resetPlayer(player);
    }

    private void resetPlayer(Player player) {
        player.x = 100;
        player.y = 90;
        player.velocityY = 0;
        player.onGround = true;
        System.out.println("You got stuck in the hidden wall! Resetting.");
    }

    public void draw(ShapeRenderer shapeRenderer, Player player) {
        boolean surpriseMode = player.emotion == Emotion.SURPRISE;

        if (!surpriseMode) {
            // Normal mode: wall looks completely solid.
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(x, y, width, height);
            return;
        }

        // Surprise mode: reveal the hidden passage.
        shapeRenderer.setColor(Color.DARK_GRAY);

        // Bottom solid section.
        shapeRenderer.rect(x, y, width, hiddenY - y);

        // Top solid section.
        float topStart = hiddenY + hiddenHeight;
        shapeRenderer.rect(x, topStart, width, y + height - topStart);

        // Revealed hidden passage area.
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, hiddenY, width, hiddenHeight);

        // Floor line of the hidden passage, so it is clear you can stand on it.
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(x, hiddenY - 4, width, 4);
    }
}
