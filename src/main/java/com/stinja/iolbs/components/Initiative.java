package com.stinja.iolbs.components;

import com.stinja.iolbs.Action;
import com.stinja.iolbs.Component;

public class Initiative implements Component<Initiative> {
    public final int id;
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

    public Initiative clone() {
        try {
            return (Initiative) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
