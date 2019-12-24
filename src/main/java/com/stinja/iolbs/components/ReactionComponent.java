package com.stinja.iolbs.components;

import com.stinja.ecs.Component;
import com.stinja.iolbs.rules.Plan;

public class ReactionComponent extends Component {
    public final Plan[] tactics;

    protected ReactionComponent(int eid, Plan[] tactics) {
        super(eid);
        this.tactics = tactics;
    }
}
