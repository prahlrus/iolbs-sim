package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.Damage;

public class AttackMessage extends Message {
    public final Damage source;
    public final int toHit;

    public AttackMessage(int originId, Damage source, int toHit) {
        super(originId);
        this.source = source;
        this.toHit = toHit;
    }
}