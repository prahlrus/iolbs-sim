package com.stinja.iolbs.components;

import com.stinja.ecs.Component;

public class MonsterCharacter implements Component<MonsterCharacter> {
    private int id;

    public MonsterCharacter(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public MonsterCharacter clone(int newId) {
        return new MonsterCharacter(newId);
    }
}