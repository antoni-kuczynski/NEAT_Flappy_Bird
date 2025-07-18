package com.antekk.flappybird.game.player;

import com.antekk.flappybird.game.ConfigJSON;

public class FlappyBirdPlayer {
    private static final PlayersStatsJSON playerStats = new PlayersStatsJSON();
    public long score;
    public int pipesVerticalGap;
    public String name;
    public boolean wasScoreAddedAtPipe = false;

    public void addScore() {
        score++;
    }

    public FlappyBirdPlayer() {
        this.score = 0;
        this.pipesVerticalGap = ConfigJSON.getPipesVGap();
        this.name = null;
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
