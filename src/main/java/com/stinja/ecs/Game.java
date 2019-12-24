package com.stinja.ecs;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Game {
    private List<Message> queue;
    private List<Engine> engines;
    private Map<Class,Set<Engine>> messageTypeHandlers;
    private Map<Class,Mutator> componentData;

    public Game() {
        this.queue = new LinkedList<>();
        this.engines = null;
        this.messageTypeHandlers = new HashMap<>();
        this.componentData = new HashMap<>();
    }

    public void pass(Message m) {
        queue.add(m);
    }

    public void frame() {
        if (engines == null)
            throw new RuntimeException("Cannot simulate a frame when before the game has been started!");

        queue.clear();
        for (Engine e : engines) {
            e.beforeHandling();
            for (Message m : queue) {
                if (messageTypeHandlers.containsKey(m.getClass()) && messageTypeHandlers.get(m.getClass()).contains(e))
                    e.handle(m);
            }
            queue.addAll(e.frame());
            e.afterFrame();
        }
    }

    public void start(Collection<Engine> es) {
        System.err.printf("Sorting %d engines...", es.size());
        EngineSorter sorter = new EngineSorter(es);
        engines = sorter.sort();
        System.err.println("Done.");


        Map<Class,Engine> mutationRights = new HashMap<>();

        System.err.print("Injecting component dependencies...");
        for (Engine e : engines) {
            if (e.getClass().isAnnotationPresent(MessageHandler.class)) {
                MessageHandler mh = e.getClass().getDeclaredAnnotation(MessageHandler.class);
                if (mh.reads() != null) {
                    for (Class messageType : mh.reads()) {
                        Set<Engine> handlers;
                        if (! messageTypeHandlers.containsKey(messageType))
                            messageTypeHandlers.put(messageType, handlers = new HashSet<>());
                        else
                            handlers = messageTypeHandlers.get(messageType);
                        handlers.add(e);
                    }
                }
            }

            for (Field f : e.getClass().getDeclaredFields()){
                if (! f.isAnnotationPresent(ComponentAccess.class))
                    continue;
                f.setAccessible(true);
                ComponentAccess cs = f.getDeclaredAnnotation(ComponentAccess.class);
                Class componentType = cs.componentType();
                if (! componentData.containsKey(componentType)) {
                    componentData.put(componentType, new Mutator());
                }

                try {
                    if (cs.mutator()) {
                        if (mutationRights.containsKey(componentType) && mutationRights.get(componentType) != e)
                            throw new RuntimeException(String.format
                                ( "An Engine of type %s tried to claim mutation rights for a component of type %s, but only one Engine may claim mutation rights for any component type."
                                , e.getClass().getName()
                                , componentType.getName()
                                )
                            );
                        mutationRights.put(componentType,e);
                        f.set(e, componentData.get(componentType));
                    } else {
                        f.set(e, componentData.get(componentType).readOnly());
                    }
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException("Could not inject dependency for field " + f.getName() + ".");
                }
            }

            System.err.println("Done.");
        }
    }

    public void load(Class componentType, Set<Component> componentSet) {
        Mutator m;
        if (componentData.containsKey(componentType)) {
            m = componentData.get(componentType);
        } else {
            componentData.put(componentType, m = new Mutator());
        }

        if (componentSet == null)
            return;

        for (Component c : componentSet) {
            if (c.getClass() == componentType)
                m.put(c.getId(), c);
            else
                throw new RuntimeException(
                        String.format
                                ( "Illegal attempt to load data of type %s (type %s expected)."
                                , c.getClass().getName()
                                , componentType.getName()
                                )
                    );
        }
    }
}
