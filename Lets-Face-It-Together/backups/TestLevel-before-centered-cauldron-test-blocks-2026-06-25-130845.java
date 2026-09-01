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
    private CauldronHeatPlatform heatZone;
    private CauldronLiftPlatform liftPlatform;
    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Jump into the cauldron, stand on the inner platform, and use Angry to heat it.");

        levelWidth = 1200;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        // Cauldron.
        cauldron = new Cauldron(220, 90, 120, 105);

        // Inner platform inside cauldron.
        // Top = 139 + 8 = 147.
        heatZone = new CauldronHeatPlatform(246, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Cauldron center = 220 + 60 = 280.
        // Platform width = 150, so x = 280 - 75 = 205.
        // Base Y = 245. It rises upward as heat increases.
        liftPlatform = new CauldronLiftPlatform(205, 245, 150, 28, 250);

        // Upper target ledge.
        upperGoalBlock = new SolidWall(660, 500, 160, 80);

        key.x = 715;
        key.y = 590;

        door.x = 1050;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(1.0f, 0.902f, 0.910f, 1.0f); // #ffe6e8
    }

    @Override
    protected void updateLevel(float delta) {
        // Inner cauldron platform catches player from the top only.
        heatZone.blockPlayer(player);

        standingInHeatZone = heatZone.isPlayerStandingOn(player);

        // Cauldron only heats when standing on inner platform + Angry.
        cauldron.update(delta, player, standingInHeatZone);

        // Floating platform rises with cauldron heat.
        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);

        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        // Steam/heat visually goes from cauldron to floating platform.
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Debug-visible inner platform for testing.
        heatZone.drawDebug(shapeRenderer);

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Draw cauldron behind player by default.
        cauldron.draw(batch);

        // Draw steam and floating platform.
        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        upperGoalBlock.draw(batch);
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        // Draw cauldron in front while airborne over it or standing inside,
        // so the player looks like they are inside the bowl.
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingInHeatZone)) {
            cauldron.draw(batch);
        }
    }
}
