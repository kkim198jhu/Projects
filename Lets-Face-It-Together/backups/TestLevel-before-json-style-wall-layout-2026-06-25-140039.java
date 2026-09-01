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

    private SurpriseWall[] leadingBlocks;

    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Big square SurpriseWall blocks now act like normal jumpable blocks. Angry inside the cauldron lifts the platform.");

        levelWidth = 1500;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        float block = 100f;
        float groundY = 90f;

        // Cauldron.
        cauldron = new Cauldron(760, 90, 120, 105);

        // Inner cauldron platform.
        heatZone = new CauldronHeatPlatform(786, 139, 68, 8);

        // Floating platform centered above cauldron.
        liftPlatform = new CauldronLiftPlatform(745, 210, 150, 28, 250);

        // All blocks are exactly 100 x 100.
        // They stack directly on top of each other.
        //
        // Layout:
        // 1 high -> 2 high -> 3 high -> cauldron
        leadingBlocks = new SurpriseWall[] {
            // 1-block-high stack
            new SurpriseWall(240, groundY, block, block),

            // 2-block-high stack
            new SurpriseWall(390, groundY, block, block),
            new SurpriseWall(390, groundY + block, block, block),

            // 3-block-high stack
            new SurpriseWall(540, groundY, block, block),
            new SurpriseWall(540, groundY + block, block, block),
            new SurpriseWall(540, groundY + block * 2f, block, block),

            // 1 block near cauldron so the approach is easier
            new SurpriseWall(650, groundY, block, block)
        };

        upperGoalBlock = new SolidWall(980, 455, 180, 80);

        key.x = 1045;
        key.y = 545;

        door.x = 1320;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(1.0f, 0.902f, 0.910f, 1.0f); // #ffe6e8
    }

    @Override
    protected void updateLevel(float delta) {
        for (SurpriseWall block : leadingBlocks) {
            block.blockPlayer(player);
        }

        heatZone.blockPlayer(player);
        standingInHeatZone = heatZone.isPlayerStandingOn(player);

        cauldron.update(delta, player, standingInHeatZone);

        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);

        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        heatZone.drawDebug(shapeRenderer);

        for (SurpriseWall block : leadingBlocks) {
            block.draw(shapeRenderer, player);
        }

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        for (SurpriseWall block : leadingBlocks) {
            block.draw(batch, player);
        }

        upperGoalBlock.draw(batch);
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingInHeatZone)) {
            cauldron.draw(batch);
        }
    }
}
