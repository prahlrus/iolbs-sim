package com.stinja.iolbs.components;

import com.stinja.hecs.Component;
import com.stinja.iolbs.rules.Threat;

public abstract class VitalsComponent extends Component {
    public final int hd;
    public final int ac;
    public final int thAC;

    protected VitalsComponent(int eid, int hd, int ac, int thAC) {
        super(eid);
        this.hd = hd;
        this.ac = ac;
        this.thAC = thAC;
    }

    public abstract int saveAgainst(Threat source);
}
