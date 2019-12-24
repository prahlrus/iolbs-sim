package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class FigureComponent extends Component {
    private int id;

    public final int hd;
    public final int ac;
    public final int thAC;
    public final int vsMissiles;
    public final int vsDevilrie;
    public final int vsSurprise;

    public FigureComponent(int eid, int hd, int ac, int thAC, int vsDevilrie, int vsMissiles, int vsSurprise) {
        super(eid);
        this.hd = hd;
        this.ac = ac;
        this.thAC = thAC;
        this.vsDevilrie = vsDevilrie;
        this.vsMissiles = vsMissiles;
        this.vsSurprise = vsSurprise;
    }

    public void tick() {

    }
}
