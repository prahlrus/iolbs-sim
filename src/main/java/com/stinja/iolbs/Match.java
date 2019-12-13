package com.stinja.iolbs;

import java.util.*;

public class Match {
    List<Figure> players;
    List<Figure> enemies;
    List<Figure>[] actors;
    Map<Figure,Figure> engagement;

    final int MAX_ACTIONS = 4;

    Match(List<Figure> players, List<Figure> enemies) {
        this.actors = new List[MAX_ACTIONS];
        for (int x = 0; x < MAX_ACTIONS; x++)
            this.actors[x] = new LinkedList<>();

        this.players = players;
        for (Figure player : players)
            addActor(player);

        this.enemies = enemies;
        for (Figure enemy : enemies)
            addActor(enemy);

        engagement = new HashMap<>();
    }

    void addActor(Figure f) {
        for (int x = MAX_ACTIONS - f.actions; x < MAX_ACTIONS; x++) {
            this.actors[x].add(f);
        }
    }

    void removeActor(Figure f) {
        for (int x = MAX_ACTIONS - f.actions; x < MAX_ACTIONS; x++) {
            this.actors[x].remove(f);
        }

        players.remove(f);
        enemies.remove(f);
    }

    Figure nextInLine(boolean enemy, Set<Figure> downed) {
        List<Figure> line = enemy ? enemies : players;

        Figure next = null;
        for (Figure f : line) {
            if (downed.contains(f))
                continue;
            if (!engagement.containsValue(f))
                return f;
            if (next == null)
                next = f;
        }
        return next;
    }

    void engage(Figure attacker, Figure defender) {
        engagement.put(attacker, defender);
    }

    int run() {
        Set<Figure> tapped = new HashSet<>();
        Set<Figure> downed = new HashSet<>();
        int rounds = 0;

        while (players.size() > 0 && enemies.size() > 0) {
            rounds++;
            for (List<Figure> actor : actors) {
                // System.out.printf("Round %d, turn %d.%n", rounds, turn);

                for (Figure f : actor) {
                    // System.out.printf("%s up.%n", f);

                    if (tapped.contains(f))
                        continue;
                    // System.out.printf("%s will act.%n", f);

                    if (engagement.containsKey(f)) {
                        Figure g = engagement.get(f);
                        //System.out.printf("%s v %s", f, g);
                        // opponent already down
                        if (downed.contains(g)) {
                            //System.out.printf(" but %s is down.%n", g);

                            engagement.remove(f);
                            if (f.act
                                    (this
                                            , players.contains(f)
                                            , downed
                                    ))
                                tapped.add(f);
                            // opponent still fighting
                        } else {
                            //System.out.println(".");

                            if (f.fight(g, 0)) {
                                //System.out.printf("%s goes down.%n", g);

                                downed.add(g);
                                engagement.remove(f);
                            }
                            // else System.out.printf("%s is still in it.%n", g);

                            tapped.add(f);
                        }
                    } else {
                        if (f.act
                                (this
                                        , players.contains(f)
                                        , downed
                                ))
                            tapped.add(f);
                    }
                }

                for (Figure d : downed)
                    removeActor(d);

                downed.clear();
            }
            tapped.clear();
        }

        return rounds;
    }
}
