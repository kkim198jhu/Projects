package com.letsfaceittogether;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Player {
    public boolean ignoreBaseFloor = false;
    public boolean gravityInverted = false;
    public boolean pushingBox = false;
    public float x = 100;
    public float y = 90;

    public float previousX = x;
    public float previousY = y;

    public float width = 50;
    public float height = 50;

    public float worldWidth = 960;

    public float speed = 220;
    public float velocityY = 0;

    public Emotion emotion = Emotion.NEUTRAL;

    private final float gravity = -1200;
    private final float jumpPower = 520;
    private final float floorY = 90;

    public boolean onGround = true;

    private float groundSupportMinX = -999999f;
    private float groundSupportMaxX = 999999f;
    private float groundSupportY = 90f;
    private float groundSupportTimer = 0f;
    private boolean groundSupportIsObject = false;

    // 1 = facing right, -1 = facing left
    public int facingDirection = 1;

    private boolean movingHorizontally = false;

    private SpriteSet normalSprites;
    private SpriteSet happySprites;
    private SpriteSet angrySprites;
    private SpriteSet sadSprites;
    private SpriteSet fearSprites;
    private SpriteSet surpriseSprites;
    private SpriteSet disgustSprites;

    private TextureRegion[] angryPushFramesRight;
    private TextureRegion[] angryPushFramesLeft;
    private boolean angryPushSpritesLoaded = false;

    private float animationTimer = 0f;
    private float frameDuration = 0.14f;
    private int walkFrameIndex = 0;

    public Player() {
        normalSprites = new SpriteSet("Normal");
        happySprites = new SpriteSet("Happy");
        angrySprites = new SpriteSet("Angry");
        sadSprites = new SpriteSet("Sad");
        fearSprites = new SpriteSet("Fear");
        surpriseSprites = new SpriteSet("Surprise");
        disgustSprites = new SpriteSet("Disgust");

        loadAngryPushingSprites();
    }

    public void setGravityInverted(boolean inverted) {
        if (gravityInverted != inverted) {
            gravityInverted = inverted;
            velocityY = 0.0f;
            onGround = false;
        }
    }

    public void setGroundSupport(float minX, float maxX, float supportY) {
        groundSupportMinX = minX;
        groundSupportMaxX = maxX;
        groundSupportY = supportY;
        groundSupportTimer = 0.10f;
        groundSupportIsObject = true;
        onGround = true;
    }

    private void validateGroundSupport(float delta) {
        if (!onGround) {
            return;
        }

        // Normal base floor support.
        if (!ignoreBaseFloor && y <= floorY + 4.0f) {
            groundSupportMinX = -999999f;
            groundSupportMaxX = 999999f;
            groundSupportY = floorY;
            groundSupportTimer = 0.10f;
            groundSupportIsObject = false;
            return;
        }

        float centerX = x + width / 2.0f;

        boolean stillOverSupport =
            centerX >= groundSupportMinX - 8.0f &&
            centerX <= groundSupportMaxX + 8.0f;

        // For object support, allow small physics drift but not real air.
        boolean stillNearObjectSupport =
            y >= groundSupportY - 28.0f &&
            y <= groundSupportY + 28.0f;

        if (groundSupportIsObject) {
            if (!stillOverSupport || !stillNearObjectSupport) {
                onGround = false;
            }
            return;
        }

        // If not on base floor and not on object support, not grounded.
        onGround = false;
    }

    public void updateMovement(float delta) {
        previousX = x;
        previousY = y;

        movingHorizontally = false;

        // Reset every frame. MovableBox sets this true only while actually pushing.
        pushingBox = false;

        // If the base floor is disabled, do not let stale onGround allow a midair jump.
        // This matters for pits/holes where the player walks off without jumping.
        if (ignoreBaseFloor && y <= floorY + 4.0f) {
            onGround = false;
        }

        float moveSpeed = speed;

        if (emotion == Emotion.FEAR) {
            moveSpeed = 360;
        }

        if (Gdx.input.isKeyPressed(Keybinds.leftKey)) {
            x -= moveSpeed * delta;
            facingDirection = -1;
            movingHorizontally = true;
        }

        if (Gdx.input.isKeyPressed(Keybinds.rightKey)) {
            x += moveSpeed * delta;
            facingDirection = 1;
            movingHorizontally = true;
        }

        validateGroundSupport(delta);

        if (Gdx.input.isKeyJustPressed(Keybinds.jumpKey) && onGround) {
            velocityY = gravityInverted ? -jumpPower : jumpPower;

            if (emotion == Emotion.FEAR) {
                velocityY = gravityInverted ? -620 : 620;
            }

            onGround = false;
        }

        float effectiveGravity = gravityInverted ? -gravity : gravity;
        velocityY += effectiveGravity * delta;
        y += velocityY * delta;

        if (!gravityInverted && !ignoreBaseFloor && y <= floorY) {
            y = floorY;
            velocityY = 0;
            groundSupportMinX = -999999f;
            groundSupportMaxX = 999999f;
            groundSupportY = floorY;
            groundSupportTimer = 0.10f;
            groundSupportIsObject = false;
            onGround = true;
        }

        if (x < 0) {
            x = 0;
        }

        if (x + width > worldWidth) {
            x = worldWidth - width;
        }

        updateShapeFromEmotion();
        updateWalkAnimation(delta);
    }

    private void updateWalkAnimation(float delta) {
        if (!movingHorizontally) {
            animationTimer = 0f;
            walkFrameIndex = 0;
            return;
        }

        animationTimer += delta;

        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            walkFrameIndex++;

            if (walkFrameIndex >= 4) {
                walkFrameIndex = 0;
            }
        }
    }

    public void handleEmotionKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0) || Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            emotion = Emotion.NEUTRAL;
            System.out.println("Neutral");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            emotion = Emotion.HAPPY;
            System.out.println("Happy: heavy body");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            emotion = Emotion.ANGRY;
            System.out.println("Angry: break walls");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            emotion = Emotion.SAD;
            System.out.println("Sad: curl into ball");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            emotion = Emotion.FEAR;
            System.out.println("Fear: run faster");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            emotion = Emotion.SURPRISE;
            System.out.println("Surprise: reveal hidden objects");
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            emotion = Emotion.DISGUST;
            System.out.println("Disgust: repel ghost");
        }
    }

    public void revertToPreviousPosition() {
        x = previousX;
        y = previousY;
        velocityY = 0;
    }

    private void updateShapeFromEmotion() {
        // Same gameplay hitbox for every emotion.
        // Slightly taller so the character's head/eyes do not visually enter walls.
        width = 50;
        height = 85;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (getCurrentSpriteSet() != null && getCurrentSpriteSet().loaded) {
            return;
        }

        // Fallback colored shapes if a sprite fails to load.
        if (emotion == Emotion.HAPPY) {
            shapeRenderer.setColor(Color.PINK);
        } else if (emotion == Emotion.ANGRY) {
            shapeRenderer.setColor(Color.RED);
        } else if (emotion == Emotion.SAD) {
            shapeRenderer.setColor(Color.CYAN);
        } else if (emotion == Emotion.FEAR) {
            shapeRenderer.setColor(Color.PURPLE);
        } else if (emotion == Emotion.SURPRISE) {
            shapeRenderer.setColor(Color.WHITE);
        } else if (emotion == Emotion.DISGUST) {
            shapeRenderer.setColor(Color.GREEN);
        } else {
            shapeRenderer.setColor(Color.LIGHT_GRAY);
        }

        shapeRenderer.rect(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        SpriteSet set = getCurrentSpriteSet();

        if (set == null || !set.loaded) {
            return;
        }

        TextureRegion currentFrame;

        if (emotion == Emotion.ANGRY && pushingBox && angryPushSpritesLoaded) {
            TextureRegion[] pushFrames = facingDirection == 1 ? angryPushFramesRight : angryPushFramesLeft;
            currentFrame = pushFrames[walkFrameIndex % pushFrames.length];
        } else {
            TextureRegion[] frames = facingDirection == 1 ? set.walkFramesRight : set.walkFramesLeft;
            currentFrame = frames[walkFrameIndex];
        }

        // Same visual anchor for every emotion.
        float visualWidth = 120;
        float visualHeight = 120;
        float visualX = x - 35;
        float visualY = y - 20;

        if (gravityInverted) {
            // Flip the player sprite vertically when gravity is inverted.
            batch.draw(currentFrame, visualX, visualY + visualHeight, visualWidth, -visualHeight);
        } else {
            batch.draw(currentFrame, visualX, visualY, visualWidth, visualHeight);
        }
    }

    private void loadAngryPushingSprites() {
        try {
            Texture pushRight1 = new Texture("assets/sprites/player/Angry/Pushing/Looking Right (Angry) (Left Leg Up) (Pushing Right).png");
            Texture pushRight2 = new Texture("assets/sprites/player/Angry/Pushing/Looking Right (Angry) (Right Leg Up) (Pushing Right).png");

            TextureRegion r1 = new TextureRegion(pushRight1);
            TextureRegion r2 = new TextureRegion(pushRight2);

            TextureRegion l1 = new TextureRegion(pushRight1);
            TextureRegion l2 = new TextureRegion(pushRight2);
            l1.flip(true, false);
            l2.flip(true, false);

            angryPushFramesRight = new TextureRegion[] { r1, r2 };
            angryPushFramesLeft = new TextureRegion[] { l1, l2 };

            angryPushSpritesLoaded = true;
            System.out.println("Loaded Angry pushing sprites.");
        } catch (GdxRuntimeException e) {
            angryPushSpritesLoaded = false;
            System.out.println("Could not load Angry pushing sprites.");
            System.out.println(e.getMessage());
        }
    }

    private SpriteSet getCurrentSpriteSet() {
        if (emotion == Emotion.HAPPY) {
            return happySprites;
        } else if (emotion == Emotion.ANGRY) {
            return angrySprites;
        } else if (emotion == Emotion.SAD) {
            return sadSprites;
        } else if (emotion == Emotion.FEAR) {
            return fearSprites;
        } else if (emotion == Emotion.SURPRISE) {
            return surpriseSprites;
        } else if (emotion == Emotion.DISGUST) {
            return disgustSprites;
        } else {
            return normalSprites;
        }
    }

    private static class SpriteSet {
        Texture idleTexture;
        Texture walkRightUpTexture;
        Texture walkLeftUpTexture;

        TextureRegion[] walkFramesRight;
        TextureRegion[] walkFramesLeft;

        boolean loaded = false;

        SpriteSet(String folderName) {
            try {
                idleTexture = new Texture("assets/sprites/player/" + folderName + "/idle.png");
                walkRightUpTexture = new Texture("assets/sprites/player/" + folderName + "/walk_right_up.png");
                walkLeftUpTexture = new Texture("assets/sprites/player/" + folderName + "/walk_left_up.png");

                walkFramesRight = makeRightFrames(idleTexture, walkRightUpTexture, walkLeftUpTexture);
                walkFramesLeft = makeLeftFrames(idleTexture, walkRightUpTexture, walkLeftUpTexture);

                loaded = true;
                System.out.println("Loaded " + folderName + " player sprites.");
            } catch (GdxRuntimeException e) {
                loaded = false;
                System.out.println("Could not load " + folderName + " player sprites. Using placeholder.");
            }
        }

        private TextureRegion[] makeRightFrames(Texture idle, Texture rightUp, Texture leftUp) {
            TextureRegion idleRight = new TextureRegion(idle);
            TextureRegion rightUpRight = new TextureRegion(rightUp);
            TextureRegion leftUpRight = new TextureRegion(leftUp);

            return new TextureRegion[] {
                    idleRight,
                    rightUpRight,
                    leftUpRight,
                    rightUpRight
            };
        }

        private TextureRegion[] makeLeftFrames(Texture idle, Texture rightUp, Texture leftUp) {
            TextureRegion idleLeft = new TextureRegion(idle);
            idleLeft.flip(true, false);

            TextureRegion rightUpLeft = new TextureRegion(rightUp);
            rightUpLeft.flip(true, false);

            TextureRegion leftUpLeft = new TextureRegion(leftUp);
            leftUpLeft.flip(true, false);

            return new TextureRegion[] {
                    idleLeft,
                    rightUpLeft,
                    leftUpLeft,
                    rightUpLeft
            };
        }
    }
}
