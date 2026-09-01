package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cauldron;
import com.letsfaceittogether.objects.CauldronHeatPlatform;
import com.letsfaceittogether.objects.CauldronLiftPlatform;
import com.letsfaceittogether.objects.SolidWall;

public class TestLevel extends BaseLevel {
    private Cauldron cauldron;
    private CauldronHeatPlatform heatPlatform;
    private CauldronLiftPlatform liftPlatform;
    private SolidWall upperGoalBlock;

    private boolean standingOnHeatPlatform = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Jump into the cauldron onto the inner platform. Angry heats it.");

        levelWidth = 1200;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        cauldron = new Cauldron(160, 90, 120, 105);

        // Small top-only platform inside the cauldron bowl.
        // Cauldron x=160, width=120. Center is 220.
        // Platform width=62, so x=189.
        heatPlatform = new CauldronHeatPlatform(184, 112, 72, 60);

        // Platform centered above cauldron. Steam/heat lifts it.
        liftPlatform = new CauldronLiftPlatform(145, 245, 150, 28, 250);

        upperGoalBlock = new SolidWall(660, 500, 160, 80);

        key.x = 715;
        key.y = 590;

        door.x = 1050;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        // #ffe6e8
        return new Color(1.0f, 0.902f, 0.910f, 1.0f);
    }

    @Override
    protected void updateLevel(float delta) {
        // Never call cauldron.blockPlayer(player). Cauldron itself is non-solid.

        // Inner cauldron platform is a non-solid heat zone.
        // Do not call heatPlatform.blockPlayer(player), because that can snap/teleport the player.
        standingOnHeatPlatform = heatPlatform.isPlayerStandingOn(player);

        cauldron.update(delta, player, standingOnHeatPlatform);

        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);

        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Draw debug platform behind the cauldron. It is intentionally small.
        heatPlatform.drawDebug(shapeRenderer);

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Always draw cauldron behind the player first.
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);
        upperGoalBlock.draw(batch);
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        // Draw cauldron in front only when player is airborne over it
        // or standing on the inner heat platform.
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingOnHeatPlatform)) {
            cauldron.draw(batch);
        }
    }
}
