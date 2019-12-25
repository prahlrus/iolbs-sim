package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.VitalsComponent;
import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.messages.EscapeMessage;
import com.stinja.iolbs.messages.ActionMessage;
import com.stinja.iolbs.messages.ThreatMessage;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = {ThreatMessage.class}
        , reads = {EscapeMessage.class, ActionMessage.class}
        )
public class ActionEngine extends Engine {

    @ComponentAccess( componentType = PlayerComponent.class, mutator = false )
    private Accessor<PlayerComponent> playerData;

    @ComponentAccess( componentType = MonsterComponent.class, mutator = false )
    private Accessor<MonsterComponent> monsterData;

    @ComponentAccess( componentType = VitalsComponent.class, mutator = false )
    private Accessor<VitalsComponent> figureData;


    private Set<ThreatMessage> threatMessages;
    private Set<Integer> escapees;

    public void beforeHandling() {
        threatMessages = new HashSet<>();
        escapees = new HashSet<>();
    }

    public void handle(Message m) {
        if (m instanceof EscapeMessage) {
            escapees.add(m.originId);
        } else if (m instanceof ActionMessage) {
            ActionMessage im = (ActionMessage) m;
            if (im.plan.threat == null)
                return;

            if (im.plan.target.specific) {
                threatMessages.add(new ThreatMessage(im.originId, im.plan.threat, im.targetId));
            }

            if (im.plan.target.splash && playerData.exists(im.originId)) {
                for (VitalsComponent fc : monsterData.all())
                    if (Math.random() * 2 > 1)
                        threatMessages.add(new ThreatMessage(im.originId, im.plan.threat, fc.eid));
            }
            else if (im.plan.target.splash && monsterData.exists(im.originId)) {
                for (VitalsComponent fc : playerData.all())
                    if (Math.random() * 2 > 1)
                        threatMessages.add(new ThreatMessage(im.originId, im.plan.threat, fc.eid));
            }

        }
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet();
        for (ThreatMessage tm : threatMessages)
            if (! escapees.contains(tm.targetId))
                messages.add(tm);
        return messages;
    }

    public void afterFrame() {
        threatMessages.clear();
        escapees.clear();
    }
}
