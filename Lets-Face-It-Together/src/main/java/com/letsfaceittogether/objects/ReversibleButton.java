package com.letsfaceittogether.objects;

import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.Player;

public class ReversibleButton extends BaseButton {
    public ReversibleButton(float x, float y, float width, float height) {
        super(
            x,
            y,
            "assets/sprites/objects/Buttons/Button (Reversible) (Unpushed).png",
            "reversible"
        );
    }

    public void update(Player player) {
        update(player, (MovableBox[]) null);
    }

    public void update(Player player, MovableBox... boxes) {
        boolean playerPressing =
            playerStandingOnTop(player) &&
            player.emotion == Emotion.HAPPY;

        boolean boxPressing = anyBoxStandingOnButton(boxes);

        // Reversible: down only while something valid is pressing it.
        pressed = playerPressing || boxPressing;
    }
}

