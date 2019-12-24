package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;

public class EndOfRoundMessage extends Message {

    public EndOfRoundMessage(int originId) {
        super(originId);
    }
}
