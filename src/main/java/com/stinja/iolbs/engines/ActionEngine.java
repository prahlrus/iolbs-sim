package com.stinja.iolbs.engines;

import com.stinja.iolbs.Action;
import com.stinja.iolbs.components.Initiative;

import java.util.Map;

public class ActionEngine {
    private int frame;
    private Map<Integer, Initiative> initiativeComponents;

    public static final int BEATS_PER_ROUND = 4;

    public int getRound() {
        return frame / BEATS_PER_ROUND;
    }

    public void frame() {
        int beat = frame % BEATS_PER_ROUND;

        for (Map.Entry<Integer, Initiative> entry : initiativeComponents.entrySet()) {
            Action[] options = entry.getValue().options[beat];
            if (options != null && options.length > 0) {
                Action current = selectAction(entry.getKey(), options);
            }
        }
    }

    private Action selectAction(int actorId, Action[] options) {
        return options[(int) (Math.random() * options.length)];
    }
}
