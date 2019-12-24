package com.stinja.iolbs.components;

import com.stinja.ecs.Component;
import com.stinja.iolbs.rules.Threat;

public abstract class FigureComponent extends Component {
    public final int hd;
    public final int ac;
    public final int thAC;

    protected FigureComponent(int eid, int hd, int ac, int thAC) {
        super(eid);
        this.hd = hd;
        this.ac = ac;
        this.thAC = thAC;
    }

    public abstract int saveAgainst(Threat source);
}
