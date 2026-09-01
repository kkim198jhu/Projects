package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.objects.LaserEmitter;
import com.letsfaceittogether.utils.LevelData;

import java.util.ArrayList;
import java.util.List;

public class SadLevel extends BaseLevel {
    private LevelData data;
    private List<LaserEmitter> laserEmitters;

    public SadLevel() {
        super();

        data = LevelData.load("assets/levels/neutral/sad.json");

        
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

        laserEmitters = new ArrayList<>();

        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("laserEmitter")) {
                laserEmitters.add(new LaserEmitter(object.x, object.y, object.w, object.h, object.length));
            }
        }
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        for (LaserEmitter laserEmitter : laserEmitters) {
            if (laserEmitter.hitsPlayer(player) && player.emotion != Emotion.SAD) {
                System.out.println("Touched lazer without Sad! Restarting Sad level.");
                restartSadLevel();
                return;
            }

            laserEmitter.update(player);
        }
    }

    private void restartSadLevel() {
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
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        for (LaserEmitter laserEmitter : laserEmitters) {
            laserEmitter.draw(batch);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        for (LaserEmitter laserEmitter : laserEmitters) {
            laserEmitter.draw(shapeRenderer);
        }
    }
}
