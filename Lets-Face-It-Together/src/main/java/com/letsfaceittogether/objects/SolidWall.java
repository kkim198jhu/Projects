package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class SolidWall {
    public float x;
    public float y;
    public float width;
    public float height;

    private boolean removed = false;

    private Texture wallTexture;
    private boolean spriteLoaded = false;

    public SolidWall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            wallTexture = new Texture("assets/sprites/objects/Walls/Wall.png");
            spriteLoaded = true;
            System.out.println("Loaded solid wall sprite.");
        } catch (GdxRuntimeException e) {
            spriteLoaded = false;
            System.out.println("Could not load solid wall sprite. Using placeholder.");
        }
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void blockPlayer(Player player) {
    if (removed) {
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

        // Register this block as real ground support so jumping works
        // while standing on it, but walking off it clears jump support.
        player.setGroundSupport(wallLeft, wallRight, player.y);
        return;
    }

    // Head bump.
    // In inverted gravity, this becomes the "floor" you walk on.
    if (wasBelowWall) {
        player.y = wallBottom - player.height;
        player.previousY = player.y;
        player.velocityY = 0;

        if (player.gravityInverted) {
            player.setGroundSupport(wallLeft, wallRight, player.y);
        }

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
    // This prevents getting glued to tall stacked walls.
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
                previousRight > wallLeft + 8 &&
                previousLeft < wallRight - 8;

        boolean isHorizontallyOverWall =
                playerRight > wallLeft + 8 &&
                playerLeft < wallRight - 8;

        // Land on top only if truly above the wall, not pressing into the side.
        if (wasHorizontallyOverWall &&
            isHorizontallyOverWall &&
            player.velocityY <= 0 &&
            previousBottom >= wallTop &&
            playerBottom <= wallTop) {

            player.y = wallTop;
            player.velocityY = 0;
            player.setGroundSupport(x, x + width, player.y);
            return;
        }

        // Hit underside only if truly below the wall.
        if (wasHorizontallyOverWall &&
            isHorizontallyOverWall &&
            player.velocityY > 0 &&
            previousTop <= wallBottom &&
            playerTop >= wallBottom) {

            player.y = wallBottom - player.height;
            player.velocityY = 0;
            return;
        }

        // Strong side priority.
        // This prevents wall sides from acting like tiny floors.
        float playerCenterX = player.x + player.width / 2f;
        float wallCenterX = x + width / 2f;

        if (playerCenterX < wallCenterX) {
            player.x = wallLeft - player.width;
        } else {
            player.x = wallRight;
        }

        // Do NOT set onGround here. Side collisions should not allow jumping.
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (removed || spriteLoaded) {
            return;
        }

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (removed || !spriteLoaded) {
            return;
        }

        drawTiled(batch, wallTexture, x, y, width, height);
    }

    private void drawTiled(SpriteBatch batch, Texture texture, float startX, float startY, float totalW, float totalH) {
        float blockW = 100f;
        float blockH = 95f;

        int rows = Math.max(1, Math.round(totalH / blockH));
        float drawX = startX + (totalW - blockW) / 2f;

        for (int row = 0; row < rows; row++) {
            float drawY = startY + row * blockH;
            batch.draw(texture, drawX, drawY, blockW, blockH);
        }
    }
}
