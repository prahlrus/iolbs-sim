package com.stinja.iolbs;

import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.rules.Plan;

public class Player extends Figure {
    public enum Type
        { HEAVY
                ( new Plan[]{ Plan.FIGHT }
                , new Plan[]{ Plan.FIGHT }
                , 2)
//        , ARCHER
//                ( new Plan[]{ Plan.AIM }
//                , new Plan[]{ Plan.SNAP_SHOT }
//                , 4)
        ;

        public final Plan[] options;
        public final Plan[] reflexes;
        public final int ac;

        Type(Plan[] options, Plan[] reflexes, int ac) {
            this.options = options;
            this.reflexes = reflexes;
            this.ac = ac;
        }
    }

    private int strength;
    private int learning;
    private Type type;

    Player(String name, Type type, int courage, int wisdom) {
        super(name, 2, type.options, nothing, type.reflexes);
        this.type = type;
        this.strength = 1 + courage + (wisdom / 2);
        this.learning = 1 + wisdom + (courage / 2);
    }

    public PlayerComponent getPlayerComponent(int eid) {
        return new PlayerComponent
                ( eid
                , strength
                , type.ac
                , getTarget(12, strength)
                , getTarget(12, learning)
                , getTarget(9 + type.ac, learning)
                , getTarget(15 - type.ac, learning)
                );
    }

    private int getTarget(int base, int var) {
        while (var > 1) {
            base--;
            var /= 2;
        }
        return base;
    }
}
