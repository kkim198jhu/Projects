package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public abstract class BaseButton {
    public float x;
    public float y;
    public float width;
    public float height;

    public boolean pressed = false;

    protected Texture unpressedTexture;
    protected Texture pressedTexture;
    protected boolean spritesLoaded = false;

    protected String buttonName;

    public BaseButton(float x, float y, String unpressedPath, String buttonName) {
        this.x = x;
        this.y = y;

        // Shared button dimensions.
        this.width = 55.0f;
        this.height = 50.0f;

        this.buttonName = buttonName;

        try {
            unpressedTexture = new Texture(unpressedPath);
            pressedTexture = new Texture("assets/sprites/objects/Buttons/Button (Pushed).png");

            unpressedTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pressedTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

            spritesLoaded = true;
            System.out.println("Loaded " + buttonName + " button sprites.");
        } catch (GdxRuntimeException e) {
            spritesLoaded = false;
            System.out.println("Could not load " + buttonName + " button sprites. Using placeholder.");
            System.out.println(e.getMessage());
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public void reset() {
        pressed = false;
    }

    public void blockPlayer(Player player) {
        boolean touchingPlayer = Collision.overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, height
        );

        if (!touchingPlayer) {
            return;
        }

        float buttonTop = getButtonTop();

        boolean landingOnButton =
            player.velocityY <= 0.0f &&
            player.previousY >= buttonTop &&
            player.y <= buttonTop;

        if (landingOnButton) {
            player.y = buttonTop;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }
    }

    protected float getButtonTop() {
        return pressed ? y + height * 0.38f : y + height * 0.62f;
    }

    protected boolean playerStandingOnTop(Player player) {
        float buttonTop = getButtonTop();

        float playerCenterX = player.x + player.width / 2.0f;
        float playerFeetY = player.y;

        boolean centerOverButton =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean feetOnButtonTop =
            playerFeetY >= buttonTop - 8.0f &&
            playerFeetY <= buttonTop + 10.0f;

        return centerOverButton && feetOnButtonTop && player.onGround;
    }

    protected boolean anyBoxStandingOnButton(MovableBox... boxes) {
        if (boxes == null) {
            return false;
        }

        for (MovableBox box : boxes) {
            if (box != null && box.isActive() && boxStandingOnButton(box)) {
                return true;
            }
        }

        return false;
    }

    protected boolean boxStandingOnButton(MovableBox box) {
        // Boxes use their full square collision area.
        // Any meaningful overlap with the button counts as pressing it.
        float boxLeft = box.x;
        float boxRight = box.x + box.width;
        float boxBottom = box.y;
        float boxTop = box.y + box.height;

        float buttonLeft = x;
        float buttonRight = x + width;
        float buttonBottom = y;
        float buttonTop = y + height;

        boolean horizontallyOverlapping =
            boxRight > buttonLeft + 4.0f &&
            boxLeft < buttonRight - 4.0f;

        boolean verticallyOverlapping =
            boxTop > buttonBottom + 2.0f &&
            boxBottom < buttonTop - 2.0f;

        return horizontallyOverlapping && verticallyOverlapping;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (spritesLoaded) {
            return;
        }

        if (pressed) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(x, y, width, height * 0.6f);
        } else {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) {
            return;
        }

        Texture texture = pressed ? pressedTexture : unpressedTexture;
        batch.draw(texture, x, y, width, height);
    }
}
