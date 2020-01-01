package com.stinja.iolbs.messages;

import com.stinja.hecs.Message;

public class EndOfRoundMessage extends Message {

    public EndOfRoundMessage(int originId) {
        super(originId);
    }
}
