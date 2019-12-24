package com.stinja.iolbs.rules;

public enum Threat
    { NORMAL(true, 3,5)
    , MISSILE (false, 3, 2)
    , SNAP_SHOT (false, 3, 4)
    ;

    public final boolean attack;
    public final int amount;
    public final int ddc;

    Threat(boolean attack, int amount, int ddc) {
        this.attack = attack;
        this.amount = amount;
        this.ddc = ddc;
    }
}