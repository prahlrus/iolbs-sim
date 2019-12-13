package com.stinja.ecs;

public interface Component<T> {
    int getId();
    T clone(int newId);
}
