package com.letsfaceittogether.screens;

import com.letsfaceittogether.utils.LevelData;

import com.letsfaceittogether.Emotion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.letsfaceittogether.Keybinds;
import com.letsfaceittogether.Player;
import com.letsfaceittogether.objects.Door;
import com.letsfaceittogether.objects.Key;
import com.letsfaceittogether.utils.ScreenCoords;

public abstract class BaseLevel {
    private Texture blockedCrossTexture;
    private java.util.HashMap<Emotion, Texture> blockedEmotionTextures = new java.util.HashMap<>();

    protected java.util.HashSet<Emotion> allowedEmotions = new java.util.HashSet<>();

    protected Player player;
    protected Key key;
    protected Door door;
    protected boolean levelComplete = false;

    protected float floorY = 70;
    protected float floorHeight = 20;

    protected float screenWidth = 960;
    protected float screenHeight = 540;
    protected float levelWidth = 960;
    protected float cameraX = 0;

    protected String tutorialText = null;
    protected boolean tutorialShowing = false;

    protected float tutorialOkX = 405;
    protected float tutorialOkY = 155;
    protected float tutorialOkW = 150;
    protected float tutorialOkH = 55;

    public BaseLevel() {
        player = new Player();
        key = new Key(720, 250);
        door = new Door(820, 90);
    }

    protected void setTutorialText(String text) {
        tutorialText = text;
        tutorialShowing = text != null && text.length() > 0;
    }

    private void updateTutorialPopup() {
        if (!tutorialShowing) {
            return;
        }

        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        if (mouseX >= tutorialOkX && mouseX <= tutorialOkX + tutorialOkW &&
            mouseY >= tutorialOkY && mouseY <= tutorialOkY + tutorialOkH) {
            tutorialShowing = false;
        }
    }

    public void update(float delta) {
        if (levelComplete) {
            return;
        }

        if (tutorialShowing) {
            updateTutorialPopup();
            return;
        }

        player.worldWidth = levelWidth;

        player.handleEmotionKeys();
        enforceAllowedEmotion();
        player.updateMovement(delta);

        updateLevel(delta);

        key.update(player);
        checkWin();

        updateCamera();
    }

    private void updateCamera() {
        cameraX = player.x + player.width / 2f - screenWidth / 2f;

        if (cameraX < 0) {
            cameraX = 0;
        }

        float maxCameraX = Math.max(0, levelWidth - screenWidth);

        if (cameraX > maxCameraX) {
            cameraX = maxCameraX;
        }
    }

    public void draw(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        updateCamera();

        Color background = getBackgroundColor();
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Matrix4 oldShapeTransform = new Matrix4(shapeRenderer.getTransformMatrix());
        shapeRenderer.getTransformMatrix().setToTranslation(-cameraX, 0, 0);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        drawFloor(shapeRenderer);
        drawLevelObjects(shapeRenderer);
        drawSharedObjects(shapeRenderer);

        shapeRenderer.end();

        shapeRenderer.getTransformMatrix().set(oldShapeTransform);

        Gdx.gl.glDisable(GL20.GL_BLEND);

        Matrix4 oldBatchTransform = new Matrix4(batch.getTransformMatrix());
        Matrix4 worldTransform = new Matrix4().setToTranslation(-cameraX, 0, 0);
        batch.setTransformMatrix(worldTransform);

        batch.begin();

        door.draw(batch);
        drawLevelSprites(batch);
        key.draw(batch);
        player.draw(batch);
        drawForegroundSprites(batch);

        batch.end();

        batch.setTransformMatrix(oldBatchTransform);

        batch.begin();

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);
        font.draw(batch, "Emotion: " + player.emotion, 25, 515);
        drawBlockedEmotionIcons(batch);

        if (!levelComplete && door.playerTouches(player)) {
            font.getData().setScale(1.5f);
            font.setColor(Color.WHITE);

            if (door.isOpen()) {
                font.draw(batch, "Press " + Keybinds.keyName(Keybinds.enterDoorKey) + " to enter", 355, 455);
            } else if (key.pickedUp) {
                font.draw(batch, "Press " + Keybinds.keyName(Keybinds.enterDoorKey) + " to open", 355, 455);
            } else {
                font.draw(batch, "Find the key first", 385, 455);
            }

            font.getData().setScale(2.0f);
        }

        if (levelComplete) {
            font.getData().setScale(4.7f);

            font.setColor(Color.BLACK);
            font.draw(batch, "LEVEL COMPLETE!", 130, 300);

            font.setColor(Color.YELLOW);
            font.draw(batch, "LEVEL COMPLETE!", 120, 315);

            font.getData().setScale(2.0f);
            font.setColor(Color.WHITE);
        }

        batch.end();

        if (tutorialShowing) {
            drawTutorialPopup(shapeRenderer, batch, font);
        }
    }

    private void drawTutorialPopup(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.55f));
        shapeRenderer.rect(0, 0, screenWidth, screenHeight);

        shapeRenderer.setColor(new Color(0.12f, 0.12f, 0.16f, 0.95f));
        shapeRenderer.rect(150, 175, 660, 190);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(tutorialOkX, tutorialOkY, tutorialOkW, tutorialOkH);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        font.setColor(Color.WHITE);
        font.getData().setScale(1.6f);
        drawWrappedTutorialText(batch, font, tutorialText, 190, 330, 580, 34);

        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
        font.draw(batch, "OK", tutorialOkX + 55, tutorialOkY + 37);

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void drawWrappedTutorialText(SpriteBatch batch, BitmapFont font, String text, float x, float y, float maxWidth, float lineHeight) {
        String[] words = text.split(" ");
        String line = "";
        float currentY = y;

        for (String word : words) {
            String testLine = line.length() == 0 ? word : line + " " + word;
            GlyphLayout layout = new GlyphLayout(font, testLine);

            if (layout.width > maxWidth && line.length() > 0) {
                font.draw(batch, line, x, currentY);
                line = word;
                currentY -= lineHeight;
            } else {
                line = testLine;
            }
        }

        if (line.length() > 0) {
            font.draw(batch, line, x, currentY);
        }
    }

    protected Color getBackgroundColor() {
        return new Color(0.05f, 0.05f, 0.08f, 1);
    }

    protected void drawFloor(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(0, floorY, levelWidth, floorHeight);
    }

    protected boolean hasFloorAt(float worldX) {
        return true;
    }


    protected abstract void updateLevel(float delta);

    protected abstract void drawLevelObjects(ShapeRenderer shapeRenderer);

    protected void drawLevelSprites(SpriteBatch batch) {
        // default: no extra sprites
    }

    protected void drawSharedObjects(ShapeRenderer shapeRenderer) {
        door.draw(shapeRenderer);
        key.draw(shapeRenderer);
        player.draw(shapeRenderer);
    }

    protected void checkWin() {
        boolean tryingToUseDoor = Gdx.input.isKeyJustPressed(Keybinds.enterDoorKey);

        if (levelComplete || !door.playerTouches(player) || !tryingToUseDoor) {
            return;
        }

        if (key.pickedUp && !door.isOpen()) {
            door.setOpen(true);

            key.consumed = true;
            key.pickedUp = false;

            System.out.println("Door opened!");
            return;
        }

        if (door.isOpen()) {
            levelComplete = true;
            player.velocityY = 0;
            System.out.println("Level complete!");
        }
    }

    public boolean isComplete() {
        return levelComplete;
    }














    protected void allowOnlyEmotions(Emotion... emotions) {
        allowedEmotions.clear();

        for (Emotion emotion : emotions) {
            allowedEmotions.add(emotion);
        }
    }

    protected void applyAllowedEmotionsFromData(LevelData data) {
        allowedEmotions.clear();

        if (data == null || data.allowedEmotions == null || data.allowedEmotions.isEmpty()) {
            return;
        }

        allowedEmotions.addAll(data.allowedEmotions);
    }

    protected boolean emotionAllowed(Emotion emotion) {
        return allowedEmotions.isEmpty() || allowedEmotions.contains(emotion);
    }

    protected void enforceAllowedEmotion() {
        if (!emotionAllowed(player.emotion)) {
            player.emotion = Emotion.NEUTRAL;
        }
    }

    private Texture getBlockedCrossTexture() {
        if (blockedCrossTexture == null) {
            blockedCrossTexture = new Texture("assets/sprites/symbol/Cross.png");
        }

        return blockedCrossTexture;
    }

    private Texture getEmotionSymbolTexture(Emotion emotion) {
        if (blockedEmotionTextures.containsKey(emotion)) {
            return blockedEmotionTextures.get(emotion);
        }

        String fileName = null;

        if (emotion == Emotion.HAPPY) {
            fileName = "Happy.png";
        } else if (emotion == Emotion.ANGRY) {
            fileName = "Angry.png";
        } else if (emotion == Emotion.SAD) {
            fileName = "Sad.png";
        } else if (emotion == Emotion.FEAR) {
            fileName = "Fear.png";
        } else if (emotion == Emotion.SURPRISE) {
            fileName = "Surprise.png";
        } else if (emotion == Emotion.DISGUST) {
            fileName = "Disgust.png";
        }

        if (fileName == null) {
            return null;
        }

        Texture texture = new Texture("assets/sprites/symbol/" + fileName);
        blockedEmotionTextures.put(emotion, texture);
        return texture;
    }

    protected void drawBlockedEmotionIcons(SpriteBatch batch) {
        if (allowedEmotions.isEmpty()) {
            return;
        }

        float iconSize = 56f;

        // Near pause button, slightly to the right/left area depending on screen edge.
        float startX = screenWidth - 105f;
        float y = screenHeight - 53f;

        int shown = 0;

        for (Emotion emotion : Emotion.values()) {
            if (emotion == Emotion.NEUTRAL) {
                continue;
            }

            if (allowedEmotions.contains(emotion)) {
                continue;
            }

            Texture symbol = getEmotionSymbolTexture(emotion);

            if (symbol == null) {
                continue;
            }

            float x = startX - shown * (iconSize + 8f);

            batch.draw(symbol, x, y, iconSize, iconSize);
            batch.draw(getBlockedCrossTexture(), x, y, iconSize, iconSize);

            shown++;
        }
    }


    protected void drawForegroundSprites(SpriteBatch batch) {
    }

}
