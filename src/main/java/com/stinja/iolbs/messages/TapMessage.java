package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;

public class TapMessage extends Message {
    public final boolean untap;

    public TapMessage(int originId, boolean untap) {
        super(originId);
        this.untap = untap;
    }
}
