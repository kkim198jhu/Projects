package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.BreakableWall;
import com.letsfaceittogether.utils.LevelData;

import java.util.ArrayList;
import java.util.List;

public class AngryLevel extends BaseLevel {
    private LevelData data;
    private List<BreakableWall> breakableWalls;

    public AngryLevel() {
        super();

        data = LevelData.load("assets/levels/neutral/angry.json");

        
        applyAllowedEmotionsFromData(data);
setTutorialText(data.tutorialText);

        levelWidth = data.levelWidth;

        player.x = data.playerX;
        player.y = data.playerY;
        player.previousX = player.x;
        player.previousY = player.y;

        key.x = data.keyX;
        key.y = data.keyY;

        door.x = data.doorX;
        door.y = data.doorY;

        breakableWalls = new ArrayList<>();

        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("breakableWall")) {
                breakableWalls.add(new BreakableWall(object.x, object.y, object.w, object.h));
            }
        }
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        for (BreakableWall wall : breakableWalls) {
            wall.update(player);
        }
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        for (BreakableWall wall : breakableWalls) {
            wall.draw(batch);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        for (BreakableWall wall : breakableWalls) {
            wall.draw(shapeRenderer);
        }
    }
}
