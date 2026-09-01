package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.Emotion;
import com.letsfaceittogether.objects.SolidWall;
import com.letsfaceittogether.objects.SurpriseWall;
import com.letsfaceittogether.utils.LevelData;

import java.util.ArrayList;
import java.util.List;

public class SurpriseLevel extends BaseLevel {
    private LevelData data;

    // Original blocks from JSON, used for drawing only.
    private List<SolidWall> visualSolidWalls;
    private List<SurpriseWall> visualSurpriseWalls;

    // Larger row-merged walls, used for collision only.
    private List<SolidWall> solidCollisionWalls;
    private List<SolidWall> surpriseCollisionWalls;

    public SurpriseLevel() {
        super();

        data = LevelData.load("assets/levels/neutral/surprise.json");

        
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

        visualSolidWalls = new ArrayList<>();
        visualSurpriseWalls = new ArrayList<>();

        List<RectData> solidRects = new ArrayList<>();
        List<RectData> surpriseRects = new ArrayList<>();

        for (LevelData.LevelObjectData object : data.objects) {
            if (object.type.equals("solidWall")) {
                visualSolidWalls.add(new SolidWall(object.x, object.y, object.w, object.h));
                solidRects.add(new RectData(object.x, object.y, object.w, object.h));
            } else if (object.type.equals("surpriseWall")) {
                visualSurpriseWalls.add(new SurpriseWall(object.x, object.y, object.w, object.h));
                surpriseRects.add(new RectData(object.x, object.y, object.w, object.h));
            }
        }

        solidCollisionWalls = makeCollisionWalls(mergeHorizontalRows(solidRects));
        surpriseCollisionWalls = makeCollisionWalls(mergeHorizontalRows(surpriseRects));
    }

    @Override
    protected Color getBackgroundColor() {
        return data.background;
    }

    @Override
    protected void updateLevel(float delta) {
        for (SolidWall wall : solidCollisionWalls) {
            wall.blockPlayer(player);
        }

        // Surprise blocks behave exactly like normal solid walls,
        // except they stop colliding while the player is Surprise.
        if (player.emotion != Emotion.SURPRISE) {
            for (SolidWall wall : surpriseCollisionWalls) {
                wall.blockPlayer(player);
            }
        }
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        // Draw original individual blocks so the level still looks like tiles.
        for (SolidWall wall : visualSolidWalls) {
            wall.draw(batch);
        }

        for (SurpriseWall wall : visualSurpriseWalls) {
            wall.draw(batch, player);
        }
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        // Draw original individual blocks so the debug/shape layer matches the level.
        for (SolidWall wall : visualSolidWalls) {
            wall.draw(shapeRenderer);
        }

        for (SurpriseWall wall : visualSurpriseWalls) {
            wall.draw(shapeRenderer, player);
        }
    }

    private List<SolidWall> makeCollisionWalls(List<RectData> rects) {
        List<SolidWall> walls = new ArrayList<>();

        for (RectData rect : rects) {
            walls.add(new SolidWall(rect.x, rect.y, rect.w, rect.h));
        }

        return walls;
    }

    private List<RectData> mergeHorizontalRows(List<RectData> rects) {
        List<RectData> result = new ArrayList<>();
        boolean[] used = new boolean[rects.size()];
        float epsilon = 1.0f;

        for (int i = 0; i < rects.size(); i++) {
            if (used[i]) {
                continue;
            }

            RectData current = rects.get(i);
            used[i] = true;

            boolean changed = true;

            while (changed) {
                changed = false;

                for (int j = 0; j < rects.size(); j++) {
                    if (used[j]) {
                        continue;
                    }

                    RectData other = rects.get(j);

                    boolean sameRow = Math.abs(current.y - other.y) <= epsilon;
                    boolean sameHeight = Math.abs(current.h - other.h) <= epsilon;

                    float currentLeft = current.x;
                    float currentRight = current.x + current.w;
                    float otherLeft = other.x;
                    float otherRight = other.x + other.w;

                    boolean touchesOrOverlaps =
                            currentRight >= otherLeft - epsilon &&
                            otherRight >= currentLeft - epsilon;

                    if (sameRow && sameHeight && touchesOrOverlaps) {
                        float newLeft = Math.min(currentLeft, otherLeft);
                        float newRight = Math.max(currentRight, otherRight);

                        current = new RectData(newLeft, current.y, newRight - newLeft, current.h);
                        used[j] = true;
                        changed = true;
                    }
                }
            }

            result.add(current);
        }

        return result;
    }

    private static class RectData {
        float x;
        float y;
        float w;
        float h;

        RectData(float x, float y, float w, float h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
