package com.stinja.iolbs.rules;

public enum Plan
    { AIM
        (1
        , Target.SINGLE_RANGED
        , Threat.MISSILE
        )
    , EVADE
        (0
        , Target.NONE
        , null
        )
    , FIGHT
        (0
        , Target.SINGLE_CLOSE
        , Threat.NORMAL
        )
    , SNAP_SHOT
        (0
        , Target.SINGLE_CLOSE
        , Threat.SNAP_SHOT
        )
    ;

    public final int beats;
    public final Target target;
    public final Threat threat;

    Plan(int beats, Target target, Threat threat) {
        this.beats = beats;
        this.target = target;
        this.threat = threat;
    }

}
