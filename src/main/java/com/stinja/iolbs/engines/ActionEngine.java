package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.Dice;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.messages.AttackMessage;
import com.stinja.iolbs.messages.DamageMessage;
import com.stinja.iolbs.messages.SaveMessage;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = {DamageMessage.class}
        , reads = {AttackMessage.class}
        )
public class ActionEngine extends Engine {

    @ComponentAccess( componentType = MonsterComponent.class, mutator = false )
    private Accessor<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class, mutator = false )
    private Accessor<PlayerComponent> playerData;

    @ComponentAccess( componentType = FigureComponent.class, mutator = false )
    private Accessor<FigureComponent> figureData;

    private Set<Message> messages;

    public void beforeHandling() {
        messages = new HashSet();
    }

    public void handle(Message m) {
        if (m instanceof AttackMessage) {
            AttackMessage am = (AttackMessage) m;
            int eid = am.originId;
            FigureComponent fc = figureData.get(eid);
            int roll = Dice.check() + fc.ac;

            // a glancing blow
            if (roll == am.toHit) {
                messages.add(new DamageMessage(eid, 1));
            }
            // a solid hit
            else if (roll > am.toHit) {
                messages.add(new DamageMessage(eid, am.source.amount));
            }
        } else if (m instanceof SaveMessage) {
            SaveMessage sm = (SaveMessage) m;
            int eid = sm.originId;
            FigureComponent fc = figureData.get(eid);

            int save = 12;
            switch (sm.source) {
                case NORMAL: save = fc.vsMissiles;
                default: save = fc.vsSurprise;
            }

            int roll = Dice.check();
            if (roll == 12) return; // boxcars always saves

            roll += sm.saveClass;

            // save with a scratch
            if (roll == save) {
                messages.add(new DamageMessage(eid, 1));
            } else if (roll < save) {
                messages.add(new DamageMessage(eid, sm.source.amount));
            }

        }
    }

    public Set<Message> frame() {
        return messages;
    }

    public void afterFrame() {
        messages.clear();
    }
}
