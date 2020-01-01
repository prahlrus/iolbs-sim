package com.stinja.iolbs.engines;

import com.stinja.hecs.ComponentAccess;
import com.stinja.hecs.Engine;
import com.stinja.hecs.Message;
import com.stinja.hecs.MessageHandler;
import com.stinja.hecs.Mutator;
import com.stinja.iolbs.components.EngagedByComponent;
import com.stinja.iolbs.components.EngagingComponent;
import com.stinja.iolbs.messages.DownedMessage;
import com.stinja.iolbs.messages.EngagementMessage;
import com.stinja.iolbs.messages.EscapeMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for determining positioning at end of round, updating the
 * {@link com.stinja.iolbs.components.EngagingComponent EngagingComponents} and
 * {@link com.stinja.iolbs.components.EngagedByComponent EngagedByComponents} based on
 * the results of the frame so far.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */

@MessageHandler
        ( emits = {}
        , reads = {EscapeMessage.class, DownedMessage.class, EngagementMessage.class})
public class TacticalEngine extends Engine {

    @ComponentAccess(componentType = EngagedByComponent.class, mutator = true)
    Mutator<EngagedByComponent> engagedByData;

    @ComponentAccess(componentType = EngagingComponent.class, mutator = true)
    Mutator<EngagingComponent> engagingData;

    public TacticalEngine() {
        evaders = new HashSet<>();
        downed = new HashSet<>();
        approaching = new HashSet<>();
    }

    Set<Integer> evaders;
    Set<Integer> downed;
    Set<EngagingComponent> approaching;

    public void beforeHandling() {
        downed.clear();
        evaders.clear();
        approaching.clear();
    }

    public void handle(Message m) {
        if (m instanceof EscapeMessage) {
            evaders.add(m.originId);
        } else if (m instanceof DownedMessage) {
            engagingData.remove(m.originId);
            downed.add(m.originId);
        } else if (m instanceof EngagementMessage) {
            EngagementMessage em = (EngagementMessage) m;
            approaching.add(new EngagingComponent(em.originId, em.targetId));
        }
    }

    public Set<Message> frame() {
        engagedByData.clear();
        for (EngagingComponent ec : approaching) {
            if (! downed.contains(ec.eid) && ! downed.contains(ec.targetId) && ! evaders.contains(ec.targetId))
                engagingData.put(ec.eid, ec);
        }

        for (EngagingComponent ec : engagingData.all()) {
            if (! engagedByData.exists(ec.targetId))
                engagedByData.put(ec.targetId, new EngagedByComponent(ec.targetId, ec.eid));
        }

        return null;
    }

    public void afterFrame() {
    }
}
