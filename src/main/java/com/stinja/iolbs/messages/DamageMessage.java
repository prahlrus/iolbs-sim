package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.Damage;

public class DamageMessage extends Message {
    public final int amount;
    
    public DamageMessage(int originId, int amount) {
        super(originId);
        this.amount = amount;
    }
}
