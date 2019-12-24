package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.messages.EndOfRoundMessage;
import com.stinja.iolbs.messages.TapMessage;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = {}
        , reads = {TapMessage.class, EndOfRoundMessage.class}
        )
public class ReadinessEngine extends Engine {
    boolean endOfRound;

    @ComponentAccess( componentType = TappedComponent.class, mutator = true)
    private Mutator<TappedComponent> tappedData;

    public void beforeHandling() {
        endOfRound = false;
    }

    public void handle(Message m) {
        if (m instanceof TapMessage) {
            TapMessage tm = (TapMessage) m;
            if (tm.untap && tappedData.exists(tm.originId))
                tappedData.remove(tm.originId);
            else if (! tm.untap && ! tappedData.exists(tm.originId))
                tappedData.put(tm.originId, new TappedComponent(tm.originId));
        } else if (m instanceof EndOfRoundMessage) {
            endOfRound = true;
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
