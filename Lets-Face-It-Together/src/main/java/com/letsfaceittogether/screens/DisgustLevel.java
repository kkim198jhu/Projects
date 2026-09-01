package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.Cage;
import com.letsfaceittogether.objects.Ghost;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.utils.LevelData;

public class DisgustLevel extends BaseLevel {
    private LevelData data;
    private Ghost ghost;
    private Cage cage;
    private SolidWall wall;

    public DisgustLevel() {
        super();

        data = LevelData.load("assets/levels/neutral/disgust.json");
        
        applyAllowedEmotionsFromData(data);
setTutorialText(data.tutorialText);

        levelWidth = data.levelWidth;

        buildLevelFromData();
        resetDisgustLevelState();
    }

    private void buildLevelFromData() {
        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("ghost")) {
                ghost = new Ghost(object.x, object.y);
            } else if (object.type.equals("cage")) {
                cage = new Cage(object.x, object.y, object.w, object.h);
                cage.setClosed(false);
            } else if (object.type.equals("solidWall")) {
                wall = new SolidWall(object.x, object.y, object.w, object.h);
            }
        }
    }

    private void resetDisgustLevelState() {
        player.x = data.playerX;
        player.y = data.playerY;
        player.previousX = player.x;
        player.previousY = player.y;
        player.velocityY = 0;
        player.onGround = true;

        key.x = data.keyX;
        key.y = data.keyY;
        key.pickedUp = false;
        key.consumed = false;

        door.x = data.doorX;
        door.y = data.doorY;
        door.setOpen(false);
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        if (ghost != null && cage != null) {
            ghost.update(delta, player, cage);

            if (!ghost.caged && ghost.isInsideCage(cage)) {
                ghost.putInCage(cage);
                cage.setClosed(true);

                if (wall != null) {
                    wall.remove();
                }
            }
        }

        if (wall != null) {
            wall.blockPlayer(player);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        if (wall != null) {
            wall.draw(shapeRenderer);
        }

        if (ghost != null) {
            ghost.draw(shapeRenderer);
        }

        if (cage != null) {
            cage.draw(shapeRenderer);
        }
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        if (wall != null) {
            wall.draw(batch);
        }

        if (ghost != null) {
            ghost.draw(batch);
        }

        if (cage != null) {
            cage.draw(batch);
        }
    }
}
