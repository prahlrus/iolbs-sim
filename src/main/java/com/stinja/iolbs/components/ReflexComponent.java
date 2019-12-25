package com.stinja.iolbs.components;

import com.stinja.ecs.Component;
import com.stinja.iolbs.rules.Plan;

public class ReflexComponent extends Component {
    public final Plan[] tactics;

    public ReflexComponent(int eid, Plan[] tactics) {
        super(eid);
        this.tactics = tactics;
    }
}
