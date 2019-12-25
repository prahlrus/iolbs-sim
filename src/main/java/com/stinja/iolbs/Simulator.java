package com.stinja.iolbs;

import com.stinja.iolbs.rules.Plan;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Hello world!
 *
 */
public class Simulator {
    private static final int TRIALS = 1000;
    // the maximum ratio of one party size to the other
    private static final int MAX_ODDS = 12;
    // the maximum rank of the opposing fighters
    private static final int MAX_RANK = 8;
    private static final double BALANCE_POINT = 0.5;
    private static String[] playerNames =
            { "Arthur"      // 0
            , "Bors"        // 1
            , "Gunther"     // 2
            , "Dietrich"    // 3
            , "Hagen"       // 4
            , "Walwain"     // 5
            , "Zelda"       // 6
            , "Hector"      // 7
            , "Tristan"     // 8
            , "Yvain"       // 9
            , "Kay"         // 10
            , "Lancelot"    // 11
            };

    public static void main(String[] args) {
        String bestiaryFilename = args[0];

        Reader bestiaryFile;
        Iterable<CSVRecord> records;
        try {
            bestiaryFile = new FileReader(bestiaryFilename);
            records = CSVFormat.DEFAULT.parse(bestiaryFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file as " + bestiaryFilename + ".");
        } catch (IOException e) {
            throw new RuntimeException("IOException while parsing " + bestiaryFilename + ".");
        }

        Set<Monster> bestiary = new HashSet<>();

        Set<Plan> optionSet = new HashSet<>();
        Set<Plan> freeOptionSet = new HashSet<>();
        Set<Plan> reflexSet = new HashSet<>();

        String name;
        int hd;
        int ac;
        int thAC;
        int save;
        int initiative;
        Plan[] options;
        Plan[] freeOptions;
        Plan[] reflexes;

        for (CSVRecord record : records) {
            name = record.get("Name");
            hd = Integer.parseInt(record.get("HD"));
            ac = Integer.parseInt(record.get("AC"));
            thAC = Integer.parseInt(record.get("THAC0"));
            save = Integer.parseInt(record.get("Saving Throw"));
            initiative = Integer.parseInt(record.get("Initiative"));

            for (String planString : record.get("Action Options").split("/")) {
                Plan p = Plan.parse(planString);
                if (p != null)
                    optionSet.add(p);
            }
            options = optionSet.toArray(new Plan[0]);
            optionSet.clear();

            for (String planString : record.get("Free Actions").split("/")) {
                Plan p = Plan.parse(planString);
                if (p != null)
                    freeOptionSet.add(p);
            }
            freeOptions = freeOptionSet.toArray(new Plan[0]);
            freeOptionSet.clear();

            for (String planString : record.get("Reflex Actions").split("/")) {
                Plan p = Plan.parse(planString);
                if (p != null)
                    reflexSet.add(p);
            }
            reflexes = reflexSet.toArray(new Plan[0]);
            reflexSet.clear();

            bestiary.add(
                    new Monster
                            ( name
                                    , hd
                                    , ac
                                    , thAC
                                    , save
                                    , initiative
                                    , options
                                    , freeOptions
                                    , reflexes
                            )
            );
        }

        System.out.printf("%40s", "Warriors of rank ");
        for (int rank = 0; rank < MAX_RANK; rank++) {
            System.out.printf("|%-4d", rank);
        }
        System.out.println();

        for (Monster monster : bestiary)
            tableRow(monster);
    }

    private static void tableRow(Monster monster) {
        System.out.println("----------------------------------------|----|----|----|----|----|----|----|----");
        System.out.printf("%-40s", monster + " is matched with");
        for (int rank = 0; rank < MAX_RANK; rank++) {
            double[] trialResults = runTrials(monster, rank);
            int warriors = balance(trialResults);
            if (warriors > 0)
                System.out.printf("|%-4d", warriors);
            else
                System.out.printf("|1/%-2d", 1 - warriors);
        }
        System.out.println();
    }

    private static int balance(double[] tpkChances) {
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

    private static double[] runTrials(Monster m, int rank) {
        double[] results = new double[2 * (MAX_ODDS - 1)];
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

            Set<Figure> figures = new HashSet<>();

            for (int y = 0; y < playersCount; y++)
                figures.add(new Player(playerNames[y],Player.Type.HEAVY, rank, 0));
            for (int y = 0; y < enemiesCount; y++)
                figures.add(new Monster(m));

            for (int t = 0; t < TRIALS; t++) {
                Match match = new Match(figures);
                MatchResult matchResult = match.run();
                if (matchResult.playerSurvival.length > 0)
                    results[x]++;
            }

            results[x] /= TRIALS;
        }

        return results;
    }





}
