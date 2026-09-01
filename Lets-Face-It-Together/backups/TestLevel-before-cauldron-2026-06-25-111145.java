package com.letsfaceittogether.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letsfaceittogether.objects.MainSumTimer;
import com.letsfaceittogether.objects.MiniLoopTimer;
import com.letsfaceittogether.objects.SpriteNumberRenderer;
import com.letsfaceittogether.objects.TimerStopButton;

public class TestLevel extends BaseLevel {
    private static final int TARGET_TOTAL_CENTIS = 600; // 6.00 seconds

    private SpriteNumberRenderer numberRenderer;
    private MainSumTimer mainTimer;
    private MiniLoopTimer[] miniTimers;
    private TimerStopButton[] timerButtons;

    private boolean puzzleSolved = false;

    public TestLevel() {
        super();

        setTutorialText("TEST LEVEL: Stop all 3 mini timers. If their total is under 6.00, the key appears.");

        numberRenderer = new SpriteNumberRenderer();

        mainTimer = new MainSumTimer(420, 400);

        miniTimers = new MiniLoopTimer[] {
            new MiniLoopTimer(120, 280),
            new MiniLoopTimer(420, 280),
            new MiniLoopTimer(720, 280)
        };

        timerButtons = new TimerStopButton[] {
            new TimerStopButton(145, 90, 70, 45),
            new TimerStopButton(445, 90, 70, 45),
            new TimerStopButton(745, 90, 70, 45)
        };

        player.x = 70;
        player.y = 90;

        key.x = -5000;
        key.y = 130;

        door.x = 840;
        door.y = 90;
    }

    @Override
    protected Color getBackgroundColor() {
        return new Color(0.14f, 0.06f, 0.06f, 1f);
    }

    @Override
    protected void updateLevel(float delta) {
        for (TimerStopButton button : timerButtons) {
            button.blockPlayer(player);
            button.update(player);
        }

        for (int i = 0; i < miniTimers.length; i++) {
            if (timerButtons[i].isPressed()) {
                miniTimers[i].stop();
            }
            miniTimers[i].update(delta);
        }

        int stoppedCount = getStoppedCount();
        int stoppedTotal = getStoppedTotalCentis();

        if (stoppedTotal > TARGET_TOTAL_CENTIS) {
            resetTimerPuzzle();
            return;
        }

        puzzleSolved = stoppedCount == miniTimers.length && stoppedTotal <= TARGET_TOTAL_CENTIS;

        if (puzzleSolved) {
            key.x = 470;
            key.y = 160;
        } else {
            key.x = -5000;
            key.y = 160;
        }
    }

    private void resetTimerPuzzle() {
        System.out.println("Main timer exceeded target. Resetting timer puzzle.");

        for (MiniLoopTimer timer : miniTimers) {
            timer.reset();
        }

        for (TimerStopButton button : timerButtons) {
            button.reset();
        }

        puzzleSolved = false;

        key.x = -5000;
        key.y = 160;

        player.x = 70;
        player.y = 90;
        player.previousX = player.x;
        player.previousY = player.y;
        player.velocityY = 0.0f;
        player.onGround = true;
    }

    private int getStoppedCount() {
        int count = 0;
        for (MiniLoopTimer timer : miniTimers) {
            if (timer.isStopped()) {
                count++;
            }
        }
        return count;
    }

    private int getStoppedTotalCentis() {
        int total = 0;
        for (MiniLoopTimer timer : miniTimers) {
            if (timer.isStopped()) {
                total += timer.getCentis();
            }
        }
        return total;
    }

    @Override
    protected void drawLevelSprites(SpriteBatch batch) {
        for (MiniLoopTimer timer : miniTimers) {
            timer.draw(batch, numberRenderer);
        }

        for (TimerStopButton button : timerButtons) {
            button.draw(batch);
        }

        mainTimer.draw(batch, numberRenderer, getStoppedTotalCentis());
    }

    @Override
    protected void drawLevelObjects(ShapeRenderer shapeRenderer) {
        for (TimerStopButton button : timerButtons) {
            button.draw(shapeRenderer);
        }
    }
}
