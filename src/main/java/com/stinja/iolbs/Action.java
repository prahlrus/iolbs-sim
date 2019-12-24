package com.stinja.iolbs;

public enum Action
    { AIM
            (0
            , true
            , Damage.NORMAL
            , Save.MISSILES
            )
    , FIGHT
            (1
            , true
            , Damage.NORMAL
            , null
            )
    ;

    public final int beats;
    public final boolean full;
    public final Damage source;
    public final Save save;

    Action(int beats, boolean full, Damage source, Save save) {
        this.beats = beats;
        this.full = full;
        this.source = source;
        this.save = save;
    }

}
