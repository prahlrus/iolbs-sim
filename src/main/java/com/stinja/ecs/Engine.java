package com.stinja.ecs;

import java.util.Set;

public abstract class Engine {
    public abstract void beforeHandling();
    public abstract void handle(Message m);
    public abstract Set<Message> frame();
    public abstract void afterFrame();

}