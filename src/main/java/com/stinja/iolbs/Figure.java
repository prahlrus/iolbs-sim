package com.stinja.iolbs;

import com.stinja.iolbs.components.InitiativeComponent;
import com.stinja.iolbs.components.ReflexComponent;
import com.stinja.iolbs.rules.Plan;

/**
 * Contains the things that {@link com.stinja.iolbs.Player Players} and  {@link com.stinja.iolbs.Monster Monsters}
 * have in common.
 */
public abstract class Figure {

    protected static Plan[] nothing = {};

    public final String name;
    private Plan[] beat0 = nothing;
    private Plan[] beat1 = nothing;
    private Plan[] beat2 = nothing;
    private Plan[] beat3 = nothing;
    private Plan[] reflexes = nothing;

    Figure(String name, int initiative, Plan[] options, Plan[] freeOptions, Plan[] reflexes) {
        this.name = name;
        switch (initiative) {
            case 3:
                if (freeOptions.length > 0)
                    beat0 = freeOptions;
                beat1 = options;
            case 2:
                if (freeOptions.length > 0 && initiative < 3)
                    beat1 = freeOptions;
                beat2 = options;
            case 1:
                if (freeOptions.length > 0 && initiative < 2)
                    beat2 = freeOptions;
                beat3 = options;
                break;
            default:
                throw new RuntimeException("Initiative must be 1, 2 or 3, was " + initiative + ".");
        }
        this.reflexes = reflexes;
    }

    protected Figure(Figure figure) {
        this.name = figure.name;
        this.beat0 = figure.beat0;
        this.beat1 = figure.beat1;
        this.beat2 = figure.beat2;
        this.beat3 = figure.beat3;
        this.reflexes = figure.reflexes;
    }

    public InitiativeComponent getInitiativeComponent(int eid) {
        return new InitiativeComponent(eid, beat0, beat1, beat2, beat3);
    }
    public ReflexComponent getReactionComponent(int eid) {
        if (reflexes == nothing)
            return null;
        else
            return new ReflexComponent(eid, reflexes);
    }
}
