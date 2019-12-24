package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class EncounterComponent extends Component{
    public static final int BEATS_PER_ROUND = 4;
    private int frame;

    public EncounterComponent(int eid) {
        super(eid);
        this.frame = 0;
    }

    public int getFrame() {
        return frame;
    }

    public void tick() {
        frame++;
    }
}
