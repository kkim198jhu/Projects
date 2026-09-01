package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cauldron;
import com.letsfaceittogether.objects.CauldronHeatPlatform;
import com.letsfaceittogether.objects.CauldronLiftPlatform;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.objects.SurpriseBlock;

public class TestLevel extends BaseLevel {
    private Cauldron cauldron;
    private CauldronHeatPlatform heatZone;
    private CauldronLiftPlatform liftPlatform;

    private SurpriseBlock leftLowSurpriseBlock;
    private SurpriseBlock leftMidSurpriseBlock;
    private SurpriseBlock leftHighSurpriseBlock;
    private SurpriseBlock rightSurpriseBlock;

    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Use Surprise blocks to jump around. Jump into the cauldron, stand inside, and use Angry to lift the platform.");

        levelWidth = 1300;
        player.worldWidth = levelWidth;

        player.x = 120;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        // Cauldron near the middle.
        cauldron = new Cauldron(470, 90, 120, 105);

        // Inner cauldron platform.
        // Top = 139 + 8 = 147.
        heatZone = new CauldronHeatPlatform(496, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Lower start height so it is easier to test.
        liftPlatform = new CauldronLiftPlatform(455, 215, 150, 28, 250);

        // Surprise blocks: only solid/visible in Surprise mode.
        // Lowered so you can actually jump onto them.
        leftLowSurpriseBlock = new SurpriseBlock(220, 125, 150, 28);
        leftMidSurpriseBlock = new SurpriseBlock(305, 185, 150, 28);
        leftHighSurpriseBlock = new SurpriseBlock(355, 245, 150, 28);

        // Right-side landing/test block.
        rightSurpriseBlock = new SurpriseBlock(690, 245, 170, 28);

        // Upper target ledge remains solid so you have a final goal.
        upperGoalBlock = new SolidWall(820, 485, 180, 80);

        key.x = 885;
        key.y = 575;

        door.x = 1150;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(1.0f, 0.902f, 0.910f, 1.0f); // #ffe6e8
    }

    @Override
    protected void updateLevel(float delta) {
        // Surprise blocks only collide while player is in Surprise mode.
        leftLowSurpriseBlock.blockPlayer(player);
        leftMidSurpriseBlock.blockPlayer(player);
        leftHighSurpriseBlock.blockPlayer(player);
        rightSurpriseBlock.blockPlayer(player);

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
        // Steam/heat visually goes from above the cauldron toward the floating platform.
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Debug-visible inner platform for testing.
        heatZone.drawDebug(shapeRenderer);

        // Surprise blocks draw only while in Surprise mode.
        leftLowSurpriseBlock.draw(shapeRenderer, player);
        leftMidSurpriseBlock.draw(shapeRenderer, player);
        leftHighSurpriseBlock.draw(shapeRenderer, player);
        rightSurpriseBlock.draw(shapeRenderer, player);

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Draw cauldron behind player by default.
        cauldron.draw(batch);

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
