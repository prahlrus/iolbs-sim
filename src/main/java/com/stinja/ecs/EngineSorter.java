package com.stinja.ecs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EngineSorter {
    private Class<? extends Engine>[] es;
    private Class<? extends Message>[][] emission;
    private Class<? extends Message>[][] consumption;
    private List<Engine> result;

    private Set<Class<? extends Engine>> marked;
    private Set<Class<? extends Engine>> tempMarked;

    public EngineSorter(Class<? extends Engine>[] toSort) {
        this.es = toSort;
        this.emission = new Class[es.length][];
        this.consumption = new Class[es.length][];

        // populate edge information
        for (int i = 0; i < es.length; i++) {
            Class<? extends Engine> engineClazz = es[i];
            if (engineClazz.isAnnotationPresent(MessageHandler.class)) {
                MessageHandler mh = engineClazz.getDeclaredAnnotation(MessageHandler.class);
                this.emission[i] = mh.emits();
                this.consumption[i] = mh.reads();
            } else {
                this.emission[i] = new Class[0];
                this.consumption[i] = new Class[0];
            }
        }

        this.marked = new HashSet<>();
        this.tempMarked = new HashSet<>();
        this.result = new LinkedList<>();
    }

    public List<Engine> sort() {
        this.result.clear();

        for (int x = 0; x < es.length; x++)
            visit(x);

        return  result;
    }

    private void visit(int x) {
        Class<? extends Engine> clazz = es[x];
        if (marked.contains(clazz))
            return;
        else if (tempMarked.contains(clazz))
            throw new RuntimeException
                    ("A cycle was encountered when sorting the Engines.");

        tempMarked.add(clazz);

        for (int y = 0; y < emission.length; y++) {
            boolean linked = false;
            for (Class<? extends Message> messageType0 : consumption[x]) {
                for (Class<? extends Message> messageType1 : emission[y]) {
                    if (messageType0 == messageType1) {
                        visit(y);
                        linked = true;
                        break;
                    }
                }
                if (linked) break;
            }
        }

        tempMarked.remove(clazz);
        marked.add(clazz);
        try {
            result.add(clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format
                            ("Caught a %s while trying to instantiate Engine of type %s; an Engine must have a public no-argument constructor."
                            , e.getClass().getName()
                            , clazz.getName()
                            )
                    );
        }
    }
}
