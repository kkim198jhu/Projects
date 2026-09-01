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

    private SolidWall[] solidWalls;
    private SurpriseWall[] surpriseWalls;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Use Surprise to pass through surprise walls. Use Angry inside the cauldron to lift the platform.");

        levelWidth = 1900;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        // Exact JSON-style wall layout: 100 wide x 95 tall tiles.
        solidWalls = new SolidWall[] {
            new SolidWall(300, 90, 100, 95),
            new SolidWall(300, 280, 100, 95),
            new SolidWall(400, 280, 100, 95),
            new SolidWall(500, 280, 100, 95),
            new SolidWall(500, 185, 100, 95),
            new SolidWall(600, 280, 100, 95),
            new SolidWall(700, 90, 100, 95),
            new SolidWall(800, 90, 100, 95),
            new SolidWall(800, 185, 100, 95),
            new SolidWall(900, 90, 100, 95),

            // Later platforms after the cauldron section.
            new SolidWall(1220, 280, 100, 95),
            new SolidWall(1420, 280, 100, 95),
            new SolidWall(1620, 280, 100, 95)
        };

        surpriseWalls = new SurpriseWall[] {
            new SurpriseWall(300, 185, 100, 95),
            new SurpriseWall(400, 90, 100, 95),
            new SurpriseWall(400, 185, 100, 95),
            new SurpriseWall(500, 90, 100, 95),
            new SurpriseWall(600, 90, 100, 95),
            new SurpriseWall(600, 185, 100, 95),
            new SurpriseWall(700, 185, 100, 95),
            new SurpriseWall(700, 280, 100, 95),
            new SurpriseWall(800, 280, 100, 95)
        };

        // Cauldron placed after the wall puzzle.
        cauldron = new Cauldron(1040, 90, 120, 105);
        heatZone = new CauldronHeatPlatform(1066, 139, 68, 8);

        // Floating platform centered above cauldron.
        liftPlatform = new CauldronLiftPlatform(1025, 210, 150, 28, 250);

        key.x = 1250;
        key.y = 410;

        door.x = 1720;
        door.y = 375;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(0.30f, 0.30f, 0.30f, 1.0f);
    }

    @Override
    protected void updateLevel(float delta) {
        for (SolidWall wall : solidWalls) {
            wall.blockPlayer(player);
        }

        for (SurpriseWall wall : surpriseWalls) {
            wall.blockPlayer(player);
        }

        heatZone.blockPlayer(player);
        standingInHeatZone = heatZone.isPlayerStandingOn(player);

        cauldron.update(delta, player, standingInHeatZone);

        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        heatZone.drawDebug(shapeRenderer);

        for (SolidWall wall : solidWalls) {
            wall.draw(shapeRenderer);
        }

        for (SurpriseWall wall : surpriseWalls) {
            wall.draw(shapeRenderer, player);
        }

        liftPlatform.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        for (SolidWall wall : solidWalls) {
            wall.draw(batch);
        }

        for (SurpriseWall wall : surpriseWalls) {
            wall.draw(batch, player);
        }
    }

    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        if (cauldron.shouldDrawInFrontOfPlayer(player, standingInHeatZone)) {
            cauldron.draw(batch);
        }
    }
}
