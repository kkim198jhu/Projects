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
import com.letsfaceittogether.Keybinds;
import com.letsfaceittogether.SaveManager;
import com.letsfaceittogether.objects.BackButton;

public class SettingsScreen {
    private BackButton backButton;

    private Slider masterSlider;
    private Slider musicSlider;
    private Slider effectsSlider;

    // -1 = not rebinding
    // 0 = jump, 1 = left, 2 = right, 3 = enter door, 4 = restart
    private int waitingForKeybind = -1;

    private GameScreenType backTarget = GameScreenType.TITLE;

    private float bindX = 500;
    private float bindW = 210;
    private float bindH = 30;

    private float jumpY = 170;
    private float leftY = 137;
    private float rightY = 104;
    private float enterY = 71;
    private float restartY = 38;

    public void setBackTarget(GameScreenType backTarget) {
        this.backTarget = backTarget;
    }

    public SettingsScreen() {
        backButton = new BackButton();

        masterSlider = new Slider(350, 385, 300, 0.75f);
        musicSlider = new Slider(350, 325, 300, 0.60f);
        effectsSlider = new Slider(350, 265, 300, 0.70f);
    }

    public GameScreenType update() {
        if (waitingForKeybind != -1) {
            int newKey = getNewPressedKey();

            if (newKey != -1) {
                applyKeybind(waitingForKeybind, newKey);
                waitingForKeybind = -1;
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                waitingForKeybind = -1;
            }

            return GameScreenType.SETTINGS;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            return backTarget;
        }

        if (backButton.clicked()) {
            return backTarget;
        }

        masterSlider.update();
        musicSlider.update();
        effectsSlider.update();

        if (Gdx.input.justTouched()) {
            float mouseX = ScreenCoords.mouseX();
            float mouseY = ScreenCoords.mouseY();

            if (isInside(mouseX, mouseY, bindX, jumpY, bindW, bindH)) {
                waitingForKeybind = 0;
            } else if (isInside(mouseX, mouseY, bindX, leftY, bindW, bindH)) {
                waitingForKeybind = 1;
            } else if (isInside(mouseX, mouseY, bindX, rightY, bindW, bindH)) {
                waitingForKeybind = 2;
            } else if (isInside(mouseX, mouseY, bindX, enterY, bindW, bindH)) {
                waitingForKeybind = 3;
            } else if (isInside(mouseX, mouseY, bindX, restartY, bindW, bindH)) {
                waitingForKeybind = 4;
            }
        }

        return GameScreenType.SETTINGS;
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glClearColor(0.03f, 0.03f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        backButton.draw(shapeRenderer);

        masterSlider.draw(shapeRenderer);
        musicSlider.draw(shapeRenderer);
        effectsSlider.draw(shapeRenderer);

        drawKeyBox(shapeRenderer, 230, 20, 500, 205);

        drawBindButton(shapeRenderer, bindX, jumpY, waitingForKeybind == 0);
        drawBindButton(shapeRenderer, bindX, leftY, waitingForKeybind == 1);
        drawBindButton(shapeRenderer, bindX, rightY, waitingForKeybind == 2);
        drawBindButton(shapeRenderer, bindX, enterY, waitingForKeybind == 3);
        drawBindButton(shapeRenderer, bindX, restartY, waitingForKeybind == 4);

        shapeRenderer.end();

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "Settings", 480, 505, 2.2f);

        font.getData().setScale(1.45f);
        font.draw(batch, "Master Volume", 125, 405);
        font.draw(batch, "Music", 125, 345);
        font.draw(batch, "Sound Effects", 125, 285);

        font.getData().setScale(1.25f);
        font.draw(batch, "Keybinds", 410, 212);

        font.getData().setScale(0.95f);
        font.draw(batch, "Jump", 275, 192);
        font.draw(batch, "Left", 275, 159);
        font.draw(batch, "Right", 275, 126);
        font.draw(batch, "Enter Door", 275, 93);
        font.draw(batch, "Restart", 275, 60);

        drawKeyText(batch, font, Keybinds.keyName(Keybinds.jumpKey), bindX, jumpY, bindW, bindH, waitingForKeybind == 0);
        drawKeyText(batch, font, Keybinds.keyName(Keybinds.leftKey), bindX, leftY, bindW, bindH, waitingForKeybind == 1);
        drawKeyText(batch, font, Keybinds.keyName(Keybinds.rightKey), bindX, rightY, bindW, bindH, waitingForKeybind == 2);
        drawKeyText(batch, font, Keybinds.keyName(Keybinds.enterDoorKey), bindX, enterY, bindW, bindH, waitingForKeybind == 3);
        drawKeyText(batch, font, Keybinds.keyName(Keybinds.restartKey), bindX, restartY, bindW, bindH, waitingForKeybind == 4);

        if (waitingForKeybind != -1) {
            font.setColor(Color.YELLOW);
            drawCenteredText(batch, font, "Press a new key, or ESC to cancel", 480, 245, 1.1f);
        } else {
            font.setColor(Color.LIGHT_GRAY);
            drawCenteredText(batch, font, "Click a key to change it", 480, 245, 1.1f);
        }

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void applyKeybind(int bindIndex, int newKey) {
        if (bindIndex == 0) {
            Keybinds.jumpKey = newKey;
        } else if (bindIndex == 1) {
            Keybinds.leftKey = newKey;
        } else if (bindIndex == 2) {
            Keybinds.rightKey = newKey;
        } else if (bindIndex == 3) {
            Keybinds.enterDoorKey = newKey;
        } else if (bindIndex == 4) {
            Keybinds.restartKey = newKey;
        }

        System.out.println("Changed keybind to " + Keybinds.keyName(newKey));
        SaveManager.saveKeybinds();
    }

    private int getNewPressedKey() {
        int[] possibleKeys = {
                Input.Keys.SPACE,
                Input.Keys.A, Input.Keys.B, Input.Keys.C, Input.Keys.D, Input.Keys.E,
                Input.Keys.F, Input.Keys.G, Input.Keys.H, Input.Keys.I, Input.Keys.J,
                Input.Keys.K, Input.Keys.L, Input.Keys.M, Input.Keys.N, Input.Keys.O,
                Input.Keys.P, Input.Keys.Q, Input.Keys.R, Input.Keys.S, Input.Keys.T,
                Input.Keys.U, Input.Keys.V, Input.Keys.W, Input.Keys.X, Input.Keys.Y,
                Input.Keys.Z,
                Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
                Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT,
                Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT,
                Input.Keys.TAB, Input.Keys.ENTER,
                Input.Keys.NUM_0, Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3,
                Input.Keys.NUM_4, Input.Keys.NUM_5, Input.Keys.NUM_6, Input.Keys.NUM_7,
                Input.Keys.NUM_8, Input.Keys.NUM_9
        };

        for (int key : possibleKeys) {
            if (key != Input.Keys.ESCAPE && Gdx.input.isKeyJustPressed(key)) {
                return key;
            }
        }

        return -1;
    }

    private void drawKeyBox(ShapeRenderer shapeRenderer, float x, float y, float width, float height) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
    }

    private void drawBindButton(ShapeRenderer shapeRenderer, float x, float y, boolean selected) {
        if (selected) {
            shapeRenderer.setColor(Color.YELLOW);
        } else {
            shapeRenderer.setColor(Color.GRAY);
        }

        shapeRenderer.rect(x, y, bindW, bindH);
    }

    private void drawKeyText(SpriteBatch batch, BitmapFont font, String text, float x, float y, float w, float h, boolean selected) {
        font.setColor(selected ? Color.BLACK : Color.WHITE);
        drawCenteredTextInBox(batch, font, selected ? "Press key..." : text, x, y, w, h, 0.95f);
    }

    private void drawCenteredText(SpriteBatch batch, BitmapFont font, String text, float centerX, float y, float scale) {
        GlyphLayout layout = new GlyphLayout();

        font.getData().setScale(scale);
        layout.setText(font, text);

        float textX = centerX - layout.width / 2f;
        font.draw(batch, text, textX, y);
    }

    private void drawCenteredTextInBox(SpriteBatch batch, BitmapFont font, String text, float boxX, float boxY, float boxW, float boxH, float scale) {
        GlyphLayout layout = new GlyphLayout();

        font.getData().setScale(scale);
        layout.setText(font, text);

        float textX = boxX + (boxW - layout.width) / 2f;
        float textY = boxY + (boxH + layout.height) / 2f;

        font.draw(batch, text, textX, textY);
    }

    private boolean isInside(float px, float py, float x, float y, float w, float h) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    private static class Slider {
        float x;
        float y;
        float width;
        float height = 12;
        float knobSize = 28;
        float value;

        Slider(float x, float y, float width, float value) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.value = value;
        }

        void update() {
            if (!Gdx.input.isTouched()) {
                return;
            }

            float mouseX = ScreenCoords.mouseX();
            float mouseY = ScreenCoords.mouseY();

            boolean nearSlider =
                    mouseX >= x &&
                    mouseX <= x + width &&
                    mouseY >= y - 20 &&
                    mouseY <= y + 30;

            if (nearSlider) {
                value = (mouseX - x) / width;

                if (value < 0) value = 0;
                if (value > 1) value = 1;
            }
        }

        void draw(ShapeRenderer shapeRenderer) {
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(x, y, width, height);

            shapeRenderer.setColor(Color.LIGHT_GRAY);
            shapeRenderer.rect(x, y, width * value, height);

            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(x + width * value - knobSize / 2, y - 8, knobSize, knobSize);
        }
    }
}
