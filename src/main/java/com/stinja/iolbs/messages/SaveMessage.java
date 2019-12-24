package com.stinja.iolbs.messages;

import com.stinja.ecs.Message;
import com.stinja.iolbs.Damage;
import com.stinja.iolbs.Save;

public class SaveMessage extends Message {
    public final Damage source;
    public final int saveClass;

    public SaveMessage(int originId, Damage source, int saveClass) {
        super(originId);
        this.source = source;
        this.saveClass = saveClass;
    }
}
