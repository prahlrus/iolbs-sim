package com.stinja.iolbs;

public enum Action {
    ENGAGE
            (false
            , null
            , null
            )
    , FIRE
            (true
            , Damage.MISSILE
            , Save.MISSILES
            )
    , FIGHT
            (true
            , Damage.WEAPON
            , null
            )
    ;

    public final boolean full;
    public final Damage source;
    public final Save save;

    Action(boolean full, Damage source, Save save) {
        this.full = full;
        this.source = source;
        this.save = save;
    }

}
