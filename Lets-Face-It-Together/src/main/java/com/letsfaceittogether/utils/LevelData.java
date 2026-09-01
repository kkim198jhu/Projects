package com.letsfaceittogether.utils;

import com.letsfaceittogether.Emotion;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelData {
    public java.util.HashSet<Emotion> allowedEmotions = new java.util.HashSet<>();

    public String tutorialText = "";
    public float levelWidth = 960;

    public Color background = new Color(0.05f, 0.05f, 0.08f, 1f);

    public float playerX = 100;
    public float playerY = 90;

    public float keyX = 720;
    public float keyY = 130;

    public float doorX = 830;
    public float doorY = 90;

    public List<LevelObjectData> objects = new ArrayList<>();

    public static LevelData load(String path) {
        LevelData data = new LevelData();

        JsonValue root = new JsonReader().parse(Gdx.files.internal(path));

        data.tutorialText = root.getString("tutorialText", "");
        data.levelWidth = root.getFloat("levelWidth", 960);

        JsonValue background = root.get("background");
        if (background != null) {
            data.background = new Color(
                    background.getFloat("r", 0.05f),
                    background.getFloat("g", 0.05f),
                    background.getFloat("b", 0.08f),
                    1f
            );
        }

        JsonValue playerStart = root.get("playerStart");
        if (playerStart != null) {
            data.playerX = playerStart.getFloat("x", 100);
            data.playerY = playerStart.getFloat("y", 90);
        }

        JsonValue key = root.get("key");
        if (key != null) {
            data.keyX = key.getFloat("x", 720);
            data.keyY = key.getFloat("y", 130);
        }

        JsonValue door = root.get("door");
        if (door != null) {
            data.doorX = door.getFloat("x", 830);
            data.doorY = door.getFloat("y", 90);
        }

        JsonValue objectsJson = root.get("objects");
        if (objectsJson != null) {
            for (JsonValue objectJson = objectsJson.child; objectJson != null; objectJson = objectJson.next) {
                LevelObjectData object = new LevelObjectData();

                object.type = objectJson.getString("type", "");
                object.id = objectJson.getString("id", "");

                JsonValue opensValue = objectJson.get("opens");
                if (opensValue != null && opensValue.isArray()) {
                    for (JsonValue openId = opensValue.child; openId != null; openId = openId.next) {
                        object.opensList.add(openId.asString());
                    }
                    object.opens = String.join(",", object.opensList);
                } else {
                    object.opens = objectJson.getString("opens", "");
                    if (object.opens != null && object.opens.length() > 0) {
                        object.opensList.addAll(Arrays.asList(object.opens.split(",")));
                    }
                }
                object.x = objectJson.getFloat("x", 0);
                object.y = objectJson.getFloat("y", 0);
                object.w = objectJson.getFloat("w", 50);
                object.h = objectJson.getFloat("h", 50);
                object.length = objectJson.getFloat("length", 0);

                data.objects.add(object);
            }
        }

        
        JsonValue allowedEmotionsJson = root.get("allowedEmotions");
        if (allowedEmotionsJson != null && allowedEmotionsJson.isArray()) {
            for (JsonValue emotionValue = allowedEmotionsJson.child; emotionValue != null; emotionValue = emotionValue.next) {
                try {
                    data.allowedEmotions.add(Emotion.valueOf(emotionValue.asString()));
                } catch (IllegalArgumentException ignored) {
                    System.out.println("Unknown allowed emotion in JSON: " + emotionValue.asString());
                }
            }
        }

return data;
    }

    public static class LevelObjectData {
        public String type;
        public String id = "";
        public String opens = "";
        public ArrayList<String> opensList = new ArrayList<>();
        public float x;
        public float y;
        public float w;
        public float h;
        public float length;
    }
}
