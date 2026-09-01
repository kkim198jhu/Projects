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

        setTutorialText("TEST LEVEL: Use the square blocks as steps. Jump into the cauldron, stand inside, and use Angry to lift the platform.");

        levelWidth = 1300;
        player.worldWidth = levelWidth;

        player.x = 120;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        float block = 48f;
        float groundY = 90f;

        // Cauldron near the middle.
        cauldron = new Cauldron(560, 90, 120, 105);

        // Inner cauldron platform.
        // Top = 139 + 8 = 147.
        heatZone = new CauldronHeatPlatform(586, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Cauldron center = 560 + 60 = 620.
        // Platform width = 150, so x = 620 - 75 = 545.
        liftPlatform = new CauldronLiftPlatform(545, 210, 150, 28, 250);

        // Square SurpriseWall tiles used like normal blocks.
        // They are solid in normal emotions and pass-through in Surprise.
        //
        // Layout:
        // ground → 1-block-high step → 2-block-high step → cauldron
        leadingBlocks = new SurpriseWall[] {
            // 1-block-high platform
            new SurpriseWall(300, groundY, block, block),
            new SurpriseWall(348, groundY, block, block),

            // 2-block-high platform
            new SurpriseWall(420, groundY, block, block),
            new SurpriseWall(468, groundY, block, block),
            new SurpriseWall(420, groundY + block, block, block),
            new SurpriseWall(468, groundY + block, block, block),

            // Small square block close to cauldron rim for testing approach
            new SurpriseWall(516, groundY, block, block)
        };

        // Upper target ledge remains solid.
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
