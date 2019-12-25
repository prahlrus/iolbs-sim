package com.stinja.iolbs.components;

import com.stinja.iolbs.rules.Threat;

public class PlayerComponent extends VitalsComponent {
    public final int vsDevilrie;
    public final int vsMissiles;
    public final int vsSurprise;

    public PlayerComponent(int eid, int hd, int ac, int thAC, int vsDevilrie, int vsMissiles, int vsSurprise) {
        super(eid, hd, ac, thAC);
        this.vsDevilrie = vsDevilrie;
        this.vsMissiles = vsMissiles;
        this.vsSurprise = vsSurprise;
    }

    public int saveAgainst(Threat source) {
        switch (source) {
            case MISSILE:
            case SNAP_SHOT:
                return vsMissiles;
            default:
                return vsSurprise;
        }
    }
}
