package com.stinja.iolbs;

public enum Action {
    ENGAGE (false)
    , FIRE (true)
    ;

    public final boolean full;

    Action(boolean full) {
        this.full = full;
    }

}
