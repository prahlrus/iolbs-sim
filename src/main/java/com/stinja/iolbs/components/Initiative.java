package com.stinja.iolbs.components;

import com.stinja.iolbs.Action;
import com.stinja.ecs.Component;

public class Initiative implements Component<Initiative> {
    private int id;
    public final Action[][] options;

    public Initiative
            ( int id
            , Action[] beat0
            , Action[] beat1
            , Action[] beat2
            , Action[] beat3) {
        this.id = id;
        this.options = new Action[][]{ beat0, beat1, beat2, beat3 };
    }

    public int getId() {
        return id;
    }

    public Initiative clone(int newId) {
        return new Initiative
                ( newId
                , options[0]
                , options[1]
                , options[2]
                , options[3]
                );
    }
}
