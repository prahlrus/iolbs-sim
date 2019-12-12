package com.stinja.iolbs;

public enum Damage {
    LIGHT(1, "light damage"), FULL(3, "damage"), HEAVY(5, "heavy damage");

    public final int amount;
    private final String description;
    Damage(int amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    public String toString() {
        return description;
    }
}