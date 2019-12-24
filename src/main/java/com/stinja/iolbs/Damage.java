package com.stinja.iolbs;

public enum Damage
    { NORMAL(3)
    , COLD(5)
    , FIRE(5)
    , LIGHTNING(5)
    ;

    public final int amount;

    Damage(int amount) {
        this.amount = amount;
    }
}