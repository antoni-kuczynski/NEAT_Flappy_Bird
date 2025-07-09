package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class PlayerMode implements GameMode {
    private ArrayList<FlappyBirdPlayer> players;
    private Bird playerControlledBird;

    @Override
    public void draw(Graphics g, Birds birds) {
        playerControlledBird.draw(g);
    }

    @Override
    public void drawWithoutRotation(Graphics g, Birds birds) {
        playerControlledBird.drawWithoutRotation(g);
    }

    @Override
    public void resetPosition(Birds birds) {
        playerControlledBird.resetPosition();
    }

    @Override
    public void flap(Birds birds) {
        playerControlledBird.flap();
    }

    @Override
    public Bird isBetweenPipes(PipeFormation pipeFormation, Birds birds) {
        return playerControlledBird.isBetweenPipes(pipeFormation) ? playerControlledBird : null;
    }

    @Override
    public boolean areAllBirdsDead(Birds birds) {
        return !playerControlledBird.isAlive;
    }

    @Override
    public Iterator<Bird> iterator(Birds birds) {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                if(index < 1) {
                    index++;
                    return true;
                }
                return false;
            }

            @Override
            public Bird next() {
                return playerControlledBird;
            }
        };
    }

    @Override
    public void init(Birds birds) {
        playerControlledBird = new Bird();
//        birds.mlBirdsArray = null;
    }

    @Override
    public boolean usesMachineLearning() {
        return false;
    }

    @Override
    public String toString() {
        return "PLAYER_MODE";
    }

    @Override
    public int size(Birds birds) {
        return 1;
    }

    @Override
    public Bird getBirdAt(int index, Birds birds) {
        if(index > 0)
            throw new IllegalArgumentException("index" + index + " is higher than the size (1)");
        return playerControlledBird;
    }

    @Override
    public ArrayList<FlappyBirdPlayer> players(Birds birds) {
        if(players == null || players.isEmpty())
            players = new ArrayList<>(Arrays.asList(playerControlledBird.getPlayer()));
        return players;
    }

    public Bird getPlayerControlledBird() {
        return playerControlledBird;
    }
}
