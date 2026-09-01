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

public class TimerStopButton {
    public float x;
    public float y;
    public float width;
    public float height;

    public boolean pressed = false;

    private Texture unpressedTexture;
    private Texture pressedTexture;
    private boolean loaded = false;

    public TimerStopButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        try {
            unpressedTexture = new Texture("assets/sprites/objects/Buttons/Button (unpushed).png");
            pressedTexture = new Texture("assets/sprites/objects/Buttons/Button (Pushed).png");
            unpressedTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            pressedTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            loaded = true;
            System.out.println("Loaded timer stop button sprites.");
        } catch (GdxRuntimeException e) {
            loaded = false;
            System.out.println("Could not load timer stop button sprites.");
        }
    }

    private float getTopSurfaceY() {
        return pressed ? y + height * 0.38f : y + height * 0.62f;
    }

    private boolean playerStandingOnTop(Player player) {
        float top = getTopSurfaceY();
        float centerX = player.x + player.width / 2.0f;

        boolean centerOverButton =
            centerX >= x &&
            centerX <= x + width;

        boolean feetNearTop =
            player.y >= top - 8.0f &&
            player.y <= top + 8.0f;

        return centerOverButton && feetNearTop && player.onGround;
    }

    public void update(Player player) {
        if (pressed) return;

        if (playerStandingOnTop(player) && player.emotion == Emotion.HAPPY) {
            pressed = true;
            System.out.println("Timer button pressed!");
        }
    }

    public void blockPlayer(Player player) {
        boolean touchingPlayer = Collision.overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, height
        );

        if (!touchingPlayer) return;

        float top = getTopSurfaceY();

        boolean landingOnButton =
            player.velocityY <= 0.0f &&
            player.previousY >= top &&
            player.y <= top;

        if (landingOnButton) {
            player.y = top;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.onGround = true;
            player.setGroundSupport(x, x + width, top);
        }
    }

    public boolean isPressed() {
        return pressed;
    }

    public void reset() {
        pressed = false;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (loaded) return;

        if (pressed) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(x, y, width, height * 0.6f);
        } else {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(x, y, width, height);
        }
    }

    public void draw(SpriteBatch batch) {
        if (!loaded) return;

        Texture texture = pressed ? pressedTexture : unpressedTexture;
        batch.draw(texture, x, y, width, height);
    }
}
