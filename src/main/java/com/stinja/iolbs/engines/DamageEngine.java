package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.rules.Dice;
import com.stinja.iolbs.components.EngagedByComponent;
import com.stinja.iolbs.components.VitalsComponent;
import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.messages.HurtMessage;
import com.stinja.iolbs.messages.ThreatMessage;
import com.stinja.iolbs.rules.Threat;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = {HurtMessage.class}
        , reads = {ThreatMessage.class}
)
public class DamageEngine extends Engine {

    @ComponentAccess(componentType = EngagedByComponent.class)
    private Accessor<EngagedByComponent> engagedByData;

    @ComponentAccess( componentType = MonsterComponent.class)
    private Accessor<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class)
    private Accessor<PlayerComponent> playerData;

    private Set<Message> hurtMessages;

    public void beforeHandling() {
        hurtMessages = new HashSet<>();
    }

    public void handle(Message m) {
        if (m instanceof ThreatMessage) {
            ThreatMessage tm = (ThreatMessage) m;
            int originId = tm.originId;
            int targetId = tm.targetId;

            VitalsComponent originVC = null;
            if (playerData.exists(originId))
                originVC = playerData.get(originId);
            else if (monsterData.exists(originId))
                originVC = monsterData.get(originId);

            if (originVC == null) {
                return;
            }

            VitalsComponent targetVC = null;
            if (playerData.exists(targetId))
                targetVC = playerData.get(targetId);
            else if (monsterData.exists(targetId))
                targetVC = monsterData.get(targetId);

            if (targetVC == null) {
                return;
            }

            Threat threat = tm.threat;

            int threshold;
            if (threat.attack)
                threshold = originVC.thAC;
            else
                threshold = targetVC.saveAgainst(threat);

            int ddc;
            if (threat.attack)
                ddc = targetVC.ac;
            else
                ddc = threat.ddc;

            int roll = Dice.check();

            switch (threat) {
                case NORMAL:
                    // flanking bonus
                    if (engagedByData.exists(targetId) && engagedByData.get(targetId).opponentId != originId)
                        roll += 1;
                    break;
                default:
                    if (roll == 12)
                        return;
                    break;
            }
            roll += ddc;

            if (threat.attack) {
                if (roll == threshold)
                    hurtMessages.add(new HurtMessage(targetId, 1));
                else if (roll > threshold)
                    hurtMessages.add(new HurtMessage(targetId, threat.amount));
            }
            else {
                if (roll == threshold)
                    hurtMessages.add(new HurtMessage(targetId, 1));
                else if (roll < threshold)
                    hurtMessages.add((new HurtMessage(targetId, threat.amount)));
            }

        }
    }

    public Set<Message> frame() {
        return hurtMessages;
    }

    public void afterFrame() {
        hurtMessages.clear();
    }
}
