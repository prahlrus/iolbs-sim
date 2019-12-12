package com.stinja.iolbs;

public class Monster extends Figure {
    String type;

    Monster(String type, int actions, int hd, int ac, int save) {
        super( actions, hd, ac );

        this.type = type;
        thAC = getBase(hd, 13);
        tsMC = save;
        tsDC = save;
        tsSC = save;
    }

    public String toString() {
        return String.format
                ( "a %s"
                        , type
                );
    }
}