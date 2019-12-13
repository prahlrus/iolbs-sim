package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class Figure implements Component<Figure> {
    private int id;

    public final int hd;
    public final int thAC;
    public final int vsMissiles;
    public final int vsDevilrie;
    public final int vsSurprise;

    public Figure(int id, int hd, int thAC, int vsMissiles, int vsDevilrie, int vsSurprise) {
        this.id = id;
        this.hd = hd;
        this.thAC = thAC;
        this.vsMissiles = vsMissiles;
        this.vsDevilrie = vsDevilrie;
        this.vsSurprise = vsSurprise;
    }

    public int getId() {
        return id;
    }

    public Figure clone(int newId) {
        return new Figure
                    ( newId
                    , hd
                    , thAC
                    , vsMissiles
                    , vsDevilrie
                    , vsSurprise
                    );
    }
}
