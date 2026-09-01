package com.letsfaceittogether.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenCoords {
    public static final float VIRTUAL_WIDTH = 960;
    public static final float VIRTUAL_HEIGHT = 540;

    private static Viewport viewport;
    private static Vector3 mouse = new Vector3();

    public static void setViewport(Viewport viewport) {
        ScreenCoords.viewport = viewport;
    }

    public static float mouseX() {
        if (viewport == null) {
            return Gdx.input.getX();
        }

        mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        return mouse.x;
    }

    public static float mouseY() {
        if (viewport == null) {
            return Gdx.graphics.getHeight() - Gdx.input.getY();
        }

        mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mouse);
        return mouse.y;
    }
}
