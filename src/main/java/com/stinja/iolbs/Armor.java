package com.stinja.iolbs;

public enum Armor {
    NONE(5, "unarmored")
    , LEATHER(4, "lightly armored")
    , MAILE(2, "mailled");

    public final int ac;
    private final String description;
    Armor(int ac, String description) {
        this.ac = ac;
        this.description = description;
    }

    public String toString() {
        return description;
    }
}
