package com.stinja.iolbs.components;

import com.stinja.iolbs.Action;
import com.stinja.ecs.Component;

public class InitiativeComponent extends Component {
    public final Action[][] options;

    public InitiativeComponent
            ( int eid
            , Action[] beat0
            , Action[] beat1
            , Action[] beat2
            , Action[] beat3) {
        super(eid);
        this.options = new Action[][]{ beat0, beat1, beat2, beat3 };
    }
}
