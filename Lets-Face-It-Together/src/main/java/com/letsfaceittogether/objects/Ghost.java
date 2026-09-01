package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class Ghost {
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean caged;

    private float startX;
    private float startY;

    private float chaseSpeed = 190;
    private float cageMoveSpeed = 70;

    private Texture ghostTexture;
    private TextureRegion ghostLeft;
    private TextureRegion ghostRight;
    private boolean spriteLoaded = false;

    public Ghost(float x, float y) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.width = 45;
        this.height = 60;
        this.caged = false;

        try {
            ghostTexture = new Texture("assets/sprites/objects/Enemies/Ghost (Looking Left).png");

            ghostLeft = new TextureRegion(ghostTexture);
            ghostRight = new TextureRegion(ghostTexture);
            ghostRight.flip(true, false);

            spriteLoaded = true;
            System.out.println("Loaded ghost sprite.");
        } catch (GdxRuntimeException e) {
            spriteLoaded = false;
            System.out.println("Could not load ghost sprite. Using placeholder.");
        }
    }

    public void update(float delta, Player player, Cage cage) {
        if (caged) {
            return;
        }

        boolean playerIsDisgusted = player.emotion == Emotion.DISGUST;
        boolean playerLookingAtGhost = isPlayerLookingAtGhost(player);

        if (playerIsDisgusted && playerLookingAtGhost) {
            moveTowardCage(delta, cage);
        } else {
            moveTowardPlayer(delta, player);
        }

        boolean playerTouchingGhost = Collision.overlaps(
                player.x, player.y, player.width, player.height,
                x, y, width, height
        );

        if (playerTouchingGhost) {
            resetPlayer(player);
            resetGhost();
            System.out.println("The ghost caught you! Player and ghost reset.");
        }

        keepOnScreen();
    }

    private boolean isPlayerLookingAtGhost(Player player) {
        float playerCenterX = player.x + player.width / 2;
        float ghostCenterX = x + width / 2;

        boolean ghostIsToRight = ghostCenterX > playerCenterX;
        boolean ghostIsToLeft = ghostCenterX < playerCenterX;

        return (ghostIsToRight && player.facingDirection == 1) ||
               (ghostIsToLeft && player.facingDirection == -1);
    }

    private void moveTowardPlayer(float delta, Player player) {
        float targetX = player.x + player.width / 2;
        float targetY = player.y + player.height / 2;

        moveTowardPoint(delta, targetX, targetY, chaseSpeed);
    }

    private void moveTowardCage(float delta, Cage cage) {
        float cageCenterX = cage.x + cage.width / 2;
        float cageCenterY = cage.y + cage.height / 2;

        moveTowardPoint(delta, cageCenterX, cageCenterY, cageMoveSpeed);
    }

    private void moveTowardPoint(float delta, float targetX, float targetY, float speed) {
        float ghostCenterX = x + width / 2;
        float ghostCenterY = y + height / 2;

        float dx = targetX - ghostCenterX;
        float dy = targetY - ghostCenterY;

        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if (length == 0) {
            return;
        }

        dx /= length;
        dy /= length;

        x += dx * speed * delta;
        y += dy * speed * delta;
    }

    private void resetPlayer(Player player) {
        player.x = 100;
        player.y = 90;
        player.velocityY = 0;
        player.onGround = true;
    }

    private void resetGhost() {
        x = startX;
        y = startY;
    }

    private void keepOnScreen() {
        if (x < 0) x = 0;
        if (x + width > 960) x = 960 - width;
        if (y < 90) y = 90;
        if (y + height > 540) y = 540 - height;
    }

    public boolean isInsideCage(Cage cage) {
        return Collision.overlaps(x, y, width, height, cage.x, cage.y, cage.width, cage.height);
    }

    public void putInCage(Cage cage) {
        caged = true;
        x = cage.x + cage.width / 2 - width / 2;
        y = cage.y + cage.height / 2 - height / 2;
        System.out.println("Ghost trapped in cage!");
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (spriteLoaded) {
            return;
        }

        if (caged) {
            shapeRenderer.setColor(new Color(0.8f, 0.8f, 0.8f, 0.45f));
        } else {
            shapeRenderer.setColor(new Color(1f, 1f, 1f, 0.35f));
        }

        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.circle(x + 13, y + 42, 5);
        shapeRenderer.circle(x + 32, y + 42, 5);
    }

    public void draw(SpriteBatch batch) {
        if (!spriteLoaded) {
            return;
        }

        TextureRegion currentFrame = ghostLeft;

        if (caged) {
            batch.setColor(1f, 1f, 1f, 0.45f);
        } else {
            batch.setColor(1f, 1f, 1f, 0.72f);
        }

        float visualWidth;
        float visualHeight;
        float visualY;

        if (caged) {
            visualWidth = 70;
            visualHeight = 70;
            visualY = y - 2;
        } else {
            visualWidth = 95;
            visualHeight = 95;
            visualY = y - 10;
        }

        float visualX = x + width / 2f - visualWidth / 2f;

        batch.draw(currentFrame, visualX, visualY, visualWidth, visualHeight);

        batch.setColor(1f, 1f, 1f, 1f);
    }
}
