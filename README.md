# iolbs-sim
A combat simulator for the IOLBSLOFBG roleplaying game.

# running
To add your own beasts, one need only modify the beasts.csv file.

```
mvn package
java -jar target/iolbs-sim beasts.csv
```

# hyper-ecs
The simulator works using a hyper-entity-component-system architecture. It is likely
that as I add tests and documentation, this framework will be pulled out to
be a project of its own.
 
1. Entities are simply positive integer IDs. 
2. Information about entity state is stored on data objects called Components. These 
inherit from `com.stinja.ecs.Component`, and typically have only `public final` fields. 
Components also have a `.tick()` method, whose purpose is to update any fields that
change predictably over time.
3. Engines (also called 'systems') access and modify component data using game-specific 
rules. They read and write component data using dependency-injected `com.stinja.ecs.Accessor`
or `com.stinja.ecs.Mutator` fields.
4. A `com.stinja.ecs.Game` is created by giving it an array of engine types, loading starting
data into it using `.load()` and repeatedly calling the `frame()` method to run another
frame.

Engines are instantiated and dependency-injected automatically by the `Game` that needs
them. Some restrictions apply:

- Only one type of engine can have mutation rights to a given component type,in order to 
prevent race conditions. If one engine needs to communicate a state change to another,
they should do this by emitting `com.stinja.ecs.Message`s from the `frame()` method.
- Engines need to be annotated with `@MessageHandler` to indicate what message types
they emit and read, so that they can be ordered. If one engine reads `AMessage`, it will
always fire after every engine that emits `AMessage`. If there are cyclical dependencies,
the `Game` cannot be instantiated.