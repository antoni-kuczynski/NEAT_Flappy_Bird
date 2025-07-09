package com.antekk.flappybird.game.player;

public class FlappyBirdPlayer {
    private static final PlayersStatsJSON playerStats = new PlayersStatsJSON();
    public long score = 0;
    public int pipesVerticalGap = 0;
    public String name;
    public boolean wasScoreAddedAtPipe = false;

    public void addScore() {
        score++;
    }

    public FlappyBirdPlayer() {
        this.pipesVerticalGap = -1; //TODO temp
    }

    public FlappyBirdPlayer(long score, int pipesVerticalGap, String name) {
        this.score = score;
        this.pipesVerticalGap = pipesVerticalGap;
        this.name = name;
    }

    public static PlayersStatsJSON getStatsFile() {
        return playerStats;
    }
}
