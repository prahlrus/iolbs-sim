package com.stinja.iolbs;

public class Dice {
    public static int check() {
        return 2 + (int) (Math.random() * 6) + (int) (Math.random() * 6);
    }

    public static int hit(int hd) {
        int result = 0;
        while (hd-- > 0)
            result += Math.random() * 6;
        return result;
    }
}
