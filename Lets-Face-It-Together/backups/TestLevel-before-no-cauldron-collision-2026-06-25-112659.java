package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cauldron;
import com.letsfaceittogether.objects.CauldronLiftPlatform;
import com.letsfaceittogether.objects.SolidWall;

public class TestLevel extends BaseLevel {
    private Cauldron cauldron;
    private CauldronLiftPlatform liftPlatform;
    private SolidWall upperGoalBlock;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Jump into the cauldron. Angry heats it faster. Heat lifts the platform.");

        levelWidth = 1200;
        player.worldWidth = levelWidth;

        player.x = 100;
        player.y = 90;

        cauldron = new Cauldron(160, 90, 120, 105);

        // Platform starts low and rises as the cauldron heats.
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
        cauldron.blockPlayer(player);
        cauldron.update(delta, player);

        liftPlatform.update(delta, cauldron.getHeatRatio());
        liftPlatform.blockPlayer(player);

        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());
        cauldron.draw(shapeRenderer);
        liftPlatform.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        if (!cauldron.shouldDrawInFrontOfPlayer(player)) {
            cauldron.draw(batch);
        }

liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);
        upperGoalBlock.draw(batch);
    }
    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        if (cauldron.shouldDrawInFrontOfPlayer(player)) {
            }
    }

}
