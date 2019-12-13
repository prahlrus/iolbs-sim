package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class Encounter implements Component<Encounter> {
    public static final int BEATS_PER_ROUND = 4;

    private int id;
    public int frame;

    public Encounter(int id) {
        this.id = id;
        this.frame = 0;
    }

    public int getId() {
        return id;
    }

    public Encounter clone(int newId) {
        Encounter e = new Encounter(newId);
        e.frame = frame;
        return e;
    }
}
