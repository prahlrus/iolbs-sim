package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;

public class EngagementMessage extends Message {
    public final int targetId;

    public EngagementMessage(int originId, int targetId) {
        super(originId);
        this.targetId = targetId;
    }
}
