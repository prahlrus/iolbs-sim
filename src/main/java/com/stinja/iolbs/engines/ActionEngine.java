package com.stinja.iolbs.engines;

import com.stinja.hecs.Accessor;
import com.stinja.hecs.ComponentAccess;
import com.stinja.hecs.Engine;
import com.stinja.hecs.Message;
import com.stinja.hecs.MessageHandler;
import com.stinja.iolbs.components.VitalsComponent;
import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.messages.*;
import com.stinja.iolbs.rules.Target;

import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for resolving {@link com.stinja.iolbs.messages.ActionMessage ActionMessages} into
 * {@link com.stinja.iolbs.messages.ThreatMessage ThreatMessages} and
 * {@link com.stinja.iolbs.messages.DisengagementMessage DisengagementMessages} into
 * {@link com.stinja.iolbs.messages.EscapeMessage EscapeMessages}.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */
@MessageHandler
        ( emits = {EscapeMessage.class, TapMessage.class, ThreatMessage.class}
        , reads = {DisengagementMessage.class, ActionMessage.class}
        )
public class ActionEngine extends Engine {

    @ComponentAccess( componentType = PlayerComponent.class)
    private Accessor<PlayerComponent> playerData;

    @ComponentAccess( componentType = MonsterComponent.class)
    private Accessor<MonsterComponent> monsterData;

    @ComponentAccess( componentType = VitalsComponent.class)
    private Accessor<VitalsComponent> figureData;

    public ActionEngine() {
        targeted = new HashSet<>();
        splashed = new HashSet<>();

        tapped = new HashSet<>();
        escapees = new HashSet<>();
    }

    private Set<ThreatMessage> targeted;
    private Set<ThreatMessage> splashed;

    private Set<Integer> tapped;
    private Set<Integer> escapees;

    public void beforeHandling() {
        targeted.clear();
        splashed.clear();
        tapped.clear();
        escapees.clear();
    }

    public void handle(Message m) {
        if (m instanceof DisengagementMessage) {
            // two-thirds odds of ducking all attackers
            if (Math.random() * 3 > 1)
                escapees.add(m.originId);
        } else if (m instanceof ActionMessage) {
            ActionMessage im = (ActionMessage) m;
            if (im.plan.threat == null)
                return;

            tapped.add(im.originId);

            Target target = im.plan.target;
            if (target.specific) {
                targeted.add(new ThreatMessage(im.originId, im.plan.threat, im.targetId));
            }

            if (target.splash && playerData.exists(im.originId)) {
                for (VitalsComponent fc : monsterData.all())
                    if (Math.random() * 2 > 1)
                        splashed.add(new ThreatMessage(im.originId, im.plan.threat, fc.eid));
            }
            else if (target.splash && monsterData.exists(im.originId)) {
                for (VitalsComponent fc : playerData.all())
                    if (Math.random() * 2 > 1)
                        splashed.add(new ThreatMessage(im.originId, im.plan.threat, fc.eid));
            }

        }
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet<>();
        for (ThreatMessage tm : targeted) {
            if (!escapees.contains(tm.targetId))
                messages.add(tm);
        }

        for (ThreatMessage tm : splashed) {
            messages.add(tm);
        }

        for (int tappedId : tapped) {
            messages.add(new TapMessage(tappedId));
        }

        for (int escapeId : escapees) {
            messages.add(new EscapeMessage(escapeId));
        }
        return messages;
    }

    public void afterFrame() {
    }
}
