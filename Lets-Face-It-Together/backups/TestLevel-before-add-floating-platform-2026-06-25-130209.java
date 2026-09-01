package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cauldron;
import com.letsfaceittogether.objects.CauldronHeatPlatform;

public class TestLevel extends BaseLevel {
    private Cauldron cauldron;
    private CauldronHeatPlatform heatZone;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Cauldron isolated. Jump onto the inner platform + Angry to heat.");

        levelWidth = 900;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        cauldron = new Cauldron(220, 90, 120, 105);

        // Non-solid heat zone inside the cauldron.
        // This cannot snap the player because it has no collision.
        heatZone = new CauldronHeatPlatform(246, 139, 68, 8);

        key.x = 620;
        key.y = 140;

        door.x = 760;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(1.0f, 0.902f, 0.910f, 1.0f); // #ffe6e8
    }

    @Override
    protected void updateLevel(float delta) {
        float beforeX = player.x;
        float beforeY = player.y;

        heatZone.blockPlayer(player);
        standingInHeatZone = heatZone.isPlayerStandingOn(player);
        cauldron.update(delta, player, standingInHeatZone);

        float movedX = Math.abs(player.x - beforeX);
        float movedY = Math.abs(player.y - beforeY);

        if (movedX > 20.0f || movedY > 20.0f) {
            System.out.println("SNAP DURING TestLevel.updateLevel: before=(" + beforeX + "," + beforeY + ") after=(" + player.x + "," + player.y + ")");
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        heatZone.drawDebug(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Behind player by default.
        cauldron.draw(batch);
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        // In front only when airborne over cauldron or inside heat zone.
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingInHeatZone)) {
            cauldron.draw(batch);
        }
    }
}
