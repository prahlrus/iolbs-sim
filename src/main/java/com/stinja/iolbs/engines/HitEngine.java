package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.components.DamageComponent;
import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.components.VitalsComponent;
import com.stinja.iolbs.messages.DownedMessage;
import com.stinja.iolbs.messages.HurtMessage;
import com.stinja.iolbs.rules.Dice;

import java.util.HashSet;
import java.util.Set;

/**
 * Responsible for the rolling of hit dice after damage has been taken. If a figure fails their
 * hit die roll, this engine removes the {@link com.stinja.iolbs.components.PlayerComponent PlayerComponent} or
 * {@link com.stinja.iolbs.components.MonsterComponent MonsterComponent} that represents that figure's
 * continued presence in the fight and emits a {@link com.stinja.iolbs.messages.DownedMessage DownedMessage} to
 * indicate as much.
 *
 * @author Will Zev Prahl
 * @version 0.2
 */

@MessageHandler
        ( emits = {DownedMessage.class}
        , reads = {HurtMessage.class}
        )
public class HitEngine extends Engine {
    @ComponentAccess( componentType = MonsterComponent.class, mutator = true )
    private Mutator<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class, mutator = true )
    private Mutator<PlayerComponent> playerData;

    @ComponentAccess( componentType = DamageComponent.class, mutator = true)
    private Mutator<DamageComponent> damageData;

    public HitEngine() {
        messages = new HashSet<>();
    }

    private Set<Message> messages;

    public void beforeHandling() {
        messages.clear();
    }

    public void handle(Message m) {
        if (m instanceof HurtMessage) {
            HurtMessage hm = (HurtMessage) m;
            int eid = hm.originId;
            VitalsComponent fc = null;
            if (playerData.exists(eid))
                fc = playerData.get(eid);
            else if (monsterData.exists(eid))
                fc = monsterData.get(eid);

            if (fc == null) return;

            int total = hm.damage;
            if (damageData.exists(eid)) {
                total += damageData.get(eid).amount;
            }

            if (Dice.hit(fc.hd) < total) {
//                System.out.printf("\t\t%d failed their hit die roll.\n", eid);

                playerData.remove(eid);
                monsterData.remove(eid);
                damageData.remove(eid);

                messages.add(new DownedMessage(eid));
            } else {
//                System.out.printf("\t\t%d passed their hit die roll.\n", eid);

                damageData.put(eid, new DamageComponent(eid, total));
            }
        }
    }

    public Set<Message> frame() {
        return messages;
    }

    public void afterFrame() {

    }
}
