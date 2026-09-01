package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cauldron;
import com.letsfaceittogether.objects.CauldronHeatPlatform;
import com.letsfaceittogether.objects.CauldronLiftPlatform;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.objects.SurpriseWall;

public class TestLevel extends BaseLevel {
    private Cauldron cauldron;
    private CauldronHeatPlatform heatZone;
    private CauldronLiftPlatform liftPlatform;

    private SurpriseWall leftLowWall;
    private SurpriseWall leftMidWall;
    private SurpriseWall leftHighWall;
    private SurpriseWall rightWall;

    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Surprise lets you pass through walls. Other emotions can jump on them. Angry heats the cauldron.");

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
        // Starts lower so it is easier to test.
        liftPlatform = new CauldronLiftPlatform(455, 215, 150, 28, 250);

        // Surprise walls:
        // In normal emotions, these are solid and can be jumped on.
        // In Surprise, these become pass-through.
        // Lowered so the jumps are reachable.
        leftLowWall = new SurpriseWall(215, 105, 145, 32);
        leftMidWall = new SurpriseWall(300, 165, 145, 32);
        leftHighWall = new SurpriseWall(360, 225, 145, 32);

        // Right-side test wall.
        rightWall = new SurpriseWall(680, 225, 170, 32);

        // Upper target ledge remains solid.
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
        // Surprise walls are solid unless the player is in Surprise mode.
        leftLowWall.blockPlayer(player);
        leftMidWall.blockPlayer(player);
        leftHighWall.blockPlayer(player);
        rightWall.blockPlayer(player);

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
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Debug-visible inner platform for testing.
        heatZone.drawDebug(shapeRenderer);

        // Shape fallback only. If sprites load, these do nothing here.
        leftLowWall.draw(shapeRenderer, player);
        leftMidWall.draw(shapeRenderer, player);
        leftHighWall.draw(shapeRenderer, player);
        rightWall.draw(shapeRenderer, player);

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Draw cauldron behind player by default.
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        // Sprite version of SurpriseWall.
        leftLowWall.draw(batch, player);
        leftMidWall.draw(batch, player);
        leftHighWall.draw(batch, player);
        rightWall.draw(batch, player);

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
