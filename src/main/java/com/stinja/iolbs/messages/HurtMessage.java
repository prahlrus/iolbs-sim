package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;

public class HurtMessage extends Message {
    public final int damage;

    public HurtMessage(int originId, int damage) {
        super(originId);
        this.damage = damage;
    }
}
