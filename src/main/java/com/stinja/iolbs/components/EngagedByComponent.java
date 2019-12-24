package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class EngagedByComponent extends Component {

    public final int opponentId;

    public EngagedByComponent(int eid, int opponentId) {
        super(eid);
        this.opponentId = opponentId;
    }
}
