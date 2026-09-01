package com.letsfaceittogether.screens;

import com.letsfaceittogether.Emotion;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.HeavyButton;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.utils.LevelData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HappyLevel extends BaseLevel {
    private LevelData data;

    private List<ButtonLink> buttons;
    private List<WallLink> walls;

    public HappyLevel() {
        this("assets/levels/neutral/happy.json");
    }

    public HappyLevel(String levelPath) {
        super();

        data = LevelData.load(levelPath);

        
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

        buttons = new ArrayList<>();
        walls = new ArrayList<>();

        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("heavyButton")) {
                buttons.add(new ButtonLink(
                        new HeavyButton(object.x, object.y, object.w, object.h),
                        object.opensList
                ));
            } else if (object.type.equals("solidWall")) {
                walls.add(new WallLink(
                        new SolidWall(object.x, object.y, object.w, object.h),
                        object.id
                ));
            }
        }
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        Set<String> openedWallIds = new HashSet<>();

        for (ButtonLink buttonLink : buttons) {
            buttonLink.button.blockPlayer(player);
            buttonLink.button.update(player);

            if (buttonLink.button.pressed) {
                openedWallIds.addAll(buttonLink.opensIds);
            }
        }

        for (WallLink wallLink : walls) {
            if (openedWallIds.contains(wallLink.id)) {
                wallLink.wall.remove();
            }

            wallLink.wall.blockPlayer(player);
        }
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        for (WallLink wallLink : walls) {
            wallLink.wall.draw(batch);
        }

        for (ButtonLink buttonLink : buttons) {
            buttonLink.button.draw(batch);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        for (ButtonLink buttonLink : buttons) {
            buttonLink.button.draw(shapeRenderer);
        }

        for (WallLink wallLink : walls) {
            wallLink.wall.draw(shapeRenderer);
        }
    }

    private static class ButtonLink {
        HeavyButton button;
        List<String> opensIds;

        ButtonLink(HeavyButton button, List<String> opensIds) {
            this.button = button;
            this.opensIds = opensIds;
        }
    }

    private static class WallLink {
        SolidWall wall;
        String id;

        WallLink(SolidWall wall, String id) {
            this.wall = wall;
            this.id = id;
        }
    }
}
