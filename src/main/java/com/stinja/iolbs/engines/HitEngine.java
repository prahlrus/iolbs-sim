package com.stinja.iolbs.engines;

import com.stinja.ecs.*;
import com.stinja.iolbs.Dice;
import com.stinja.iolbs.components.DamageComponent;
import com.stinja.iolbs.components.FigureComponent;
import com.stinja.iolbs.components.MonsterComponent;
import com.stinja.iolbs.components.PlayerComponent;
import com.stinja.iolbs.messages.HurtMessage;

import java.util.HashSet;
import java.util.Set;

@MessageHandler
        ( emits = { }
        , reads = {HurtMessage.class}
        )
public class HitEngine extends Engine {
    @ComponentAccess( componentType = MonsterComponent.class, mutator = true )
    private Mutator<MonsterComponent> monsterData;

    @ComponentAccess( componentType = PlayerComponent.class, mutator = true )
    private Mutator<PlayerComponent> playerData;

    @ComponentAccess( componentType = DamageComponent.class, mutator = true)
    private Mutator<DamageComponent> damageData;

    public void beforeHandling() {

    }

    public void handle(Message m) {
        if (m instanceof HurtMessage) {
            HurtMessage hm = (HurtMessage) m;
            int eid = hm.originId;
            FigureComponent fc = null;
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
                playerData.remove(eid);
                monsterData.remove(eid);
                damageData.remove(eid);
            } else {
                damageData.put(eid, new DamageComponent(eid, total));
            }
        }
    }

    public Set<Message> frame() {
        return new HashSet<>();
    }

    public void afterFrame() {

    }
}
