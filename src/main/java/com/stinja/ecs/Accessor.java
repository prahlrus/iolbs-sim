package com.stinja.ecs;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class Accessor<T extends Component> {
    protected Map<Integer,T> data;

    public boolean exists(int entId) {
        return data.containsKey(entId);
    }

    public T get(int entId) {
        return data.get(entId);
    }

    public Collection<T> all() {
        return data.values();
    }

    public int size() { return data.size(); }
}
