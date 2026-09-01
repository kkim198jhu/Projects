package com.letsfaceittogether;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Matrix4;
import com.letsfaceittogether.screens.*;
import com.letsfaceittogether.utils.ScreenCoords;

public class LetsFaceItTogether extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameScreenType currentScreen;

    private TitleScreen titleScreen;
    private SaveSelectScreen saveSelectScreen;
    private SettingsScreen settingsScreen;
    private WorldSelectScreen worldSelectScreen;
    private LevelSelectScreen levelSelectScreen;
    private HappyWorldLevelSelectScreen happyWorldLevelSelectScreen;
    private BaseLevel currentLevel;

    private int highestUnlockedWorld = 0;
    private int highestUnlockedLevel = 0;

    private boolean[] completedLevels = new boolean[6];

    private int currentLevelIndex = -1;
    private GameScreenType currentLevelType = null;

    private boolean currentLevelCompletionHandled = false;
    private float levelCompleteTimer = 0f;

    private boolean paused = false;

    private float pauseButtonX = 895;
    private float pauseButtonY = 475;
    private float pauseButtonSize = 45;

    private float settingsButtonX = 330;
    private float settingsButtonY = 270;
    private float returnButtonX = 330;
    private float returnButtonY = 180;
    private float pauseMenuButtonW = 300;
    private float pauseMenuButtonH = 65;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2.0f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(ScreenCoords.VIRTUAL_WIDTH, ScreenCoords.VIRTUAL_HEIGHT, camera);
        viewport.apply();
        ScreenCoords.setViewport(viewport);

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        currentScreen = GameScreenType.TITLE;

        titleScreen = new TitleScreen();
        saveSelectScreen = new SaveSelectScreen();
        settingsScreen = new SettingsScreen();
        worldSelectScreen = new WorldSelectScreen(highestUnlockedWorld);
        levelSelectScreen = new LevelSelectScreen(highestUnlockedLevel);
        happyWorldLevelSelectScreen = new HappyWorldLevelSelectScreen();
        levelSelectScreen.setCompletedLevels(completedLevels);
    }

    @Override
    public void render() {
        viewport.apply();
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        switch (currentScreen) {
            case TITLE:
                handleTitleScreen();
                break;

            case SAVE_SELECT:
                handleSaveSelectScreen();
                break;

            case SETTINGS:
                handleSettingsScreen();
                break;

            case WORLD_SELECT:
                handleWorldSelectScreen();
                break;

            case WORLD_1_LEVEL_SELECT:
                handleLevelSelectScreen();
                break;

            case WORLD_2_LEVEL_SELECT:
                handleHappyWorldLevelSelectScreen();
                break;

            default:
                handleLevelScreen();
                break;
        }
    }

    private void handleTitleScreen() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            System.out.println("Opening test level.");
            startLevel(GameScreenType.TEST_LEVEL);
            return;
        }

        GameScreenType nextScreen = titleScreen.update();

        if (nextScreen == GameScreenType.SAVE_SELECT) {
            currentScreen = GameScreenType.SAVE_SELECT;
        } else if (nextScreen == GameScreenType.SETTINGS) {
            settingsScreen.setBackTarget(GameScreenType.TITLE);
            currentScreen = GameScreenType.SETTINGS;
        }

        titleScreen.draw(shapeRenderer, batch, font);
        drawTitleTestPrompt();
    }


    private void drawTitleTestPrompt() {
        batch.begin();

        font.setColor(Color.LIGHT_GRAY);
        drawCenteredText(batch, font, "Press T for Test Level", 480, 70, 1.2f);

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void handleSaveSelectScreen() {
        GameScreenType nextScreen = saveSelectScreen.update();

        if (nextScreen == GameScreenType.TITLE) {
            currentScreen = GameScreenType.TITLE;
            titleScreen.printInstructions();
        } else if (nextScreen == GameScreenType.WORLD_SELECT) {
            loadSaveDataIntoGame();
            currentScreen = GameScreenType.WORLD_SELECT;
            worldSelectScreen.printInstructions();
        }

        saveSelectScreen.draw(shapeRenderer, batch, font);
    }

    private void loadSaveDataIntoGame() {
        highestUnlockedWorld = SaveManager.highestUnlockedWorld;
        highestUnlockedLevel = SaveManager.highestUnlockedLevel;

        for (int i = 0; i < completedLevels.length; i++) {
            completedLevels[i] = SaveManager.completedLevels[i];
        }

        levelSelectScreen.setCompletedLevels(completedLevels);
    }

    private void handleSettingsScreen() {
        GameScreenType nextScreen = settingsScreen.update();

        if (nextScreen != GameScreenType.SETTINGS) {
            currentScreen = nextScreen;

            if (currentScreen == GameScreenType.TITLE) {
                titleScreen.printInstructions();
            }
        }

        settingsScreen.draw(shapeRenderer, batch, font);
    }

    private void handleWorldSelectScreen() {
        worldSelectScreen.setHighestUnlockedWorld(highestUnlockedWorld);

        GameScreenType nextScreen = worldSelectScreen.update();

        if (nextScreen == GameScreenType.TITLE) {
            currentScreen = GameScreenType.TITLE;
            titleScreen.printInstructions();
        } else if (nextScreen == GameScreenType.WORLD_1_LEVEL_SELECT) {
            currentScreen = levelSelectForCurrentLevel();
            printCurrentLevelSelectInstructions();
        } else if (nextScreen == GameScreenType.WORLD_2_LEVEL_SELECT) {
            currentScreen = GameScreenType.WORLD_2_LEVEL_SELECT;
            happyWorldLevelSelectScreen.printInstructions();
        }

        worldSelectScreen.draw(shapeRenderer, batch, font);
    }

    private void handleLevelSelectScreen() {
        paused = false;

        levelSelectScreen.setHighestUnlockedLevel(highestUnlockedLevel);
        levelSelectScreen.setCompletedLevels(completedLevels);

        GameScreenType selectedLevel = levelSelectScreen.update();

        if (selectedLevel == GameScreenType.WORLD_SELECT) {
            currentScreen = GameScreenType.WORLD_SELECT;
            worldSelectScreen.printInstructions();
        } else if (selectedLevel != GameScreenType.WORLD_1_LEVEL_SELECT) {
            startLevel(selectedLevel);
        }

        levelSelectScreen.draw(shapeRenderer, batch, font);
    }


    private void handleHappyWorldLevelSelectScreen() {
        paused = false;

        GameScreenType selectedLevel = happyWorldLevelSelectScreen.update();

        if (selectedLevel == GameScreenType.WORLD_SELECT) {
            currentScreen = GameScreenType.WORLD_SELECT;
            worldSelectScreen.printInstructions();
        } else if (selectedLevel != GameScreenType.WORLD_2_LEVEL_SELECT) {
            startLevel(selectedLevel);
        }

        happyWorldLevelSelectScreen.draw(shapeRenderer, batch, font);
    }

    private void handleLevelScreen() {
        if (currentLevel == null) {
            currentScreen = levelSelectForCurrentLevel();
            return;
        }

        boolean openedPauseThisFrame = false;

        if (Gdx.input.isKeyJustPressed(Keybinds.restartKey)) {
            restartCurrentLevel();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !currentLevel.isComplete()) {
            paused = !paused;
            openedPauseThisFrame = paused;
        }

        if (!paused && !currentLevel.isComplete() && pauseButtonClicked()) {
            paused = true;
            openedPauseThisFrame = true;
        }

        float delta = Gdx.graphics.getDeltaTime();

        if (!paused) {
            currentLevel.update(delta);
        }

        currentLevel.draw(shapeRenderer, batch, font);

        if (paused) {
            drawPauseOverlay();

            if (!openedPauseThisFrame) {
                handlePauseInput();
            }

            return;
        }

        if (!currentLevel.isComplete()) {
            drawTopRightPauseButton();
        }

        handleCompletion(delta);
    }

    private void handleCompletion(float delta) {
        if (!currentLevel.isComplete()) {
            return;
        }

        if (!currentLevelCompletionHandled) {
            currentLevelCompletionHandled = true;
            levelCompleteTimer = 0f;

            if (currentLevelIndex >= 0 && currentLevelIndex < completedLevels.length) {
                completedLevels[currentLevelIndex] = true;
            }

            if (currentLevelIndex == highestUnlockedLevel && highestUnlockedLevel < 5) {
                highestUnlockedLevel++;
                System.out.println("Next level unlocked!");
            }

            if (highestUnlockedLevel >= 5) {
                highestUnlockedWorld = Math.max(highestUnlockedWorld, 1);
                System.out.println("World 1 complete! The next world is unlocked, but not built yet.");
            }

            SaveManager.saveProgress(highestUnlockedWorld, highestUnlockedLevel, completedLevels);
        }

        levelCompleteTimer += delta;

        if (levelCompleteTimer >= 1.25f) {
            currentScreen = levelSelectForCurrentLevel();
            paused = false;
            levelSelectScreen.setCompletedLevels(completedLevels);
            printCurrentLevelSelectInstructions();
        }
    }

    private boolean pauseButtonClicked() {
        if (!Gdx.input.justTouched()) {
            return false;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        return isInside(mouseX, mouseY, pauseButtonX, pauseButtonY, pauseButtonSize, pauseButtonSize);
    }

    private void handlePauseInput() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = ScreenCoords.mouseX();
        float mouseY = ScreenCoords.mouseY();

        if (isInside(mouseX, mouseY, settingsButtonX, settingsButtonY, pauseMenuButtonW, pauseMenuButtonH)) {
            settingsScreen.setBackTarget(currentLevelType);
            currentScreen = GameScreenType.SETTINGS;
            return;
        }

        if (isInside(mouseX, mouseY, returnButtonX, returnButtonY, pauseMenuButtonW, pauseMenuButtonH)) {
            paused = false;
            currentScreen = levelSelectForCurrentLevel();
            printCurrentLevelSelectInstructions();
        }
    }

    private void resetToScreenSpace() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setTransformMatrix(new Matrix4().idt());

        batch.setProjectionMatrix(camera.combined);
        batch.setTransformMatrix(new Matrix4().idt());
    }

    private void drawTopRightPauseButton() {
        resetToScreenSpace();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(pauseButtonX, pauseButtonY, pauseButtonSize, pauseButtonSize);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(pauseButtonX + 13, pauseButtonY + 10, 6, 25);
        shapeRenderer.rect(pauseButtonX + 26, pauseButtonY + 10, 6, 25);

        shapeRenderer.end();
    }

    private void drawPauseOverlay() {
        resetToScreenSpace();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.35f));
        shapeRenderer.rect(0, 0, ScreenCoords.VIRTUAL_WIDTH, ScreenCoords.VIRTUAL_HEIGHT);

        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(settingsButtonX, settingsButtonY, pauseMenuButtonW, pauseMenuButtonH);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(returnButtonX, returnButtonY, pauseMenuButtonW, pauseMenuButtonH);

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        font.setColor(Color.WHITE);
        drawCenteredText(batch, font, "PAUSE", 480, 430, 4.0f);

        drawCenteredText(batch, font, "Settings", settingsButtonX + pauseMenuButtonW / 2f, settingsButtonY + 42, 2.0f);
        drawCenteredText(batch, font, "Return to Neutral World", returnButtonX + pauseMenuButtonW / 2f, returnButtonY + 42, 1.35f);

        font.setColor(Color.LIGHT_GRAY);
        drawCenteredText(batch, font, "Press ESC to resume | Press " + Keybinds.keyName(Keybinds.restartKey) + " to restart", 480, 90, 1.2f);

        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);

        batch.end();
    }

    private void restartCurrentLevel() {
        if (currentLevelType != null) {
            System.out.println("Restarting level.");
            startLevel(currentLevelType);
        }
    }


    private GameScreenType levelSelectForCurrentLevel() {
        if (currentLevelType == GameScreenType.HAPPY_WORLD_LEVEL_1) {
            return GameScreenType.WORLD_2_LEVEL_SELECT;
        }

        return GameScreenType.WORLD_1_LEVEL_SELECT;
    }

    private void printCurrentLevelSelectInstructions() {
        if (currentScreen == GameScreenType.WORLD_2_LEVEL_SELECT) {
            happyWorldLevelSelectScreen.printInstructions();
        } else {
            levelSelectScreen.printInstructions();
        }
    }

    private void startLevel(GameScreenType levelType) {
        currentScreen = levelType;
        currentLevelType = levelType;
        currentLevelCompletionHandled = false;
        levelCompleteTimer = 0f;
        paused = false;

        switch (levelType) {
            case TEST_LEVEL:
                currentLevel = new TestLevel();
                currentLevelIndex = -1;
                break;

            case HAPPY_WORLD_LEVEL_1:
                currentLevel = new HappyLevel("assets/levels/happy/Happy 1.json");
                currentLevelIndex = -1;
                break;

            case HAPPY_LEVEL:
                currentLevel = new HappyLevel();
                currentLevelIndex = 0;
                break;

            case ANGRY_LEVEL:
                currentLevel = new AngryLevel();
                currentLevelIndex = 1;
                break;

            case SAD_LEVEL:
                currentLevel = new SadLevel();
                currentLevelIndex = 2;
                break;

            case FEAR_LEVEL:
                currentLevel = new FearLevel();
                currentLevelIndex = 3;
                break;

            case SURPRISE_LEVEL:
                currentLevel = new SurpriseLevel();
                currentLevelIndex = 4;
                break;

            case DISGUST_LEVEL:
                currentLevel = new DisgustLevel();
                currentLevelIndex = 5;
                break;

            default:
                currentScreen = GameScreenType.WORLD_1_LEVEL_SELECT;
                currentLevelIndex = -1;
                currentLevelType = null;
                break;
        }
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

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Let's Face It Together");
        config.setWindowedMode(960, 540);
        config.useVsync(true);

        new Lwjgl3Application(new LetsFaceItTogether(), config);
    }
}
