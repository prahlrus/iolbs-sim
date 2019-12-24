package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.Action;
import com.stinja.iolbs.components.*;
import com.stinja.iolbs.messages.AttackMessage;
import com.stinja.iolbs.messages.TapMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.stinja.iolbs.components.EncounterComponent.BEATS_PER_ROUND;

@MessageHandler
        ( emits = {AttackMessage.class, TapMessage.class}
        , reads = {}
        )
public class DecisionEngine extends Engine {

    @ComponentAccess(componentType = EncounterComponent.class, mutator = false)
    private Accessor<EncounterComponent> encounterData;

    @ComponentAccess(componentType = InitiativeComponent.class, mutator = false)
    private Accessor<InitiativeComponent> initiativeData;

    @ComponentAccess( componentType = MonsterComponent.class, mutator = false )
    private Accessor<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class, mutator = false )
    private Accessor<PlayerComponent> playerData;

    @ComponentAccess( componentType = DecisionComponent.class, mutator = true )
    private Mutator<DecisionComponent> decisionData;

    @ComponentAccess( componentType = EngagementComponent.class, mutator = true )
    private Mutator<EngagementComponent> engagedData;

    @ComponentAccess( componentType = TappedComponent.class, mutator = false)
    private Accessor<TappedComponent> tappedData;

    public void beforeHandling() {
    }

    public void handle(Message m) {
    }

    public Set<Message> frame() {
        Set<Message> messages = new HashSet<>();

        for (EncounterComponent ec : encounterData.all()) {
            int beat = ec.getFrame() % BEATS_PER_ROUND;

            for (InitiativeComponent ic : initiativeData.all()) {
                Component alignment = null;
                int eid = ic.eid;
                if (playerData.exists(eid))
                    alignment = playerData.get(eid);
                else if (monsterData.exists(eid))
                    alignment = monsterData.get(eid);

                if (alignment == null)
                    continue; // out of the fight

                if (decisionData.exists(eid)) {
                    if (tappedData.exists(eid))
                        continue; // don't take a turn

                    DecisionComponent dc = decisionData.get(eid);
                    boolean targetUp = playerData.exists(dc.targetId) || monsterData.exists(dc.targetId);
                    if (dc.getBeats() > 0 && targetUp) {
                        dc.tick();
                        continue; // don't pick a new decision
                    }

                    decisionData.remove(eid); // either the decision is invalid or it's time to take it

                    if (targetUp) {
                        messages.add
                            ( new AttackMessage
                                ( eid
                                , dc.action
                                , dc.targetId
                                , (dc.action.save == null) ? 0 : 2 - dc.getBeats(),
                                        toHit)
                            );
                        if (dc.action.full)
                            messages.add(new TapMessage(eid));
                        continue; // don't pick a new decision
                    }
                }
                // try to make a new decision
                Action intent = selectAction(ic, beat);
                if (intent != null) {
                    decisionData.put
                        ( eid
                        , new DecisionComponent
                            ( eid
                            , intent
                            , selectSingleTarget
                                ( alignment instanceof MonsterComponent )
                            , intent.beats
                            )
                        );
                }
            }

        }
        return messages;
    }

    private Action selectAction(InitiativeComponent init, int beat) {
        Action[] options = init.options[beat];
        Action intent = null;

        if (options != null && options.length > 0) {
            intent = options[(int) (Math.random() * options.length)];
        }

        return intent;
    }

    private int selectSingleTarget(boolean targetPlayer) {
        List<Component> candidates;
        if (targetPlayer)
            candidates = new ArrayList<>(playerData.all());
        else
            candidates = new ArrayList<>(monsterData.all());
        return candidates.get((int) (Math.random() * candidates.size())).eid;
    }

    public void afterFrame() {
    }
}
