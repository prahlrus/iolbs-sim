package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.messages.DownedMessage;
import com.stinja.iolbs.messages.EndOfRoundMessage;
import com.stinja.iolbs.messages.TapMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for tapping acting figures until the end of the round, and removing
 * figures' {@link com.stinja.iolbs.components.InitiativeComponent InitiativeComponents} and
 * {@link com.stinja.iolbs.components.ReflexComponent ReflexComponents} when they are taken
 * out.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */

@MessageHandler
        ( emits = {}
        , reads = {DownedMessage.class, TapMessage.class, EndOfRoundMessage.class}
        )
public class ReadinessEngine extends Engine {
    boolean endOfRound;

    @ComponentAccess( componentType = TappedComponent.class, mutator = true)
    private Mutator<TappedComponent> tappedData;

    @ComponentAccess(componentType = InitiativeComponent.class, mutator = true)
    private Mutator<InitiativeComponent> initiativeData;

    @ComponentAccess(componentType = ReflexComponent.class, mutator = true)
    private Mutator<ReflexComponent> reflexData;

    public void beforeHandling() {
        endOfRound = false;
    }

    public void handle(Message m) {
        if (m instanceof DownedMessage){
            tappedData.remove(m.originId);
            initiativeData.remove(m.originId);
            reflexData.remove(m.originId);
        } else if (m instanceof EndOfRoundMessage) {
            endOfRound = true;
        } else if (m instanceof TapMessage) {
            TapMessage tm = (TapMessage) m;
            tappedData.put(tm.originId, new TappedComponent(tm.originId));
        }
    }

    public Set<Message> frame() {
        if (endOfRound)
            tappedData.clear();
        return new HashSet<>();
    }

    public void afterFrame() {
    }
}
