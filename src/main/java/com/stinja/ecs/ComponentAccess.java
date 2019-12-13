package com.stinja.ecs;

public @interface ComponentAccess {
    Class componentType();
    boolean mutator();
}
