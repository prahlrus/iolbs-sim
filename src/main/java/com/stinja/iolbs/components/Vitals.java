package com.stinja.iolbs.components;

import com.stinja.iolbs.Component;

public class Vitals implements Component<Vitals> {
    public final int id;
    public final int hd;
    private int damage;
    public final int vsMissiles;
    public final int vsDevilrie;
    public final int vsSurprise;

    public Vitals(int id, int hd, int vsMissiles, int vsDevilrie, int vsSurprise) {
        this.id = id;
        this.hd = hd;
        this.vsMissiles = vsMissiles;
        this.vsDevilrie = vsDevilrie;
        this.vsSurprise = vsSurprise;
        this.damage = 0;
    }

    public int getId() {
        return id;
    }

    public Vitals clone() {
        try {
            return (Vitals) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int getDamage() {
        return damage;
    }

    public void applyDamage(int amount) {
        damage += amount;
    }
}
