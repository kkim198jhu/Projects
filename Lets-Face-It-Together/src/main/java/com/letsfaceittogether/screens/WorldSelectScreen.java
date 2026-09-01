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

public class WorldSelectScreen {
    private int highestUnlockedWorld;
    private BackButton backButton;

    private float boxSize = 90;

    private float[] xs = {120, 300, 480, 660, 210, 390, 570};
    private float[] ys = {330, 330, 330, 330, 170, 170, 170};

    private String[] labels = {
            "Neutral", "Happy", "Angry", "Sad", "Fear", "Surprise", "Disgust"
    };

    private Color[] colors = {
            Color.LIGHT_GRAY,
            Color.PINK,
            Color.RED,
            Color.CYAN,
            Color.PURPLE,
            Color.WHITE,
            Color.GREEN
    };

    public WorldSelectScreen(int highestUnlockedWorld) {
        this.highestUnlockedWorld = highestUnlockedWorld;
        this.backButton = new BackButton();
        printInstructions();
    }

    public void setHighestUnlockedWorld(int highestUnlockedWorld) {
        this.highestUnlockedWorld = highestUnlockedWorld;
    }

    public GameScreenType update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            return GameScreenType.TITLE;
        }

        if (backButton.clicked()) {
            System.out.println("Back to title.");
            return GameScreenType.TITLE;
        }

        if (Gdx.input.justTouched()) {
            float mouseX = ScreenCoords.mouseX();
            float mouseY = ScreenCoords.mouseY();

            for (int i = 0; i < 7; i++) {
                if (isInside(mouseX, mouseY, xs[i], ys[i], boxSize, boxSize)) {
                    if (i <= highestUnlockedWorld) {
                        if (i == 0) {
                            System.out.println("World 1: Neutral selected.");
                            return GameScreenType.WORLD_1_LEVEL_SELECT;
                        } else if (i == 1) {
                            System.out.println("World 2: Happy selected.");
                            return GameScreenType.WORLD_2_LEVEL_SELECT;
                        } else {
                            System.out.println("This world is unlocked, but not built yet.");
                        }
                    } else {
                        System.out.println("This world is locked.");
                    }
                }
            }
        }

        return GameScreenType.WORLD_SELECT;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        backButton.draw(shapeRenderer);

        for (int i = 0; i < 7; i++) {
            shapeRenderer.setColor(i <= highestUnlockedWorld ? colors[i] : Color.DARK_GRAY);
            shapeRenderer.rect(xs[i], ys[i], boxSize, boxSize);
        }

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "World Select", 480, 500, 2.0f);

        for (int i = 0; i < 7; i++) {
            String text = i <= highestUnlockedWorld ? labels[i] : "LOCKED";

            font.setColor(Color.BLACK);

            float scale = 1.1f;
            if (text.equals("Surprise") || text.equals("Disgust") || text.equals("Neutral") || text.equals("LOCKED")) {
                scale = 0.95f;
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

    private boolean isInside(float px, float py, float x, float y, float w, float h) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public void printInstructions() {
        System.out.println("WORLD SELECT");
        System.out.println("Click Neutral to open World 1.");
    }
}
