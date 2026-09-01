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

    private SurpriseWall leftGate;
    private SurpriseWall rightGate;

    private SolidWall leftJumpBlock;
    private SolidWall rightJumpBlock;
    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Use Surprise to pass through gates. Use Angry inside the cauldron to lift the platform.");

        levelWidth = 1300;
        player.worldWidth = levelWidth;

        player.x = 120;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        // Cauldron near the middle.
        cauldron = new Cauldron(500, 90, 120, 105);

        // Inner cauldron platform.
        // Top = 139 + 8 = 147.
        heatZone = new CauldronHeatPlatform(526, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Cauldron center = 500 + 60 = 560.
        // Platform width = 150, so x = 560 - 75 = 485.
        liftPlatform = new CauldronLiftPlatform(485, 210, 150, 28, 250);

        // Proper SurpriseWall usage:
        // These are vertical gates.
        // In normal emotions, they are solid.
        // In Surprise, you can pass through them.
        leftGate = new SurpriseWall(360, 90, 45, 120);
        rightGate = new SurpriseWall(710, 90, 45, 120);

        // Normal test blocks for jumping onto / testing platform routes.
        // These are NOT surprise walls.
        leftJumpBlock = new SolidWall(260, 185, 130, 28);
        rightJumpBlock = new SolidWall(780, 185, 130, 28);

        // Upper target ledge.
        upperGoalBlock = new SolidWall(860, 455, 180, 80);

        key.x = 925;
        key.y = 545;

        door.x = 1150;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(1.0f, 0.902f, 0.910f, 1.0f); // #ffe6e8
    }

    @Override
    protected void updateLevel(float delta) {
        // Surprise gates:
        // solid normally, pass-through only in Surprise.
        leftGate.blockPlayer(player);
        rightGate.blockPlayer(player);

        // Normal jump blocks.
        leftJumpBlock.blockPlayer(player);
        rightJumpBlock.blockPlayer(player);

        // Inner cauldron platform catches player from the top only.
        heatZone.blockPlayer(player);

        standingInHeatZone = heatZone.isPlayerStandingOn(player);

        // Cauldron only heats when standing inside + Angry.
        cauldron.update(delta, player, standingInHeatZone);

        // Floating platform rises with cauldron heat.
        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);

        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Debug-visible inner platform.
        heatZone.drawDebug(shapeRenderer);

        // Shape fallback only.
        leftGate.draw(shapeRenderer, player);
        rightGate.draw(shapeRenderer, player);

        liftPlatform.draw(shapeRenderer);
        leftJumpBlock.draw(shapeRenderer);
        rightJumpBlock.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Cauldron behind player by default.
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        // Sprite version of surprise gates.
        leftGate.draw(batch, player);
        rightGate.draw(batch, player);

        leftJumpBlock.draw(batch);
        rightJumpBlock.draw(batch);
        upperGoalBlock.draw(batch);
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        // Draw cauldron in front while airborne over it or standing inside.
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingInHeatZone)) {
            cauldron.draw(batch);
        }
    }
}
