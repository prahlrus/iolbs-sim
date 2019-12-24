package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.rules.Threat;

public class ThreatMessage extends Message {
    public final Threat threat;
    public final int targetId;

    public ThreatMessage(int originId, Threat threat, int targetId) {
        super(originId);
        this.threat = threat;
        this.targetId = targetId;
    }
}
