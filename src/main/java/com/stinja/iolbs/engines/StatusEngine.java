package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.Dice;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.messages.DamageMessage;
import com.stinja.iolbs.messages.TapMessage;

import java.util.HashSet;
import java.util.Set;

import static com.stinja.iolbs.components.EncounterComponent.BEATS_PER_ROUND;

@MessageHandler
        ( emits = {}
        , reads = {DamageMessage.class, TapMessage.class}
        )
public class StatusEngine extends Engine {
    @ComponentAccess( componentType = MonsterComponent.class, mutator = true )
    private Mutator<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class, mutator = true )
    private Mutator<PlayerComponent> playerData;

    @ComponentAccess( componentType = TappedComponent.class, mutator = true)
    private Mutator<TappedComponent> tappedData;

    @ComponentAccess( componentType = DamageComponent.class, mutator = true)
    private Mutator<DamageComponent> damageData;

    @ComponentAccess(componentType = EncounterComponent.class, mutator = true)
    private Mutator<EncounterComponent> encounterData;

    @ComponentAccess( componentType = FigureComponent.class, mutator = false )
    private Accessor<FigureComponent> figureData;

    public void beforeHandling() {
    }

    public void handle(Message m) {
        if (m instanceof TapMessage) {
            TapMessage tm = (TapMessage) m;
            if (! tappedData.exists(tm.originId))
                tappedData.put(tm.originId, new TappedComponent(tm.originId));
        } else if (m instanceof DamageMessage) {
            DamageMessage dm = (DamageMessage) m;
            int eid = dm.originId;
            if (! (playerData.exists(eid) || monsterData.exists(eid)))
                return;

            FigureComponent fc = figureData.get(eid);

            int total = 0;
            if (damageData.exists(eid)) {
                total += damageData.get(eid).amount;
            }

            if (Dice.hit(fc.hd) < total) {
                playerData.remove(eid);
                monsterData.remove(eid);
            }
        }
    }

    public Set<Message> frame() {
        return new HashSet<>();
    }

    public void afterFrame() {
        encounterData.tick();

        for (EncounterComponent ec : encounterData.all()) {
            if (ec.getFrame() % BEATS_PER_ROUND == 0) {
                tappedData.clear();
                return;
            }
        }
    }
}
