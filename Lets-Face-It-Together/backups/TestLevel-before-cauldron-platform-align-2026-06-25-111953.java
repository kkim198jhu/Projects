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

        cauldron = new Cauldron(280, 90, 100, 90);

        // Platform starts low and rises as the cauldron heats.
        liftPlatform = new CauldronLiftPlatform(520, 170, 140, 28, 250);

        upperGoalBlock = new SolidWall(780, 420, 160, 80);

        key.x = 835;
        key.y = 510;

        door.x = 1050;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(0.12f, 0.06f, 0.05f, 1.0f);
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
        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);
        upperGoalBlock.draw(batch);
    }
    @Override
    protected void drawForegroundSprites(SpriteBatch batch) {
        // Draw cauldron after player so the rim/body appears in front.
        cauldron.draw(batch);
    }

}
