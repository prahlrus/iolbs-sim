package com.stinja.iolbs;

import com.stinja.hecs.Accessor;
import com.stinja.hecs.Game;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.engines.*;

import java.util.HashSet;
import java.util.Set;

import static com.stinja.iolbs.components.EncounterComponent.BEATS_PER_ROUND;

/**
 * Actually handles the simulation of encounters.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */
public class Arena extends Game {

    private Accessor<EncounterComponent> encounterData;
    private Accessor<PlayerComponent> playerData;
    private Accessor<MonsterComponent> monsterData;

    private String[] names;

    Arena() {
        super(new Class[]
                {ActionEngine.class
                , DamageEngine.class
                , DecisionEngine.class
                , HitEngine.class
                , ReadinessEngine.class
                , StrategyEngine.class
                , TacticalEngine.class
                }
            );
    }

    /**
     * Sets up a match, loading the given Figures' information.
     *
     * @param figures To use in the encounter.
     */
    public void match (Set<Figure> figures) {
        reset();

        Set<EncounterComponent> encounterComponents = new HashSet<>();
        Set<PlayerComponent> playerComponents = new HashSet<>();
        Set<MonsterComponent> monsterComponents = new HashSet<>();
        Set<InitiativeComponent> initiativeComponents = new HashSet<>();
        Set<ReflexComponent> reflexComponents = new HashSet<>();

        // eid 0 tracks the frame
        int eid = 0;
        encounterComponents.add(new EncounterComponent(eid++));

        names = new String[figures.size() + 1];

        // collect the figure data
        for (Figure f : figures) {
            names[eid] = f.name;

            InitiativeComponent ic = f.getInitiativeComponent(eid);
            if (ic != null)
                initiativeComponents.add(ic);

            ReflexComponent rc = f.getReactionComponent(eid);
            if (rc != null)
                reflexComponents.add(rc);

            if (f instanceof Player) {
                Player p = (Player) f;
                PlayerComponent pc = p.getPlayerComponent(eid);
                if (pc != null) {
                    playerComponents.add(pc);
                }
            } else if (f instanceof Monster) {
                Monster m = (Monster) f;
                MonsterComponent mc = m.getMonsterComponent(eid);
                if (mc != null) {
                    monsterComponents.add(mc);
                }
            }

            eid++;
        }

        load(EncounterComponent.class, encounterComponents);
        load(InitiativeComponent.class, initiativeComponents);
        load(ReflexComponent.class, reflexComponents);
        load(PlayerComponent.class, playerComponents);
        load(MonsterComponent.class, monsterComponents);

        encounterData = getMutator(EncounterComponent.class);
        playerData = getMutator(PlayerComponent.class);
        monsterData = getMutator(MonsterComponent.class);
    }

    /**
     * Runs the simulation for a loaded encounter.
     *
     * @return A MatchResult describing how long the encounter took and who survived it.
     */
    public MatchResult run() {
        while (playerData.size() > 0 && monsterData.size() > 0) {
            frame();
        }

        // number of frames divided by the number of frames per round, rounded up
        int rounds = (encounterData.get(0).getFrame() - 1) / BEATS_PER_ROUND + 1;
        Set<String> survivingPlayers = new HashSet<>();
        for (PlayerComponent pc : playerData.all())
            survivingPlayers.add(names[pc.eid]);

        return new MatchResult
            ( rounds
            , survivingPlayers.toArray(new String[survivingPlayers.size()])
            , monsterData.size()
            );
    }
}
