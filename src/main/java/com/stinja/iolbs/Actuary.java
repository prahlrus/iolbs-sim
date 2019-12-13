package com.stinja.iolbs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Actuary {
    List<Figure[]> playerTeams;
    List<Figure[]> enemyTeams;

    final int TRIALS = 100;

    Actuary
            ( List<Figure[]> playerTeams
                    , List<Figure[]> enemyTeams
            ) {
        this.playerTeams = playerTeams;
        this.enemyTeams = enemyTeams;
    }

    void makeMatches() {
        for (Figure[] enemyTeam : enemyTeams) {
            System.out.print("ENEMIES: ");
            for (int x = 0; x < enemyTeam.length; x++) {
                System.out.print( enemyTeam[x]);
                if (x + 1 < enemyTeam.length)
                    System.out.print(", ");
            }
            System.out.println();

            for (Figure[] playerTeam : playerTeams) {
                System.out.print("MATCHED AGAINST: ");
                for (int x = 0; x < playerTeam.length; x++) {
                    System.out.print( playerTeam[x]);
                    if (x + 1 < playerTeam.length)
                        System.out.print(", ");
                }
                System.out.println();
                match(playerTeam, enemyTeam);
            }

            System.out.println("---------------------------------");
            System.out.println();
        }
    }

    void match(Figure[] playerTeam, Figure[] enemyTeam) {
        double[] survival = new double[playerTeam.length];
        double rounds = 0;

        for (int t = 0; t < TRIALS; t++) {
            List<Figure> players = new LinkedList<>();
            List<Figure> enemies = new LinkedList<>();

            for (Figure f : playerTeam) {
                f.refresh();
                players.add(f);
            }
            for (Figure f : enemyTeam) {
                f.refresh();
                enemies.add(f);
            }

            Match e = new Match(players, enemies);
            rounds += e.run();

            for (int x = 0; x < playerTeam.length; x++) {
                if (players.contains(playerTeam[x])) {
                    survival[x] += 1;
                }
            }
        }

        System.out.printf("Encounter took on average %.1f rounds to resolve.%n", rounds / TRIALS);
        for (int x = 0; x < playerTeam.length; x++)
            System.out.printf
                    ( "%2.2f percent survival for %s%n"
                            , survival[x] / TRIALS
                            , playerTeam[x]
                    );
        System.out.println();
    }

    public static void main(String[] args) {
        Figure arthur = new Heavy("Arthur", 0);
        Figure bors = new Light("Bors", 0);
        // Figure cay = new Light("Cay", 0);
        Figure eric = new Archer("Eric", 0);

        List<Figure[]> playerTeams = new ArrayList<>();
        playerTeams.add(new Figure[]{ arthur });
        playerTeams.add(new Figure[]{ bors });
        // playerTeams.add(new Figure[]{ eric });
        playerTeams.add(new Figure[]{ arthur, bors });
        // playerTeams.add(new Figure[]{ bors, cay });
        playerTeams.add(new Figure[]{ arthur, eric });
        playerTeams.add(new Figure[]{ bors, eric });
        // playerTeams.add(new Figure[]{ arthur, bors, cay });
        playerTeams.add(new Figure[]{ arthur, bors, eric });
        // playerTeams.add(new Figure[]{ arthur, bors, cay, eric });

        Figure b1 = new Monster("Boggle", 2, 1, 5, 10);
        Figure b2 = new Monster("Boggle", 2, 1, 5, 10);
        Figure b3 = new Monster("Boggle", 2, 1, 5, 10);
        Figure b4 = new Monster("Boggle", 2, 1, 5, 10);

        List<Figure[]> enemyTeams = new ArrayList<>();
        enemyTeams.add(new Figure[]{ b1 });
        enemyTeams.add(new Figure[]{ b1, b2 });
        enemyTeams.add(new Figure[]{ b1, b2, b3 });
        enemyTeams.add(new Figure[]{ b1, b2, b3, b4 });

        Actuary mm = new Actuary(playerTeams, enemyTeams);
        mm.makeMatches();
    }
}
