package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class SurpriseWall {
    public float x;
    public float y;
    public float width;
    public float height;

    private Texture normalWallTexture;
    private Texture surpriseWallTexture;
    private boolean spritesLoaded = false;

    public SurpriseWall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            normalWallTexture = new Texture("assets/sprites/objects/Walls/Wall.png");
            surpriseWallTexture = new Texture("assets/sprites/objects/Walls/Wall (Surprise).png");
            spritesLoaded = true;
            System.out.println("Loaded surprise wall sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load surprise wall sprites. Using placeholder.");
        }
    }

    private boolean playerIsSurprise(Player player) {
    return player.emotion == Emotion.SURPRISE;
}

public void blockPlayer(Player player) {
    if (player.emotion == Emotion.SURPRISE) {
        return;
    }

    if (!Collision.overlaps(player.x, player.y, player.width, player.height, x, y, width, height)) {
        return;
    }

    float wallTop = y + height;
    float wallBottom = y;
    float wallLeft = x;
    float wallRight = x + width;

    float previousBottom = player.previousY;
    float previousTop = player.previousY + player.height;
    float previousLeft = player.previousX;
    float previousRight = player.previousX + player.width;

    float currentBottom = player.y;

    boolean wasAboveWall = previousBottom >= wallTop - 6;
    boolean wasBelowWall = previousTop <= wallBottom + 4;
    boolean wasLeftOfWall = previousRight <= wallLeft + 4;
    boolean wasRightOfWall = previousLeft >= wallRight - 4;

    boolean feetAreOnTopSurface = Math.abs(currentBottom - wallTop) <= 10;
    boolean alreadyStandingOnTop = player.onGround && feetAreOnTopSurface;

    // Smooth walking across adjacent block tops.
    if (wasAboveWall || alreadyStandingOnTop) {
        player.y = wallTop;
        player.previousY = player.y;
        player.velocityY = 0;
        player.onGround = true;
        return;
    }

    // Head bump.
    if (wasBelowWall) {
        player.y = wallBottom - player.height;
        player.previousY = player.y;
        player.velocityY = 0;
        return;
    }

    // Side hits.
    if (wasLeftOfWall) {
        player.x = wallLeft - player.width;
        player.previousX = player.x;
        return;
    }

    if (wasRightOfWall) {
        player.x = wallRight;
        player.previousX = player.x;
        return;
    }

    // Fallback:
    // If we are not clearly landing on top or hitting bottom, treat this as a side collision.
    // This prevents getting glued to tall stacked surprise walls.
    float playerCenterX = player.x + player.width / 2f;
    float wallCenterX = wallLeft + width / 2f;

    if (playerCenterX < wallCenterX) {
        player.x = wallLeft - player.width;
    } else {
        player.x = wallRight;
    }

    player.previousX = player.x;
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

        float wallBottom = y;
        float wallTop = y + height;
        float wallLeft = x;
        float wallRight = x + width;

        boolean wasHorizontallyOverWall =
                previousRight > wallLeft + 5 &&
                previousLeft < wallRight - 5;

        boolean isHorizontallyOverWall =
                playerRight > wallLeft + 5 &&
                playerLeft < wallRight - 5;

        // Land on top only if actually over the wall, not pushing into the side.
        if (wasHorizontallyOverWall &&
            isHorizontallyOverWall &&
            player.velocityY <= 0 &&
            previousBottom >= wallTop &&
            playerBottom <= wallTop) {

            player.y = wallTop;
            player.velocityY = 0;
            player.onGround = true;
            return;
        }

        // Hit underside only if actually under/overlapping horizontally.
        if (wasHorizontallyOverWall &&
            isHorizontallyOverWall &&
            player.velocityY > 0 &&
            previousTop <= wallBottom &&
            playerTop >= wallBottom) {

            player.y = wallBottom - player.height;
            player.velocityY = 0;
            return;
        }

        // Hit left side.
        if (previousRight <= wallLeft && playerRight >= wallLeft) {
            player.x = wallLeft - player.width;
            return;
        }

        // Hit right side.
        if (previousLeft >= wallRight && playerLeft <= wallRight) {
            player.x = wallRight;
            return;
        }

        // Failsafe if slightly embedded.
        float overlapLeft = playerRight - wallLeft;
        float overlapRight = wallRight - playerLeft;
        float overlapBottom = playerTop - wallBottom;
        float overlapTop = wallTop - playerBottom;

        float smallestOverlap = Math.min(
                Math.min(overlapLeft, overlapRight),
                Math.min(overlapBottom, overlapTop)
        );

        if (smallestOverlap == overlapLeft) {
            player.x = wallLeft - player.width;
        } else if (smallestOverlap == overlapRight) {
            player.x = wallRight;
        } else if (smallestOverlap == overlapTop) {
            player.y = wallTop;
            player.velocityY = 0;
            player.onGround = true;
        } else {
            player.y = wallBottom - player.height;
            player.velocityY = 0;
        }
    }

    public boolean playerInside(Player player) {
        return Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );
    }

    public boolean playerWasInside(Player player) {
        return Collision.overlaps(
                player.previousX, player.previousY, player.width, player.height,
                x, y, width, height
        );
    }

    public void draw(ShapeRenderer shapeRenderer, Player player) {
        if (spritesLoaded) {
            return;
        }

        if (player.emotion == Emotion.SURPRISE) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.GRAY);
        }

        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch, Player player) {
        if (!spritesLoaded) {
            return;
        }

        Texture texture = player.emotion == Emotion.SURPRISE ? surpriseWallTexture : normalWallTexture;
        batch.draw(texture, x, y, width, height);
    }
}
