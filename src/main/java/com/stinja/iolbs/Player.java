package com.stinja.iolbs;

import java.util.Set;

public abstract class Player extends Figure {
    String name;
    String type;

    Player(String name, String type, int courage, int wisdom, Armor armor) {
        super
                ( 2
                        , 1 + courage + wisdom / 2
                        , armor.ac
                );

        this.name = name;
        this.type = type;
        int strength = 1 + courage + wisdom / 2;
        thAC = getBase(strength, 13);

        int intrinsic = 1 + wisdom + courage / 2;
        tsDC = getBase(intrinsic, 13);
        tsMC = getBase(intrinsic, 8 + armor.ac);
        tsSC = getBase(intrinsic, 15 - armor.ac);
    }

    public String toString() {
        return String.format
                ( "%s the %s"
                        , name
                        , type
                );
    }
}

class Light extends Player {
    Light(String name, int rank) {
        super(name, "Light", rank, 0, Armor.LEATHER);
    }
}

class Heavy extends Player {
    Heavy(String name, int rank) {
        super(name, "Heavy", rank, 0, Armor.MAILE);
    }
}

class Archer extends Player {
    Archer(String name, int rank) {
        super(name, "Archer", rank, 0, Armor.LEATHER);
    }

    boolean act
            ( Match e
                    , boolean enemy
                    , boolean engaged
                    , Set<Figure> downed
            ) {

        Figure target = e.nextInLine(enemy, downed);
        if (target != null && ! target.vsMissiles(2))
            target.hurt(Damage.LIGHT);
        return true;
    }
}