package com.antekk.flappybird.game.player;

public class FlappyBirdPlayer {
    public static final PlayersStatsJSON playerStats = new PlayersStatsJSON();
    public long score = 0;
    public int pipesVerticalGap = 0;
    public String name;

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
