package com.stinja.ecs;

import java.util.Map;

public class Accessor<T extends Component> {
    protected Map<Integer,T> data;

    public boolean exists(int entId) {
        return data.containsKey(entId);
    }

    public T get(int entId) {
        return data.get(entId);
    }
}
