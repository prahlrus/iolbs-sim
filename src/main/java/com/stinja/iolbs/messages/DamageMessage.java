package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.Damage;

public class DamageMessage extends Message {
    public final Damage source;
    
    protected DamageMessage(int originId, Damage source) {
        super(originId);
        this.source = source;
    }
}
