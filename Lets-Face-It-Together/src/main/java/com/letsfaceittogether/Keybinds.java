package com.letsfaceittogether;

import com.badlogic.gdx.Input;

public class Keybinds {
    public static int jumpKey = Input.Keys.SPACE;
    public static int leftKey = Input.Keys.A;
    public static int rightKey = Input.Keys.D;
    public static int enterDoorKey = Input.Keys.S;
    public static int restartKey = Input.Keys.R;

    public static String keyName(int key) {
        return Input.Keys.toString(key);
    }
}
