package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.objects.ChasingWall;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.utils.LevelData;

import java.util.ArrayList;
import java.util.List;

public class FearLevel extends BaseLevel {
    private LevelData data;
    private ChasingWall dangerWall;
    private List<SolidWall> obstacleWalls;

    public FearLevel() {
        super();

        data = LevelData.load("assets/levels/neutral/fear.json");
        
        applyAllowedEmotionsFromData(data);
setTutorialText(data.tutorialText);

        levelWidth = data.levelWidth;

        buildLevelFromData();
        resetFearLevelState();
    }

    private void buildLevelFromData() {
        obstacleWalls = new ArrayList<>();

        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("chasingWall")) {
                dangerWall = new ChasingWall(object.x, object.y, object.w, object.h);
            } else if (object.type.equals("solidWall")) {
                obstacleWalls.add(new SolidWall(object.x, object.y, object.w, object.h));
            }
        }
    }

    private void resetFearLevelState() {
        player.x = data.playerX;
        player.y = data.playerY;
        player.previousX = player.x;
        player.previousY = player.y;
        player.velocityY = 0;
        player.onGround = true;
        player.emotion = Emotion.NEUTRAL;

        key.x = data.keyX;
        key.y = data.keyY;
        key.pickedUp = false;
        key.consumed = false;

        door.x = data.doorX;
        door.y = data.doorY;
        door.setOpen(false);

        if (dangerWall != null) {
            dangerWall.reset();
        }
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        if (dangerWall != null) {
            dangerWall.update(delta, player);
        }

        for (SolidWall wall : obstacleWalls) {
            wall.blockPlayer(player);
        }

        if (dangerWall != null && dangerWall.caughtPlayer()) {
            System.out.println("Fear wall caught you! Resetting fear level.");
            resetFearLevelState();
        }
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        for (SolidWall wall : obstacleWalls) {
            wall.draw(batch);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        if (dangerWall != null) {
            dangerWall.draw(shapeRenderer);
        }

        for (SolidWall wall : obstacleWalls) {
            wall.draw(shapeRenderer);
        }
    }
}
