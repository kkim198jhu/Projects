package com.letsfaceittogether.utils;

public class Collision {
    public static boolean overlaps(float ax, float ay, float aw, float ah,
                                   float bx, float by, float bw, float bh) {
        return ax < bx + bw &&
               ax + aw > bx &&
               ay < by + bh &&
               ay + ah > by;
    }
}
