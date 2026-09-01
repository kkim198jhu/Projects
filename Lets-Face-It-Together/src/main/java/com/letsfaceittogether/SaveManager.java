package com.letsfaceittogether;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class SaveManager {
    public static int currentSlot = -1;

    public static int highestUnlockedWorld = 0;
    public static int highestUnlockedLevel = 0;
    public static boolean[] completedLevels = new boolean[6];

    private static String[] worldNames = {
            "Neutral World",
            "Happy World",
            "Angry World",
            "Sad World",
            "Fear World",
            "Surprise World",
            "Disgust World"
    };

    private static String[] levelNames = {
            "Happy",
            "Angry",
            "Sad",
            "Fear",
            "Surprise",
            "Disgust"
    };

    private static File saveDirectory() {
        File dir = new File("saves");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private static File saveFile(int slot) {
        return new File(saveDirectory(), "save" + slot + ".properties");
    }

    public static boolean slotExists(int slot) {
        return saveFile(slot).exists();
    }

    public static void loadSlot(int slot) {
        currentSlot = slot;

        if (!slotExists(slot)) {
            resetData();
            save();
            return;
        }

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(saveFile(slot)));

            highestUnlockedWorld = Integer.parseInt(props.getProperty("highestUnlockedWorld", "0"));
            highestUnlockedLevel = Integer.parseInt(props.getProperty("highestUnlockedLevel", "0"));

            String completed = props.getProperty("completedLevels", "false,false,false,false,false,false");
            String[] parts = completed.split(",");

            for (int i = 0; i < completedLevels.length; i++) {
                completedLevels[i] = i < parts.length && Boolean.parseBoolean(parts[i]);
            }

            Keybinds.jumpKey = Integer.parseInt(props.getProperty("jumpKey", "" + Keybinds.jumpKey));
            Keybinds.leftKey = Integer.parseInt(props.getProperty("leftKey", "" + Keybinds.leftKey));
            Keybinds.rightKey = Integer.parseInt(props.getProperty("rightKey", "" + Keybinds.rightKey));
            Keybinds.enterDoorKey = Integer.parseInt(props.getProperty("enterDoorKey", "" + Keybinds.enterDoorKey));
            Keybinds.restartKey = Integer.parseInt(props.getProperty("restartKey", "" + Keybinds.restartKey));

            System.out.println("Loaded save slot " + slot);
        } catch (Exception e) {
            System.out.println("Could not load save slot " + slot + ". Starting fresh.");
            resetData();
            save();
        }
    }

    public static void overwriteSlot(int slot) {
        currentSlot = slot;
        resetData();
        save();
        System.out.println("Overwrote save slot " + slot);
    }

    public static void saveProgress(int unlockedWorld, int unlockedLevel, boolean[] completed) {
        highestUnlockedWorld = unlockedWorld;
        highestUnlockedLevel = unlockedLevel;

        for (int i = 0; i < completedLevels.length && i < completed.length; i++) {
            completedLevels[i] = completed[i];
        }

        save();
    }

    public static void saveKeybinds() {
        save();
    }

    public static String slotDescription(int slot) {
        if (!slotExists(slot)) {
            return "Empty Slot";
        }

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(saveFile(slot)));

            int world = Integer.parseInt(props.getProperty("highestUnlockedWorld", "0"));
            int level = Integer.parseInt(props.getProperty("highestUnlockedLevel", "0"));
            String completed = props.getProperty("completedLevels", "false,false,false,false,false,false");

            int completedCount = 0;
            for (String value : completed.split(",")) {
                if (Boolean.parseBoolean(value)) {
                    completedCount++;
                }
            }

            if (world < 0) world = 0;
            if (world >= worldNames.length) world = worldNames.length - 1;

            if (level < 0) level = 0;
            if (level >= levelNames.length) level = levelNames.length - 1;

            return worldNames[world] + " - " + levelNames[level] + " (" + (level + 1) + "/6)";
        } catch (Exception e) {
            return "Save Slot";
        }
    }

    private static void save() {
        if (currentSlot < 1 || currentSlot > 3) {
            return;
        }

        try {
            Properties props = new Properties();

            props.setProperty("highestUnlockedWorld", "" + highestUnlockedWorld);
            props.setProperty("highestUnlockedLevel", "" + highestUnlockedLevel);

            StringBuilder completed = new StringBuilder();
            for (int i = 0; i < completedLevels.length; i++) {
                completed.append(completedLevels[i]);
                if (i < completedLevels.length - 1) {
                    completed.append(",");
                }
            }

            props.setProperty("completedLevels", completed.toString());

            props.setProperty("jumpKey", "" + Keybinds.jumpKey);
            props.setProperty("leftKey", "" + Keybinds.leftKey);
            props.setProperty("rightKey", "" + Keybinds.rightKey);
            props.setProperty("enterDoorKey", "" + Keybinds.enterDoorKey);
            props.setProperty("restartKey", "" + Keybinds.restartKey);

            props.store(new FileOutputStream(saveFile(currentSlot)), "Let's Face It Together Save Data");
        } catch (Exception e) {
            System.out.println("Could not save data.");
        }
    }

    private static void resetData() {
        highestUnlockedWorld = 0;
        highestUnlockedLevel = 0;

        for (int i = 0; i < completedLevels.length; i++) {
            completedLevels[i] = false;
        }

        Keybinds.jumpKey = com.badlogic.gdx.Input.Keys.SPACE;
        Keybinds.leftKey = com.badlogic.gdx.Input.Keys.A;
        Keybinds.rightKey = com.badlogic.gdx.Input.Keys.D;
        Keybinds.enterDoorKey = com.badlogic.gdx.Input.Keys.S;
        Keybinds.restartKey = com.badlogic.gdx.Input.Keys.R;
    }
}
