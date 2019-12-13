package com.stinja.ecs;

public @interface MessageHandler {
    Emits[] emits();
    Consumes[] consumes();
}
