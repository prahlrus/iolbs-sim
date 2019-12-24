package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class DamageComponent extends Component {
    private int id;

    public final int amount;

    public DamageComponent(int eid, int amount) {
        super(eid);
        this.amount = amount;
    }
}
