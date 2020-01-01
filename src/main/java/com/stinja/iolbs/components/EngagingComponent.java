package com.stinja.iolbs.components;

import com.stinja.hecs.Component;

public class EngagingComponent extends Component {
    public final int targetId;

    public EngagingComponent(int eid, int targetId) {
        super(eid);
        this.targetId = targetId;
    }
}
