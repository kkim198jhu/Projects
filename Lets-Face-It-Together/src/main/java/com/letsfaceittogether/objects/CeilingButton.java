package com.letsfaceittogether.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.utils.Collision;

public class CeilingButton extends BaseButton {
    private boolean reversible;

    public CeilingButton(float x, float y, String unpressedPath, String buttonName, boolean reversible) {
        super(x, y, unpressedPath, buttonName);
        this.reversible = reversible;
    }

    public void update(Player player) {
        update(player, (MovableBox[]) null);
    }

    public void update(Player player, MovableBox... boxes) {
        boolean playerPressing =
            playerStandingOnUnderside(player) &&
            player.emotion == Emotion.HAPPY;

        boolean boxPressing = anyBoxStandingOnButton(boxes);

        if (reversible) {
            // Reversible ceiling button pops back up when nothing is pressing it.
            pressed = playerPressing || boxPressing;
            return;
        }

        // Permanent ceiling buttons stay pressed once activated.
        if (!pressed && (playerPressing || boxPressing)) {
            pressed = true;
            System.out.println(buttonName + " ceiling button pressed!");
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

        // Draw upside down because ceiling buttons are mounted under blocks.
        batch.draw(texture, x, y + height, width, -height);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        super.draw(shapeRenderer);
    }
}
