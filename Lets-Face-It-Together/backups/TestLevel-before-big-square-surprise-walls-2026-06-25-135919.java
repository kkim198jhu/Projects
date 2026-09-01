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

        setTutorialText("TEST LEVEL: Square SurpriseWall blocks stack like normal blocks. Use them to reach the cauldron.");

        levelWidth = 1300;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        float block = 48f;
        float groundY = 90f;

        // Cauldron.
        cauldron = new Cauldron(650, 90, 120, 105);

        // Inner cauldron platform.
        heatZone = new CauldronHeatPlatform(676, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Cauldron center = 650 + 60 = 710.
        // Platform width = 150, so x = 710 - 75 = 635.
        liftPlatform = new CauldronLiftPlatform(635, 210, 150, 28, 250);

        // Every SurpriseWall tile is the same exact size: 48 x 48.
        // They are stacked directly on top of each other like the example level.
        //
        // Layout:
        //   stack 1 = 1 block high
        //   stack 2 = 2 blocks high
        //   stack 3 = 3 blocks high
        //   stack 4 = 2 blocks high next to cauldron
        leadingBlocks = new SurpriseWall[] {
            // 1-block-high stack
            new SurpriseWall(250, groundY, block, block),

            // 2-block-high stack
            new SurpriseWall(340, groundY, block, block),
            new SurpriseWall(340, groundY + block, block, block),

            // 3-block-high stack
            new SurpriseWall(430, groundY, block, block),
            new SurpriseWall(430, groundY + block, block, block),
            new SurpriseWall(430, groundY + block * 2f, block, block),

            // 2-block-high stack closer to cauldron
            new SurpriseWall(520, groundY, block, block),
            new SurpriseWall(520, groundY + block, block, block)
        };

        // Upper target ledge remains solid.
        upperGoalBlock = new SolidWall(900, 455, 180, 80);

        key.x = 965;
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
        // Square SurpriseWall tiles.
        // Solid normally, pass-through in Surprise.
        for (SurpriseWall block : leadingBlocks) {
            block.blockPlayer(player);
        }

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

        // Shape fallback for square blocks.
        for (SurpriseWall block : leadingBlocks) {
            block.draw(shapeRenderer, player);
        }

        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Cauldron behind player by default.
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        // Sprite version of square SurpriseWall tiles.
        for (SurpriseWall block : leadingBlocks) {
            block.draw(batch, player);
        }

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
