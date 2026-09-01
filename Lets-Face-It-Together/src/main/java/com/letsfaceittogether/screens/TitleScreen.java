package com.letsfaceittogether.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.GameScreenType;
import com.letsfaceittogether.utils.ScreenCoords;

public class TitleScreen {
    private float playX = 330;
    private float playY = 260;
    private float buttonWidth = 300;
    private float buttonHeight = 80;

    private float settingsX = 330;
    private float settingsY = 150;

    public TitleScreen() {
        printInstructions();
    }

    public GameScreenType update() {
        if (Gdx.input.justTouched()) {
            float mouseX = ScreenCoords.mouseX();
            float mouseY = ScreenCoords.mouseY();

            if (isInside(mouseX, mouseY, playX, playY, buttonWidth, buttonHeight)) {
                System.out.println("Play clicked.");
                return GameScreenType.SAVE_SELECT;
            }

            if (isInside(mouseX, mouseY, settingsX, settingsY, buttonWidth, buttonHeight)) {
                System.out.println("Settings clicked.");
                return GameScreenType.SETTINGS;
            }
        }

        return GameScreenType.TITLE;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.03f, 0.03f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(playX, playY, buttonWidth, buttonHeight);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(settingsX, settingsY, buttonWidth, buttonHeight);

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);

        drawCenteredText(batch, font, "Let's Face It Together", 480, 430, 2.2f);
        drawCenteredText(batch, font, "PLAY", playX + buttonWidth / 2f, playY + 50, 2.0f);
        drawCenteredText(batch, font, "SETTINGS", settingsX + buttonWidth / 2f, settingsY + 50, 2.0f);

        font.getData().setScale(2.0f);

        batch.end();
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
        System.out.println("TITLE SCREEN");
        System.out.println("Click Play.");
        System.out.println("Click Settings.");
    }
}
