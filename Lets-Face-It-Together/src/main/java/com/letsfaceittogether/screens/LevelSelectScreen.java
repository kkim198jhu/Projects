package com.letsfaceittogether.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.GameScreenType;
import com.letsfaceittogether.utils.ScreenCoords;
import com.letsfaceittogether.objects.BackButton;

public class LevelSelectScreen {
    private int highestUnlockedLevel;
    private BackButton backButton;

    private boolean[] completedLevels = new boolean[6];

    private float boxSize = 100;

    private float[] xs = {250, 430, 610, 250, 430, 610};
    private float[] ys = {300, 300, 300, 130, 130, 130};

    private String[] labels = {
            "Happy", "Angry", "Sad", "Fear", "Surprise", "Disgust"
    };

    private Color[] colors = {
            Color.PINK,
            Color.RED,
            Color.CYAN,
            Color.PURPLE,
            Color.WHITE,
            Color.GREEN
    };

    public LevelSelectScreen(int highestUnlockedLevel) {
        this.highestUnlockedLevel = highestUnlockedLevel;
        this.backButton = new BackButton();
        printInstructions();
    }

    public void setHighestUnlockedLevel(int highestUnlockedLevel) {
        this.highestUnlockedLevel = highestUnlockedLevel;
    }

    public void setCompletedLevels(boolean[] completedLevels) {
        if (completedLevels == null) return;

        for (int i = 0; i < this.completedLevels.length && i < completedLevels.length; i++) {
            this.completedLevels[i] = completedLevels[i];
        }
    }

    public GameScreenType update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            return GameScreenType.WORLD_SELECT;
        }

        if (backButton.clicked()) {
            System.out.println("Back to world select.");
            return GameScreenType.WORLD_SELECT;
        }

        if (Gdx.input.justTouched()) {
            float mouseX = ScreenCoords.mouseX();
            float mouseY = ScreenCoords.mouseY();

            for (int i = 0; i < 6; i++) {
                if (isInside(mouseX, mouseY, xs[i], ys[i], boxSize, boxSize)) {
                    if (i <= highestUnlockedLevel) {
                        return levelTypeFromIndex(i);
                    } else {
                        System.out.println("This level is locked.");
                    }
                }
            }
        }

        return GameScreenType.WORLD_1_LEVEL_SELECT;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.09f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        backButton.draw(shapeRenderer);

        for (int i = 0; i < 6; i++) {
            shapeRenderer.setColor(i <= highestUnlockedLevel ? colors[i] : Color.DARK_GRAY);
            shapeRenderer.rect(xs[i], ys[i], boxSize, boxSize);

            if (completedLevels[i]) {
                drawCornerCheckmark(shapeRenderer, xs[i], ys[i]);
            }
        }

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "World 1: Neutral", 480, 500, 2.0f);

        for (int i = 0; i < 6; i++) {
            String text = i <= highestUnlockedLevel ? labels[i] : "LOCKED";

            font.setColor(Color.BLACK);

            float scale = 1.15f;
            if (text.equals("Surprise") || text.equals("Disgust") || text.equals("LOCKED")) {
                scale = 1.0f;
            }

            drawCenteredTextInBox(batch, font, text, xs[i], ys[i], boxSize, boxSize, scale);
        }

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void drawCenteredTextInBox(SpriteBatch batch, BitmapFont font, String text, float boxX, float boxY, float boxW, float boxH, float scale) {
        GlyphLayout layout = new GlyphLayout();

        font.getData().setScale(scale);
        layout.setText(font, text);

        float textX = boxX + (boxW - layout.width) / 2f;
        float textY = boxY + (boxH + layout.height) / 2f;

        font.draw(batch, text, textX, textY);
    }

    private void drawCenteredText(SpriteBatch batch, BitmapFont font, String text, float centerX, float y, float scale) {
        GlyphLayout layout = new GlyphLayout();

        font.getData().setScale(scale);
        layout.setText(font, text);

        float textX = centerX - layout.width / 2f;

        font.draw(batch, text, textX, y);
    }

    private void drawCornerCheckmark(ShapeRenderer shapeRenderer, float x, float y) {
        shapeRenderer.setColor(new Color(0f, 0.45f, 0f, 1f));

        shapeRenderer.rectLine(x + 68, y + 82, x + 78, y + 72, 4);
        shapeRenderer.rectLine(x + 78, y + 72, x + 94, y + 94, 4);
    }

    private GameScreenType levelTypeFromIndex(int index) {
        switch (index) {
            case 0:
                return GameScreenType.HAPPY_LEVEL;
            case 1:
                return GameScreenType.ANGRY_LEVEL;
            case 2:
                return GameScreenType.SAD_LEVEL;
            case 3:
                return GameScreenType.FEAR_LEVEL;
            case 4:
                return GameScreenType.SURPRISE_LEVEL;
            case 5:
                return GameScreenType.DISGUST_LEVEL;
            default:
                return GameScreenType.WORLD_1_LEVEL_SELECT;
        }
    }

    private boolean isInside(float px, float py, float x, float y, float w, float h) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public void printInstructions() {
        System.out.println("WORLD 1: NEUTRAL");
        System.out.println("Click levels in order.");
    }
}
