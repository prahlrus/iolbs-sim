package com.stinja.iolbs.engines;

import com.stinja.ecs.Accessor;
import com.stinja.ecs.ComponentAccess;
import com.stinja.ecs.Engine;
import com.stinja.ecs.Message;
import com.stinja.ecs.MessageHandler;
import com.stinja.iolbs.messages.DisengagementMessage;
import com.stinja.iolbs.messages.EngagementMessage;
import com.stinja.iolbs.messages.ActionMessage;
import com.stinja.iolbs.messages.BideMessage;
import com.stinja.iolbs.rules.Plan;
import com.stinja.iolbs.components.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.stinja.iolbs.components.EncounterComponent.BEATS_PER_ROUND;

/**
 * Responsible for choosing what each figure is going to do at the beginning of each
 * frame. Each figure can produce up to one {@link com.stinja.iolbs.messages.BideMessage BideMessage},
 * {@link com.stinja.iolbs.messages.DisengagementMessage DisengagementMessage},
 * {@link com.stinja.iolbs.messages.EngagementMessage EngagementMessage} or
 * {@link com.stinja.iolbs.messages.ActionMessage ActionMessage} per frame.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */

@MessageHandler
        ( emits =
            { BideMessage.class
            , DisengagementMessage.class
            , EngagementMessage.class
            , ActionMessage.class
            }
        , reads = {}
        )
public class StrategyEngine extends Engine {

    @ComponentAccess(componentType = EncounterComponent.class)
    Accessor<EncounterComponent> encounterData;

    @ComponentAccess(componentType = PlayerComponent.class)
    Accessor<PlayerComponent> playerData;

    @ComponentAccess(componentType = MonsterComponent.class)
    Accessor<MonsterComponent> monsterData;

    @ComponentAccess(componentType = InitiativeComponent.class)
    Accessor<InitiativeComponent> initiativeData;

    @ComponentAccess(componentType = TappedComponent.class)
    Accessor<TappedComponent> tappedData;

    @ComponentAccess(componentType = PlanComponent.class)
    Accessor<PlanComponent> decisionData;

    @ComponentAccess(componentType = EngagedByComponent.class)
    Accessor<EngagedByComponent> engagedByData;

    @ComponentAccess(componentType = ReflexComponent.class)
    Accessor<ReflexComponent> reactionData;

    @ComponentAccess(componentType = EngagingComponent.class)
    Accessor<EngagingComponent> engagingData;


    public void beforeHandling() {
    }

    public void handle(Message m) {
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet<>();

        for (EncounterComponent ec : encounterData.all()) {
            int beat = ec.getFrame() % BEATS_PER_ROUND;
            for (PlayerComponent pc : playerData.all()) {
                Message m = getIntention(pc.eid, false, beat);
                if (m != null)
                    messages.add(m);
            }

            for (MonsterComponent mc : monsterData.all()) {
                Message m = getIntention(mc.eid, true, beat);
                if (m != null)
                    messages.add(m);
            }
        }

        return messages;
    }

    private Message getIntention(int eid, boolean targetPlayers, int beat) {
        if (! initiativeData.exists(eid) || tappedData.exists(eid))
            return null;

        Plan[] options = initiativeData.get(eid).options[beat];
        if (options.length == 0) // nothing can be done this beat
            return null;

        Plan plan = null;
        int targetId = -1;

        if (engagedByData.exists(eid) && reactionData.exists(eid)) {

            Plan[] tactics = reactionData.get(eid).tactics;
            plan = tactics[(int) (Math.random() * tactics.length)];
            int opponentId = engagedByData.get(eid).opponentId;

            if (plan.target.specific)
                targetId = opponentId;
            else
                targetId = eid;

            return new ActionMessage(eid, plan, targetId);
        } else if (decisionData.exists(eid)) {
            PlanComponent dc = decisionData.get(eid);

            for (Plan s : options) {
                if (dc.plan == s) {
                    plan = dc.plan;
                    break;
                }
            }

            // only emit messages for actions that are allowed on this beat
            if (plan == null)
                return null;

            // the chosen target is valid
            if (plan.target.specific
                &&  ( targetPlayers
                    ? playerData.exists(targetId)
                    : monsterData.exists(targetId) )) {
                targetId = dc.targetId;
            }
            // or the strategy doesn't target a specific target
            else if (! plan.target.specific) {
                targetId = eid;
            }
            // otherwise, the decision needs replaced
            else {
                plan = null;
            }

            // there's time remaining
            if (plan != null && dc.getBeats() > 0) {
                return new BideMessage(eid);
            }
        }

        if (plan == null)
            plan = options[(int) (Math.random() * options.length)];

        if (targetId < 0) {
            if (plan.target.specific) {
                List<? extends VitalsComponent> candidates;
                if (targetPlayers)
                    candidates = new ArrayList<>(playerData.all());
                else
                    candidates = new ArrayList<>(monsterData.all());
                targetId = candidates.get((int) (Math.random() * candidates.size())).eid;
            } else
                targetId = eid;
        }

        // determine if the action can be taken, or we need to engage the enemy
        if (plan.target.close) {
            EngagingComponent engage = null;
            if (engagingData.exists(eid))
                engage = engagingData.get(eid);

            if (engage != null && engage.targetId == targetId) {
                return new ActionMessage(eid, plan, targetId);
            }
            else {
                return new EngagementMessage(eid, targetId);
            }
        } else {
            if (engagedByData.exists(eid))
                return new DisengagementMessage(eid);
            else
                return new ActionMessage(eid, plan, targetId);
        }
    }

    public void afterFrame() {
    }
}
