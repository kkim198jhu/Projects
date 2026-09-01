package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class CeilingGravityButton extends BaseButton {
    public CeilingGravityButton(float x, float y, float width, float height) {
        super(
            x,
            y,
            "assets/sprites/objects/Buttons/Button (Gravity) (Unpushed).png",
            "ceiling gravity"
        );
    }

    public void update(Player player) {
        update(player, (MovableBox[]) null);
    }

    public void update(Player player, MovableBox... boxes) {
        if (pressed) {
            return;
        }

        boolean playerPressing =
            playerStandingOnUnderside(player) &&
            player.emotion == Emotion.HAPPY;

        boolean boxPressing = anyBoxStandingOnButton(boxes);

        if (playerPressing || boxPressing) {
            pressed = true;
            System.out.println("Ceiling gravity button pressed!");
        }
    }

    @Override
    public void blockPlayer(Player player) {
        boolean touchingPlayer = Collision.overlaps(
            player.x, player.y, player.width, player.height,
            x, y, width, height
        );

        if (!touchingPlayer) {
            return;
        }

        float buttonBottom = y;

        boolean landingOnButtonFromBelow =
            player.gravityInverted &&
            player.velocityY >= 0.0f &&
            player.previousY + player.height <= buttonBottom &&
            player.y + player.height >= buttonBottom;

        if (landingOnButtonFromBelow) {
            player.y = buttonBottom - player.height;
            player.previousY = player.y;
            player.velocityY = 0.0f;
            player.setGroundSupport(x, x + width, player.y);
        }
    }

    private boolean playerStandingOnUnderside(Player player) {
        float playerCenterX = player.x + player.width / 2.0f;
        float playerTopY = player.y + player.height;

        boolean centerOverButton =
            playerCenterX >= x &&
            playerCenterX <= x + width;

        boolean headAtButtonBottom =
            playerTopY >= y - 10.0f &&
            playerTopY <= y + 10.0f;

        return player.gravityInverted && centerOverButton && headAtButtonBottom && player.onGround;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!spritesLoaded) {
            return;
        }

        Texture texture = pressed ? pressedTexture : unpressedTexture;

        // Draw upside down because this button is mounted on the ceiling.
        batch.draw(texture, x, y + height, width, -height);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        super.draw(shapeRenderer);
    }
}
