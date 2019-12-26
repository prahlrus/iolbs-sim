package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.EncounterComponent;
import com.stinja.iolbs.components.PlanComponent;
import com.stinja.iolbs.messages.ActionMessage;
import com.stinja.iolbs.messages.BideMessage;
import com.stinja.iolbs.messages.DownedMessage;
import com.stinja.iolbs.messages.EndOfRoundMessage;

import java.util.HashSet;
import java.util.Set;

import static com.stinja.iolbs.components.EncounterComponent.BEATS_PER_ROUND;

/**
 * Responsible for updating the {@link com.stinja.iolbs.components.DamageComponent DecisionComponents} that represent
 * a figure's chosen (but not yet taken) course of action.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */
@MessageHandler
        ( emits = {EndOfRoundMessage.class}
        , reads = {DownedMessage.class, BideMessage.class, ActionMessage.class}
)
public class DecisionEngine extends Engine {
    @ComponentAccess(componentType = EncounterComponent.class, mutator = true)
    private Mutator<EncounterComponent> encounterData;

    @ComponentAccess(componentType = PlanComponent.class, mutator = true)
    private Mutator<PlanComponent> decisionData;
    public void beforeHandling() {

    }

    public void handle(Message m) {
        if (m instanceof ActionMessage) {
            // get rid of the old decision, if there is one
            decisionData.remove(m.originId);

            ActionMessage im = (ActionMessage) m;
            // create a new plan, if the action isn't immediate
            if (im.plan.beats > 0)
                decisionData.put
                        ( im.originId
                                , new PlanComponent
                                        ( im.originId
                                                , im.plan
                                                , im.targetId
                                                , im.plan.beats
                                        )
                        );
        } else if (m instanceof BideMessage) {
            decisionData.tick(m.originId);
        } else if (m instanceof DownedMessage) {
            decisionData.remove(m.originId);

        }
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet<>();
        encounterData.tick();

        for (EncounterComponent ec : encounterData.all()) {
            if (ec.getFrame() % BEATS_PER_ROUND == 0)
                messages.add(new EndOfRoundMessage(ec.eid));
        }
        return messages;
    }

    public void afterFrame() {

    }
}
