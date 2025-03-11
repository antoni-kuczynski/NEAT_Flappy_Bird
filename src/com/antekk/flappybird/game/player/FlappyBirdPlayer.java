package com.antekk.flappybird.game.player;

public class FlappyBirdPlayer {
    public static final PlayersStatsJSON playerStats = new PlayersStatsJSON();
    public long score = 0;
    public int hGap = 0;
    public String name;

    public void addScore() {
        score++;
    }

    public FlappyBirdPlayer() {
        this.hGap = -1; //TODO temp
    }

    public FlappyBirdPlayer(long score, int hGap, String name) {
        this.score = score;
        this.hGap = hGap;
        this.name = name;
    }

    public static PlayersStatsJSON getStatsFile() {
        return playerStats;
    }
}
