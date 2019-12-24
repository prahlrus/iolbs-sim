package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.EngagedByComponent;
import com.stinja.iolbs.components.EngagingComponent;
import com.stinja.iolbs.messages.DisengagementMessage;
import com.stinja.iolbs.messages.EngagementMessage;
import com.stinja.iolbs.messages.EscapeMessage;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = {EscapeMessage.class}
        , reads = {DisengagementMessage.class, EngagementMessage.class})
public class TacticalEngine extends Engine {

    @ComponentAccess(componentType = EngagedByComponent.class, mutator = true)
    Mutator<EngagedByComponent> engagedByData;

    @ComponentAccess(componentType = EngagingComponent.class, mutator = true)
    Mutator<EngagingComponent> engagingData;

    Set<Integer> evaders;
    Set<EngagingComponent> approaching;

    public void beforeHandling() {
        evaders = new HashSet<>();
        approaching = new HashSet<>();
    }

    public void handle(Message m) {
        if (m instanceof DisengagementMessage) {
            evaders.add(m.originId);
        } else if (m instanceof EngagementMessage) {
            EngagementMessage em = (EngagementMessage) m;
            engagingData.put(em.originId, new EngagingComponent(em.originId, em.targetId));
            if (! engagedByData.exists(em.targetId))
                engagedByData.put(em.targetId, new EngagedByComponent(em.targetId, em.originId));
        }
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet<>();
        for (int eid : evaders) {
            if (Math.random() * 3 > 1) {
                if (engagedByData.exists(eid)) {
                    engagingData.remove(engagedByData.get(eid).opponentId);
                    engagedByData.remove(eid);
                }
                for (EngagingComponent ec : engagingData.all()) {
                    if (ec.targetId == eid)
                        engagedByData.put(eid, new EngagedByComponent(eid, ec.eid));
                }

                messages.add(new EscapeMessage(eid));
            }
        }

        return messages;
    }

    public void afterFrame() {

    }
}
