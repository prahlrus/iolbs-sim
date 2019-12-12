package com.stinja.iolbs;

import java.util.*;

/**
 * Hello world!
 *
 */
public class Simulator {
    static final int TRIALS = 1000;

    // the maximum ratio of one party size to the other
    static final int MAX_ODDS = 12;
    // the maximum rank of the opposing fighters
    static final int MAX_RANK = 8;

    static final double BALANCE_POINT = 0.5;

    static double[] runTrials(Figure f, int rank) {
        double[] results = new double[2 * MAX_ODDS - 1];
        // results[0] the chance of one tested figure TPKing  MAX_ODDS fighters of rank
        // results[MAX_ODDS - 1] chance of a fighter of rank being killed by one tested figure
        // results[-1] the chance of one fighter of rank being killed by MAX_ODDS tested figures

        for (int x = 0; x < results.length; x++) {
            int ratio = MAX_ODDS - x;
            int playersCount, enemiesCount;

            if (ratio > 0) {
                playersCount = ratio;
                enemiesCount = 1;
            } else {
                playersCount = 1;
                enemiesCount = 1 - ratio;
            }

            List<Figure> players = new LinkedList<>();
            for (int y = 0; y < playersCount; y++)
                players.add(new Heavy("Heavy " + (y + 1), rank));
            Figure[] playerTeam = players.toArray(new Figure[playersCount]);

            List<Figure> enemies = new LinkedList<>();
            for (int y = 0; y < enemiesCount; y++) {
                try {
                    enemies.add((Figure) f.clone());
                } catch (CloneNotSupportedException e) {
                    return null;
                }
            }
            Figure[] enemyTeam = enemies.toArray(new Figure[enemiesCount]);

            for (int t = 0; t < TRIALS; t++) {
                players.clear();
                enemies.clear();

                for (Figure player : playerTeam) {
                    player.refresh();
                    players.add(player);
                }

                for (Figure enemy : enemyTeam) {
                    enemy.refresh();
                    enemies.add(enemy);
                }

                Encounter e = new Encounter(players, enemies);
                e.run();

                if (players.size() == 0)
                    results[x]++;
            }

            results[x] /= TRIALS;
        }

        return results;
    }

    static int balance(double[] tpkChances) {
        for (int x = 0; x < tpkChances.length; x++) {
            if (x+1 >= tpkChances.length)
                break;

            if (tpkChances[x] >= BALANCE_POINT)
                return MAX_ODDS - x;

            if (  tpkChances[x+1] > BALANCE_POINT ) {
                if ((BALANCE_POINT - tpkChances[x]) < (tpkChances[x+1] - BALANCE_POINT))
                    return MAX_ODDS - x;
                else
                    return MAX_ODDS - (x + 1);
            }
        }
        return -MAX_ODDS;
    }

    public static void main(String[] args) {
        System.out.printf("%-40s", "Warriors of rank:");
        for (int rank = 0; rank < MAX_RANK; rank++) {
            System.out.printf("|%-4d", rank);
        }
        System.out.println();

        Figure boggle = new Monster("Boggle", 2, 2, 4, 10);
        System.out.println("----------------------------------------|----|----|----|----|----|----|----|----");
        System.out.printf("%-40s", boggle + " is matched with:");
        for (int rank = 0; rank < MAX_RANK; rank++) {
            double[] trialResults = runTrials(boggle, rank);
            if (trialResults != null) {
                int warriors = balance(trialResults);
                if (warriors > 0)
                    System.out.printf("|%-4d", warriors);
                else
                    System.out.printf("|1/%-2d", 1 - warriors);
            } else {
                System.out.print("|    ");
            }
        }
        System.out.println();

    }

}
