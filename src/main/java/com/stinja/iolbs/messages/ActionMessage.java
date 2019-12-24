package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.rules.Plan;

public class ActionMessage extends Message {

    public final Plan plan;
    public final int targetId;

    public ActionMessage(int originId, Plan plan, int targetId) {
        super(originId);
        this.plan = plan;
        this.targetId = targetId;
    }
}
