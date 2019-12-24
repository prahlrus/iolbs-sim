package com.stinja.iolbs.rules;

public enum Target
    { SINGLE_CLOSE (true, true, false)
    , SINGLE_RANGED(true, false, false)
    , NONE(false, false, false)
    , AREA_CLOSE(true, false, false)
    , AREA_RANGED(true, false, false)
    , ALL(false, false, true)
    ;

    public final boolean specific;
    public final boolean close;
    public final boolean splash;

        Target(boolean specific, boolean close, boolean splash) {
            this.specific = specific;
            this.close = close;
            this.splash = splash;
        }
    }
