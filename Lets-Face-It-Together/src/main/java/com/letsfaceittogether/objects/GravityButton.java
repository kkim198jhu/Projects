package com.letsfaceittogether.objects;

import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;

public class GravityButton extends BaseButton {
    public GravityButton(float x, float y, float width, float height) {
        super(
            x,
            y,
            "assets/sprites/objects/Buttons/Button (Gravity) (Unpushed).png",
            "gravity"
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
            playerStandingOnTop(player) &&
            player.emotion == Emotion.HAPPY;

        boolean boxPressing = anyBoxStandingOnButton(boxes);

        if (playerPressing || boxPressing) {
            pressed = true;
            System.out.println("Gravity button pressed!");
        }
    }
}


