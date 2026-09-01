package com.letsfaceittogether.objects;

import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;

public class FanButton extends BaseButton {
    public FanButton(float x, float y, float width, float height) {
        super(
            x,
            y,
            "assets/sprites/objects/Buttons/Button (Fan) (Unpushed).png",
            "fan"
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
            System.out.println("Fan button pressed!");
        }
    }
}


