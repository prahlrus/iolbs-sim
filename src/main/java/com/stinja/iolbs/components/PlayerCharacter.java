package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class PlayerCharacter implements Component<PlayerCharacter> {
    private int id;

    public PlayerCharacter(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public PlayerCharacter clone(int newId) {
        return new PlayerCharacter(newId);
    }
}
