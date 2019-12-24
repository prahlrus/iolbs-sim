package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class EngagementComponent extends Component {
    private int id;
    public final int targetId;

    public EngagementComponent(int eid, int targetId) {
        super(eid);
        this.targetId = targetId;
    }
}
