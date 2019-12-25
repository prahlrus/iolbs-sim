package com.stinja.iolbs.components;

import com.stinja.iolbs.rules.Threat;

public class MonsterComponent extends VitalsComponent {
    public final int save;

    public MonsterComponent(int eid, int hd, int ac, int thAC, int save) {
        super(eid, hd, ac, thAC);
        this.save = save;
    }

    public int saveAgainst(Threat source) {
        return save;
    }
}