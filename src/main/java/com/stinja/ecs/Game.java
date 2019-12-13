package com.stinja.ecs;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game {
    private List<Message> queue;
    private List<Engine> handlers;

    private Map<Class,Mutator> mutators;
    private Map<Class,Engine> mutationRights;

    private Map<Engine, Set<Class>> emitters;
    private Map<Engine, Set<Class>> consumers;

    public void pass(Message m) {
        queue.add(m);
    }

    public void frame() {
        for (Engine e : handlers) {
            List<Message> remaining = new LinkedList<>();
            for (Message m : queue) {
                boolean handled = false;
                for (Class clazz : consumers.get(e)) {
                    if (m.getClass() == clazz) {
                        e.handle(m);
                        handled = true;
                        break;
                    }
                }
                if (! handled)
                    remaining.add(m);
            }
            e.frame();
            queue = remaining;
        }
    }

    public void addEngine(Engine e) {
    }
}
