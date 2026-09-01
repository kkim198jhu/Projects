package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class BreakableWall {
    public float x;
    public float y;
    public float width;
    public float height;

    private boolean broken = false;

    private Texture crackedWallTexture;
    private boolean spriteLoaded = false;

    public BreakableWall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            crackedWallTexture = new Texture("assets/sprites/objects/Walls/Cracked Wall.png");
            spriteLoaded = true;
            System.out.println("Loaded cracked wall sprite.");
        } catch (GdxRuntimeException e) {
            spriteLoaded = false;
            System.out.println("Could not load cracked wall sprite. Using placeholder.");
        }
    }

    public void update(Player player) {
        handlePlayer(player);
    }

    public void blockPlayer(Player player) {
        handlePlayer(player);
    }

    public void blockOrBreakPlayer(Player player) {
        handlePlayer(player);
    }

    private void handlePlayer(Player player) {
        if (broken) {
            return;
        }

        boolean touching = Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );

        if (!touching) {
            return;
        }

        if (player.emotion == Emotion.ANGRY) {
            broken = true;
            System.out.println("Angry broke one cracked block!");
            return;
        }

        resolveCollision(player);
    }

    private void resolveCollision(Player player) {
        float previousBottom = player.previousY;
        float previousTop = player.previousY + player.height;
        float previousLeft = player.previousX;
        float previousRight = player.previousX + player.width;

        float playerBottom = player.y;
        float playerTop = player.y + player.height;
        float playerLeft = player.x;
        float playerRight = player.x + player.width;

        float blockBottom = y;
        float blockTop = y + height;
        float blockLeft = x;
        float blockRight = x + width;

        // Important:
        // Only allow landing on top if the player was already horizontally over the block.
        // This prevents side-touching the wall from counting as "onGround."
        boolean wasHorizontallyOverBlock =
                previousRight > blockLeft + 5 &&
                previousLeft < blockRight - 5;

        boolean isHorizontallyOverBlock =
                playerRight > blockLeft + 5 &&
                playerLeft < blockRight - 5;

        // Land on top of the block.
        if (wasHorizontallyOverBlock &&
            isHorizontallyOverBlock &&
            player.velocityY <= 0 &&
            previousBottom >= blockTop &&
            playerBottom <= blockTop) {

            player.y = blockTop;
            player.velocityY = 0;
            player.onGround = true;
            return;
        }

        // Hit the underside of the block.
        if (wasHorizontallyOverBlock &&
            isHorizontallyOverBlock &&
            player.velocityY > 0 &&
            previousTop <= blockBottom &&
            playerTop >= blockBottom) {

            player.y = blockBottom - player.height;
            player.velocityY = 0;
            return;
        }

        // Hit the left side of the block.
        if (previousRight <= blockLeft && playerRight >= blockLeft) {
            player.x = blockLeft - player.width;
            return;
        }

        // Hit the right side of the block.
        if (previousLeft >= blockRight && playerLeft <= blockRight) {
            player.x = blockRight;
            return;
        }

        // Extra side failsafe for when the player is already slightly embedded.
        float overlapLeft = playerRight - blockLeft;
        float overlapRight = blockRight - playerLeft;
        float overlapBottom = playerTop - blockBottom;
        float overlapTop = blockTop - playerBottom;

        float smallestOverlap = Math.min(
                Math.min(overlapLeft, overlapRight),
                Math.min(overlapBottom, overlapTop)
        );

        if (smallestOverlap == overlapLeft) {
            player.x = blockLeft - player.width;
        } else if (smallestOverlap == overlapRight) {
            player.x = blockRight;
        } else if (smallestOverlap == overlapTop) {
            player.y = blockTop;
            player.velocityY = 0;
            player.onGround = true;
        } else {
            player.y = blockBottom - player.height;
            player.velocityY = 0;
        }
    }

    public boolean isBroken() {
        return broken;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (broken || spriteLoaded) {
            return;
        }

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (broken || !spriteLoaded) {
            return;
        }

        batch.draw(crackedWallTexture, x, y, width, height);
    }
}
