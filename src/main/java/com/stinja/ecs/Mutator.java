package com.stinja.ecs;

import java.util.HashMap;

public class Mutator<T extends Component> extends Accessor<T> {
    Mutator() {
        data = new HashMap<>();
    }

    public void put(int entId, T component) {
        data.put(entId, component);
    }

    public void remove(int entId) {
        data.remove(entId);
    }

    public void clear() {
        data.clear();
    }

    public Accessor<T> readOnly() {
        Accessor<T> readOnly = new Accessor<>();
        readOnly.data = this.data;
        return readOnly;
    }

    public void tick() {
        for (T t : data.values()) {
            t.tick();
        }
    }
}
