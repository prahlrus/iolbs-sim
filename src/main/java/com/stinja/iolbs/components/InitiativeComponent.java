package com.stinja.iolbs.components;

import com.stinja.iolbs.rules.Plan;
import com.stinja.ecs.Component;

public class InitiativeComponent extends Component {
    public final Plan[][] options;

    public InitiativeComponent
            ( int eid
            , Plan[] beat0
            , Plan[] beat1
            , Plan[] beat2
            , Plan[] beat3) {
        super(eid);
        this.options = new Plan[][]{ beat0, beat1, beat2, beat3 };
    }
}
