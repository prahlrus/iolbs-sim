package com.stinja.iolbs.components;

import com.stinja.ecs.Component;
import com.stinja.iolbs.Action;

public class DecisionComponent extends Component {
    public final Action action;
    public final int targetId;
    private int beats;

    public DecisionComponent(int eid, Action action, int targetId, int beats) {
        super(eid);
        this.action = action;
        this.targetId = targetId;
        this.beats = beats;
    }

    public int getBeats() {
        return beats;
    }

    public void tick() {
        beats--;
    }
}
