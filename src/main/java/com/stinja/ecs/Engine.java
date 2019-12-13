package com.stinja.ecs;

public abstract class Engine {
    private Game game;

    public abstract void frame();

    protected void send(Message m) {
        game.pass(m);
    }

    public abstract void handle(Message m);
}
