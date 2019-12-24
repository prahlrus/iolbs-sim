package com.stinja.ecs;

import java.util.*;

public class EngineSorter {
    private Engine[] es;
    private Class[][] emission;
    private Class[][] consumption;
    private List<Engine> result;

    private Set<Engine> marked;
    private Set<Engine> tempMarked;

    public EngineSorter(Collection toSort) {
        this.es = (Engine[]) toSort.toArray(new Engine[toSort.size()]);
        this.emission = new Class[es.length][];

        // populate edge information
        for (int i = 0; i < es.length; i++) {
            Class engineClazz = es[i].getClass();
            if (engineClazz.isAnnotationPresent(MessageHandler.class)) {
                MessageHandler mh = (MessageHandler) engineClazz.getDeclaredAnnotation(MessageHandler.class);
                if (mh.emits() != null)
                    this.emission[i] = mh.emits();
                else
                    this.emission[i] = new Class[0];

                if (mh.reads() != null)
                    this.consumption[i] = mh.reads();
                else
                    this.consumption[i] = new Class[0];

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
        Engine e = es[x];

        if (marked.contains(e))
            return;
        else if (tempMarked.contains(e))
            throw new RuntimeException
                    ("A cycle was encountered when sorting the Engines.");

        tempMarked.add(e);

        for (int y = 0; y < consumption.length; y++) {
            boolean linked = false;
            for (Class messageType0 : emission[x]) {
                for (Class messageType1 : consumption[y]) {
                    if (messageType0 == messageType1) {
                        visit(y);
                        linked = true;
                        break;
                    }
                }
                if (linked) break;
            }
        }

        tempMarked.remove(e);
        marked.add(e);
        result.add(e);
    }
}
