package com.stinja.ecs;

public abstract class Message {
    public final int originId;

    protected Message(int originId) {
        this.originId = originId;
    }
}
