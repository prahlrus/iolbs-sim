package com.stinja.iolbs.components;

import com.stinja.ecs.Component;
import com.stinja.iolbs.rules.Plan;

public class PlanComponent extends Component {
    public final Plan plan;
    public final int targetId;
    private int beats;

    public PlanComponent(int eid, Plan plan, int targetId, int beats) {
        super(eid);
        this.plan = plan;
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
