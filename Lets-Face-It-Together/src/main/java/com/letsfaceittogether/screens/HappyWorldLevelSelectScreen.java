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
import com.letsfaceittogether.objects.BackButton;
import com.letsfaceittogether.utils.ScreenCoords;

public class HappyWorldLevelSelectScreen {
    private BackButton backButton;

    private float boxSize = 120;
    private float levelX = 420;
    private float levelY = 230;

    public HappyWorldLevelSelectScreen() {
        this.backButton = new BackButton();
        printInstructions();
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

            if (isInside(mouseX, mouseY, levelX, levelY, boxSize, boxSize)) {
                return GameScreenType.HAPPY_WORLD_LEVEL_1;
            }
        }

        return GameScreenType.WORLD_2_LEVEL_SELECT;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.08f, 0.04f, 0.07f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        backButton.draw(shapeRenderer);

        shapeRenderer.setColor(Color.PINK);
        shapeRenderer.rect(levelX, levelY, boxSize, boxSize);

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "World 2: Happy", 480, 500, 2.0f);

        font.setColor(Color.BLACK);
        drawCenteredTextInBox(batch, font, "Happy 1", levelX, levelY, boxSize, boxSize, 1.0f);

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
        System.out.println("WORLD 2: HAPPY");
        System.out.println("Click Happy 1.");
    }
}
