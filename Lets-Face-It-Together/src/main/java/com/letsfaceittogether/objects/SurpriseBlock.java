package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class SurpriseBlock {
    public float x;
    public float y;
    public float width;
    public float height;

    public SurpriseBlock(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void blockPlayer(Player player) {
        // Surprise blocks only exist while the player is in Surprise mode.
        if (player.emotion != Emotion.SURPRISE) {
            return;
        }

        boolean touching = Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );

        if (!touching) {
            return;
        }

        resolveCollision(player);
    }

    private void resolveCollision(Player player) {
        float previousBottom = player.previousY;
        float previousTop = player.previousY + player.height;
        float previousRight = player.previousX + player.width;
        float previousLeft = player.previousX;

        float playerBottom = player.y;
        float playerTop = player.y + player.height;
        float playerRight = player.x + player.width;
        float playerLeft = player.x;

        float blockTop = y + height;
        float blockBottom = y;
        float blockLeft = x;
        float blockRight = x + width;

        // Landing on top.
        if (player.velocityY <= 0 && previousBottom >= blockTop && playerBottom <= blockTop) {
            player.y = blockTop;
            player.velocityY = 0;
            player.onGround = true;
            return;
        }

        // Hitting underside.
        if (player.velocityY > 0 && previousTop <= blockBottom && playerTop >= blockBottom) {
            player.y = blockBottom - player.height;
            player.velocityY = 0;
            return;
        }

        // Hitting left side.
        if (previousRight <= blockLeft && playerRight >= blockLeft) {
            player.x = blockLeft - player.width;
            return;
        }

        // Hitting right side.
        if (previousLeft >= blockRight && playerLeft <= blockRight) {
            player.x = blockRight;
            return;
        }

        // Failsafe: push player to previous position if embedded.
        player.revertToPreviousPosition();
    }

    public void draw(ShapeRenderer shapeRenderer, Player player) {
        if (player.emotion != Emotion.SURPRISE) {
            return;
        }

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(x, y, width, height);
    }
}
