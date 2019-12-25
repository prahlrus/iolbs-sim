package com.stinja.iolbs;

public class MatchResult {
    public final int length;
    public final String[] playerSurvival;
    public final int monsterSurvival;

    public MatchResult(int length, String[] playerSurvival, int monsterSurvival) {
        this.length = length;
        this.playerSurvival = playerSurvival;
        this.monsterSurvival = monsterSurvival;
    }
}
