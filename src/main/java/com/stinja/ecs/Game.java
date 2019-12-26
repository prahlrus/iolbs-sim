package com.stinja.ecs;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Game {
    private List<Message> queue;
    private List<Engine> engines;
    private Map<Class<? extends Message>,Set<Engine>> messageTypeHandlers;
    private Map<Class<? extends Component>,Mutator> componentData;

    public Game(Class<? extends Engine>[] engineTypes) {
        this.queue = new LinkedList<>();
        this.engines = null;
        this.messageTypeHandlers = new HashMap<>();
        this.componentData = new HashMap<>();

        EngineSorter sorter = new EngineSorter(engineTypes);
        engines = sorter.sort();

        Map<Class<? extends Component>,Engine> mutationRights = new HashMap<>();

        for (Engine e : engines) {
            if (e.getClass().isAnnotationPresent(MessageHandler.class)) {
                MessageHandler mh = e.getClass().getDeclaredAnnotation(MessageHandler.class);
                for (Class<? extends Message> messageType : mh.reads()) {
                    Set<Engine> handlers;
                    if (! messageTypeHandlers.containsKey(messageType))
                        messageTypeHandlers.put(messageType, handlers = new HashSet<>());
                    else
                        handlers = messageTypeHandlers.get(messageType);
                    handlers.add(e);
                }
            }

            for (Field f : e.getClass().getDeclaredFields()){
                if (! f.isAnnotationPresent(ComponentAccess.class))
                    continue;
                f.setAccessible(true);
                ComponentAccess cs = f.getDeclaredAnnotation(ComponentAccess.class);
                Class<? extends Component> componentType = cs.componentType();
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
        }
    }

    public void frame() {
        queue.clear();
        for (Engine e : engines) {
//            System.err.printf("%s about to fire, %d messages in the queue.\n", e.getClass().getSimpleName(), queue.size());
            e.beforeHandling();
            for (Message m : queue) {
                if (messageTypeHandlers.containsKey(m.getClass()) && messageTypeHandlers.get(m.getClass()).contains(e)) {
//                    System.err.printf("\tHandling message of type %s.\n", m.getClass().getSimpleName());
                    e.handle(m);
                }
            }
            Set<Message> newMessages = e.frame();
//            System.err.printf("\tEngine fired, %d messages emitted.\n", newMessages.size());
            queue.addAll(newMessages);
            e.afterFrame();
        }
    }

    @SuppressWarnings("unchecked")
    public void load(Class<? extends Component> componentType, Set<? extends Component> componentSet) {
        Mutator m = getMutator(componentType);

        if (componentSet == null)
            return;

        for (Component c : componentSet) {
            if (c.getClass() == componentType)
                m.put(c.eid, c);
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

    public void reset() {
        for (Mutator m : componentData.values()) {
            m.clear();
        }
    }

    protected Mutator getMutator(Class<? extends Component> componentType) {
        if (componentData.containsKey(componentType)) {
            return componentData.get(componentType);
        } else {
            throw new RuntimeException("No mutator exists for component of type " + componentType.getName() + ".");
        }
    }

    protected void componentStatus(int eid) {
        System.err.printf("\nEntity #%d has components: ", eid);
        for (Map.Entry<Class<? extends Component>,Mutator> entry : componentData.entrySet()) {
            Mutator m = entry.getValue();
            if (m.exists(eid)) {
                System.out.printf("\n\t%s", m.get(eid).toString());
            }
        }
    }


}
