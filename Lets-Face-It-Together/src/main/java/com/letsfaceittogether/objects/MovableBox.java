package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class MovableBox {
    public float x;
    public float y;
    public float width;
    public float height;

    public float previousX;
    public float previousY;
    public float velocityY = 0.0f;

    private boolean gravityInverted = false;
    private boolean active = true;

    private final float gravity = -1200.0f;
    private final float floorY = 90.0f;

    private Texture boxTexture;
    private boolean spriteLoaded = false;

    public MovableBox(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.previousX = x;
        this.previousY = y;

        this.width = width;
        this.height = height;

        try {
            boxTexture = new Texture("assets/sprites/objects/Box/Box.png");
            boxTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            spriteLoaded = true;
            System.out.println("Loaded movable box sprite.");
        } catch (GdxRuntimeException e) {
            spriteLoaded = false;
            System.out.println("Could not load movable box sprite. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
        velocityY = 0.0f;
    }

    public void setGravityInverted(boolean inverted) {
        if (gravityInverted != inverted) {
            gravityInverted = inverted;
            velocityY = 0.0f;
        }
    }

    public boolean isGravityInverted() {
        return gravityInverted;
    }

    public void update(float delta, float worldWidth) {
        if (!active) {
            return;
        }

        previousX = x;
        previousY = y;

        float effectiveGravity = gravityInverted ? -gravity : gravity;

        velocityY += effectiveGravity * delta;
        y += velocityY * delta;

        // Normal floor support.
        if (!gravityInverted && y <= floorY) {
            y = floorY;
            velocityY = 0.0f;
        }

        if (x < 0.0f) {
            x = 0.0f;
        }

        if (x + width > worldWidth) {
            x = worldWidth - width;
        }
    }

    public void blockAgainstWall(SolidWall wall) {
        if (!active) {
            return;
        }

        if (wall == null || wall.isRemoved()) {
            return;
        }

        if (!Collision.overlaps(x, y, width, height, wall.x, wall.y, wall.width, wall.height)) {
            return;
        }

        float wallTop = wall.y + wall.height;
        float wallBottom = wall.y;
        float wallLeft = wall.x;
        float wallRight = wall.x + wall.width;

        float previousBottom = previousY;
        float previousTop = previousY + height;
        float previousLeft = previousX;
        float previousRight = previousX + width;

        boolean wasAboveWall = previousBottom >= wallTop - 6.0f;
        boolean wasBelowWall = previousTop <= wallBottom + 6.0f;
        boolean wasLeftOfWall = previousRight <= wallLeft + 4.0f;
        boolean wasRightOfWall = previousLeft >= wallRight - 4.0f;

        // Normal gravity: land on top of blocks.
        if (!gravityInverted && wasAboveWall && velocityY <= 0.0f) {
            y = wallTop;
            previousY = y;
            velocityY = 0.0f;
            return;
        }

        // Inverted gravity: land on underside of blocks.
        if (gravityInverted && wasBelowWall && velocityY >= 0.0f) {
            y = wallBottom - height;
            previousY = y;
            velocityY = 0.0f;
            return;
        }

        // Side collision with blocks.
        if (wasLeftOfWall) {
            x = wallLeft - width;
            previousX = x;
            return;
        }

        if (wasRightOfWall) {
            x = wallRight;
            previousX = x;
            return;
        }

        // Fallback side collision.
        float boxCenterX = x + width / 2.0f;
        float wallCenterX = wall.x + wall.width / 2.0f;

        if (boxCenterX < wallCenterX) {
            x = wallLeft - width;
        } else {
            x = wallRight;
        }

        previousX = x;
    }

    public boolean isCrushing(Player player) {
        if (!active) {
            return false;
        }

        float boxLeft = x;
        float boxRight = x + width;
        float boxBottom = y;
        float boxTop = y + height;

        float previousBoxBottom = previousY;
        float previousBoxTop = previousY + height;

        float playerLeft = player.x;
        float playerRight = player.x + player.width;
        float playerBottom = player.y;
        float playerTop = player.y + player.height;

        boolean horizontallyOverlapping =
            boxRight > playerLeft + 8.0f &&
            boxLeft < playerRight - 8.0f;

        if (!horizontallyOverlapping) {
            return false;
        }

        if (!gravityInverted) {
            // Normal gravity: box falls downward onto player's head.
            boolean fallingDown = velocityY < -20.0f;
            boolean crossedPlayerTop =
                previousBoxBottom >= playerTop - 4.0f &&
                boxBottom <= playerTop;

            return fallingDown && crossedPlayerTop;
        } else {
            // Inverted gravity: box falls upward into player's feet/body.
            boolean fallingUp = velocityY > 20.0f;
            boolean crossedPlayerBottom =
                previousBoxTop <= playerBottom + 4.0f &&
                boxTop >= playerBottom;

            return fallingUp && crossedPlayerBottom;
        }
    }

    public void resetPosition(float newX, float newY) {
        active = true;
        x = newX;
        y = newY;
        previousX = newX;
        previousY = newY;
        velocityY = 0.0f;
        gravityInverted = false;
    }

    public void blockPlayer(Player player) {
        if (!active) {
            return;
        }

        if (!Collision.overlaps(player.x, player.y, player.width, player.height, x, y, width, height)) {
            return;
        }

        float boxTop = y + height;
        float boxBottom = y;
        float boxLeft = x;
        float boxRight = x + width;

        float previousBottom = player.previousY;
        float previousTop = player.previousY + player.height;
        float previousLeft = player.previousX;
        float previousRight = player.previousX + player.width;

        boolean wasAboveBox = previousBottom >= boxTop - 6.0f;
        boolean wasBelowBox = previousTop <= boxBottom + 4.0f;
        boolean wasLeftOfBox = previousRight <= boxLeft + 4.0f;
        boolean wasRightOfBox = previousLeft >= boxRight - 4.0f;

        boolean feetAreOnTopSurface = Math.abs(player.y - boxTop) <= 10.0f;
        boolean alreadyStandingOnTop = player.onGround && feetAreOnTopSurface;

        // Player stands on top of box in normal gravity.
        if (!player.gravityInverted && (wasAboveBox || alreadyStandingOnTop)) {
            player.y = boxTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
            return;
        }

        // Player stands on underside of box in inverted gravity.
        if (player.gravityInverted && wasBelowBox) {
            player.y = boxBottom - player.height;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
            return;
        }

        // Side push / side block.
        if (wasLeftOfBox) {
            handleSideContact(player, 1.0f);
            return;
        }

        if (wasRightOfBox) {
            handleSideContact(player, -1.0f);
            return;
        }

        float playerCenterX = player.x + player.width / 2.0f;
        float boxCenterX = x + width / 2.0f;

        if (playerCenterX < boxCenterX) {
            handleSideContact(player, 1.0f);
        } else {
            handleSideContact(player, -1.0f);
        }
    }

    private void handleSideContact(Player player, float pushDirection) {
        boolean canPush = player.emotion == Emotion.ANGRY;

        if (canPush) {
            player.pushingBox = true;

            float pushAmount = 0.75f;

            if (pushDirection > 0.0f) {
                x += pushAmount;
                player.x = x - player.width;
            } else {
                x -= pushAmount;
                player.x = x + width;
            }

            clampBoxToWorld(player.worldWidth);
            player.previousX = player.x;
            return;
        }

        if (pushDirection > 0.0f) {
            player.x = x - player.width;
        } else {
            player.x = x + width;
        }

        player.previousX = player.x;
    }

    private void clampBoxToWorld(float worldWidth) {
        if (x < 0.0f) {
            x = 0.0f;
        }

        if (x + width > worldWidth) {
            x = worldWidth - width;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (!active) {
            return;
        }

        if (spriteLoaded) {
            return;
        }

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        if (!active) {
            return;
        }

        if (spriteLoaded) {
            if (gravityInverted) {
                batch.draw(boxTexture, x, y + height, width, -height);
            } else {
                batch.draw(boxTexture, x, y, width, height);
            }
        }
    }
}
