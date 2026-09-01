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

    private SolidWall leftRunBlock;
    private SolidWall leftJumpBlock;
    private SolidWall rightJumpBlock;
    private SolidWall upperGoalBlock;

    private boolean standingInHeatZone = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Heat the cauldron with Angry, then use the side blocks to jump onto the moving platform.");

        levelWidth = 1300;
        player.worldWidth = levelWidth;

        player.x = 150;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;

        // Moved closer to the middle.
        cauldron = new Cauldron(470, 90, 120, 105);

        // Inner cauldron platform.
        // Top = 139 + 8 = 147.
        heatZone = new CauldronHeatPlatform(496, 139, 68, 8);

        // Floating platform centered above cauldron.
        // Cauldron center = 470 + 60 = 530.
        // Platform width = 150, so x = 530 - 75 = 455.
        liftPlatform = new CauldronLiftPlatform(455, 285, 150, 28, 250);

        // Test blocks so you can run back and jump onto the moving platform.
        // These are intentionally placed left/right of the lift platform.
        leftRunBlock = new SolidWall(240, 155, 150, 28);
        leftJumpBlock = new SolidWall(300, 235, 140, 28);
        rightJumpBlock = new SolidWall(640, 235, 140, 28);

        // Upper target ledge.
        upperGoalBlock = new SolidWall(780, 535, 180, 80);

        key.x = 845;
        key.y = 625;

        door.x = 1150;
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

        leftRunBlock.blockPlayer(player);
        leftJumpBlock.blockPlayer(player);
        rightJumpBlock.blockPlayer(player);
        upperGoalBlock.blockPlayer(player);
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        // Steam/heat visually goes from above the cauldron toward the floating platform.
        liftPlatform.drawWind(shapeRenderer, cauldron.getHeatRatio());

        // Debug-visible inner platform for testing.
        heatZone.drawDebug(shapeRenderer);

        liftPlatform.draw(shapeRenderer);

        leftRunBlock.draw(shapeRenderer);
        leftJumpBlock.draw(shapeRenderer);
        rightJumpBlock.draw(shapeRenderer);
        upperGoalBlock.draw(shapeRenderer);
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Draw cauldron behind player by default.
        cauldron.draw(batch);

        liftPlatform.drawWind(batch, cauldron.getHeatRatio());
        liftPlatform.draw(batch);

        leftRunBlock.draw(batch);
        leftJumpBlock.draw(batch);
        rightJumpBlock.draw(batch);
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
