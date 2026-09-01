package com.letsfaceittogether;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    public float x = 100;
    public float y = 90;

    public float width = 50;
    public float height = 50;

    public float speed = 220;
    public float velocityY = 0;

    public String emotion = "neutral";

    private final float gravity = -1200;
    private final float jumpPower = 520;
    private final float floorY = 90;

    public boolean onGround = true;

    public void updateMovement(float delta) {
        float moveSpeed = speed;

        if (emotion.equals("fear")) {
            moveSpeed = 360;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= moveSpeed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += moveSpeed * delta;
        }

        if ((Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.W)) && onGround) {
            velocityY = jumpPower;

            if (emotion.equals("fear")) {
                velocityY = 620;
            }

            onGround = false;
        }

        velocityY += gravity * delta;
        y += velocityY * delta;

        if (y <= floorY) {
            y = floorY;
            velocityY = 0;
            onGround = true;
        }

        if (x < 0) {
            x = 0;
        }

        if (x + width > Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - width;
        }

        updateShapeFromEmotion();
    }

    public void handleEmotionKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            emotion = "neutral";
            System.out.println("Neutral");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            emotion = "happy";
            System.out.println("Happy: heavy body");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            emotion = "angry";
            System.out.println("Angry: break walls");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            emotion = "sad";
            System.out.println("Sad: curl into ball");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            emotion = "fear";
            System.out.println("Fear: run faster");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            emotion = "surprise";
            System.out.println("Surprise: reveal hidden objects");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            emotion = "disgust";
            System.out.println("Disgust: repel ghost");
        }
    }

    private void updateShapeFromEmotion() {
        if (emotion.equals("happy")) {
            width = 80;
            height = 60;
        } else if (emotion.equals("sad")) {
            width = 45;
            height = 45;
        } else if (emotion.equals("fear")) {
            width = 40;
            height = 55;
        } else if (emotion.equals("angry")) {
            width = 60;
            height = 50;
        } else {
            width = 50;
            height = 50;
        }
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (emotion.equals("happy")) {
            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.rect(x, y, width, height);
        } else if (emotion.equals("angry")) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(x, y, width, height);
        } else if (emotion.equals("sad")) {
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.circle(x + width / 2, y + height / 2, width / 2);
        } else if (emotion.equals("fear")) {
            shapeRenderer.setColor(Color.PURPLE);
            shapeRenderer.rect(x, y, width, height);
        } else if (emotion.equals("surprise")) {
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.rect(x, y, width, height);
        } else if (emotion.equals("disgust")) {
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(x, y, width, height);
        } else {
            shapeRenderer.setColor(Color.LIGHT_GRAY);
            shapeRenderer.rect(x, y, width, height);
        }
    }
}
