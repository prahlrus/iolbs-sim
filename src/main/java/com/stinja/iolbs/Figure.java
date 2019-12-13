package com.stinja.iolbs;

import java.util.Set;

public abstract class Figure implements Cloneable {

    int actions;
    int hd;
    int damage;
    int ac;

    int thAC;
    int tsMC;
    int tsDC;
    int tsSC;

    static int getBase(int factors, int base) {
        while (factors > 0) {
            base--;
            factors = factors / 2;
        }
        return base;
    }

    Figure(int actions, int hd, int ac) {
        this.actions = actions;
        this.hd = hd;
        this.ac = ac;
        damage = 0;
    }

    boolean hurt(Damage d) {
        damage += 3;
        int roll = 0;
        for (int x = 0; x < hd; x++)
            roll += (int) (Math.random() * 6);
        //System.out.printf("Hit die result: %d (%d damage so far)%n", roll, damage);
        return roll < damage;
    }

    void refresh() {
        damage = 0;
    }

    static int roll() {
        return 2 + (int) (Math.random() * 6) + (int) (Math.random() * 6);
    }

    boolean vsMissiles(int mc) {
        return roll() >= tsMC - mc;
    }

    boolean vsDevilrie(int dc) {
        return roll() >= tsDC - dc;
    }

    boolean vsSurprise(int sc) {
        return roll() >= tsSC - sc;
    }

    boolean fight(Figure opponent, int mod) {
        int target = thAC - (opponent.ac + mod);
        int r = roll();
        Damage d = null;

        if (r > target)
            d = Damage.WEAPON;

        if (d != null) {
            //System.err.printf ( "%s deals %s to %s.%n" , this , d , opponent);
            return opponent.hurt(d);
        } else {
            //System.err.printf ( "%s misses %s.%n" , this , opponent);
            return false;
        }
    }

    boolean act
            ( Match e
                    , boolean enemy
                    , Set<Figure> downed
            ) {

        Figure target = e.nextInLine(enemy, downed);
        if (target != null)
            e.engage(this, target);

        return false;
    }

    public String toString() {
        return String.format
                ("HD %2d, AC %2d, THAC0 %2d, Save vs Missiles %2d, vs Devilrie %2d, vs Surprise %2d."
                        , hd
                        , ac
                        , thAC
                        , tsMC
                        , tsDC
                        , tsSC
                );
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
