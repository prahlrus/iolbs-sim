package com.stinja.iolbs.components;

import com.stinja.iolbs.Component;
import com.stinja.iolbs.Damage;

public class Attack implements Component<Attack> {
    public final int id;
    public final int thAC;
    public final Damage type;

    public Attack(int id, int thAC, Damage type) {
        this.id = id;
        this.thAC = thAC;
        this.type = type;
    }

    public Attack clone() {
        try {
            return (Attack) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
