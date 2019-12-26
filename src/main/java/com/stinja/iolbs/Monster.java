package com.stinja.iolbs;

import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.rules.Plan;

/**
 * Represents information about a monster, so that an encounter can be repeatedly re-populated with the same monster.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */
public class Monster extends Figure {
    private int hd;
    private int ac;
    private int thAC;
    private int save;

    Monster(String name, int hd, int ac, int thAC, int save, int beats, Plan[] options, Plan[] freeOptions, Plan[] reflexes) {
        super(name, beats, options, freeOptions, reflexes);
        this.hd = hd;
        this.ac = ac;
        this.thAC = thAC;
        this.save = save;
    }

    Monster(Monster monster) {
        super(monster);
        this.hd = monster.hd;
        this.ac = monster.ac;
        this.thAC = monster.thAC;
        this.save = monster.save;
    }

    public MonsterComponent getMonsterComponent(int eid) {
        return new MonsterComponent(eid, hd, ac, thAC, save);
    }
}
