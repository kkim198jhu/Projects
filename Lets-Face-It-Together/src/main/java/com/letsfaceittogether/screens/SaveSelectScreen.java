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
import com.letsfaceittogether.SaveManager;
import com.letsfaceittogether.objects.BackButton;
import com.letsfaceittogether.utils.ScreenCoords;

public class SaveSelectScreen {
    private BackButton backButton = new BackButton();

    private boolean confirmingOverwrite = false;
    private int overwriteSlot = -1;

    private float slotX = 230;
    private float slotW = 360;
    private float slotH = 70;

    private float newX = 620;
    private float newW = 150;

    private float[] slotYs = {350, 250, 150};

    private float yesX = 270;
    private float yesY = 190;
    private float noX = 520;
    private float noY = 190;
    private float confirmButtonW = 170;
    private float confirmButtonH = 70;

    public GameScreenType update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (confirmingOverwrite) {
                confirmingOverwrite = false;
                overwriteSlot = -1;
                return GameScreenType.SAVE_SELECT;
            }

            return GameScreenType.TITLE;
        }

        if (confirmingOverwrite) {
            return updateConfirmation();
        }

        if (backButton.clicked()) {
            return GameScreenType.TITLE;
        }

        if (!Gdx.input.justTouched()) {
            return GameScreenType.SAVE_SELECT;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        for (int i = 0; i < 3; i++) {
            int slot = i + 1;
            float y = slotYs[i];

            if (isInside(mouseX, mouseY, slotX, y, slotW, slotH)) {
                SaveManager.loadSlot(slot);
                return GameScreenType.WORLD_SELECT;
            }

            if (SaveManager.slotExists(slot) && isInside(mouseX, mouseY, newX, y, newW, slotH)) {
                confirmingOverwrite = true;
                overwriteSlot = slot;
                return GameScreenType.SAVE_SELECT;
            }
        }

        return GameScreenType.SAVE_SELECT;
    }

    private GameScreenType updateConfirmation() {
        if (!Gdx.input.justTouched()) {
            return GameScreenType.SAVE_SELECT;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        if (isInside(mouseX, mouseY, yesX, yesY, confirmButtonW, confirmButtonH)) {
            SaveManager.overwriteSlot(overwriteSlot);
            confirmingOverwrite = false;
            overwriteSlot = -1;
            return GameScreenType.WORLD_SELECT;
        }

        if (isInside(mouseX, mouseY, noX, noY, confirmButtonW, confirmButtonH)) {
            confirmingOverwrite = false;
            overwriteSlot = -1;
            return GameScreenType.SAVE_SELECT;
        }

        return GameScreenType.SAVE_SELECT;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        if (confirmingOverwrite) {
            drawConfirmation(shapeRenderer, batch, font);
        } else {
            drawSaveSlots(shapeRenderer, batch, font);
        }
    }

    private void drawSaveSlots(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.03f, 0.03f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        backButton.draw(shapeRenderer);

        for (int i = 0; i < 3; i++) {
            int slot = i + 1;
            float y = slotYs[i];

            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(slotX, y, slotW, slotH);

            if (SaveManager.slotExists(slot)) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.rect(newX, y, newW, slotH);
            }
        }

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "Select Save File", 480, 500, 2.4f);

        for (int i = 0; i < 3; i++) {
            int slot = i + 1;
            float y = slotYs[i];

            font.setColor(Color.WHITE);
            drawCenteredText(batch, font, "Save " + slot, slotX + slotW / 2f, y + 52, 1.6f);

            font.setColor(Color.LIGHT_GRAY);
            drawCenteredText(batch, font, SaveManager.slotDescription(slot), slotX + slotW / 2f, y + 25, 0.85f);

            if (SaveManager.slotExists(slot)) {
                font.setColor(Color.WHITE);
                drawCenteredText(batch, font, "New", newX + newW / 2f, y + 44, 1.5f);
            }
        }

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void drawConfirmation(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.03f, 0.03f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(180, 120, 600, 300);

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(yesX, yesY, confirmButtonW, confirmButtonH);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(noX, noY, confirmButtonW, confirmButtonH);

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "Overwrite Save " + overwriteSlot + "?", 480, 360, 2.2f);

        font.setColor(Color.LIGHT_GRAY);
        drawCenteredText(batch, font, "This will erase the current file.", 480, 315, 1.2f);
        drawCenteredText(batch, font, "Are you sure?", 480, 285, 1.2f);

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "YES", yesX + confirmButtonW / 2f, yesY + 45, 1.8f);
        drawCenteredText(batch, font, "NO", noX + confirmButtonW / 2f, noY + 45, 1.8f);

        font.setColor(Color.LIGHT_GRAY);
        drawCenteredText(batch, font, "Press ESC to cancel", 480, 85, 1.1f);

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

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
}
