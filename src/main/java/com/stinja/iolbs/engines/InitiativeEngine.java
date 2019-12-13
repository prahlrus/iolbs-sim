package com.stinja.iolbs.engines;

import com.stinja.ecs.Engine;
import com.stinja.iolbs.Action;
import com.stinja.iolbs.components.Encounter;
import com.stinja.iolbs.components.Initiative;
import com.stinja.iolbs.components.MonsterCharacter;
import com.stinja.iolbs.components.PlayerCharacter;

import java.util.Map;

import static com.stinja.iolbs.components.Encounter.BEATS_PER_ROUND;

public class InitiativeEngine extends Engine {
    private Map<Integer, Encounter> encounterComponents;
    private Map<Integer, Initiative> initiativeComponents;
    private Map<Integer, PlayerCharacter> pcComponents;
    private Map<Integer, MonsterCharacter> mcComponents;

    public void frame() {

        for (Map.Entry<Integer,Encounter> encounterEntry : encounterComponents.entrySet()) {
            int beat = encounterEntry.getValue().frame % BEATS_PER_ROUND;

            for (int i : initiativeComponents.keySet()) {
                Action a = selectAction(i, beat);
            }

            // increment the frame
            encounterEntry.getValue().frame ++;
        }

    }

    private Action selectAction(int actorId, int beat) {
        Action[] options = initiativeComponents.get(actorId).options[beat];
        if (options != null && options.length > 0) {
            return options[(int) (Math.random() * options.length)];
        }
        return null;
    }
}
