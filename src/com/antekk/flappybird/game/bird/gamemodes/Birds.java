package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.ai.Neuron;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Birds implements Iterable<Bird> {
    protected ArrayList<Bird> mlBirdsArray = new ArrayList<>();
    protected Bird playerControlledBird;

    private GameMode gameMode;

    public Birds(GameMode gameMode) {
        setGameMode(gameMode);
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
        gameMode.init(this);
    }

    public void draw(Graphics g) {
        gameMode.draw(g, this);
    }

    public void drawWithoutRotation(Graphics g) {
        gameMode.drawWithoutRotation(g, this);
    }

    public void resetPosition() {
        gameMode.resetPosition(this);
    }

    public void flap() {
        gameMode.flap(this);
    }

    public Bird isBetweenPipes(PipeFormation pipeFormation) {
        return gameMode.isBetweenPipes(pipeFormation, this);
    }

    public boolean areAllBirdsDead() {
        return gameMode.areAllBirdsDead(this);
    }

    public int size() {
        return gameMode.size(this);
    }

    public Bird getBirdAt(int index) {
        return gameMode.getBirdAt(index, this);
    }

    @Override
    public Iterator<Bird> iterator() {
        return gameMode.iterator(this);
    }

    public Bird getPlayerControlledBird() {
        return playerControlledBird;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ArrayList<FlappyBirdPlayer> getPlayers() {
        return gameMode.players(this);
    }

    public int getGenerationNumber() {
        if(!(getGameMode() instanceof MachineLearningMode)) return 0;
        return ((MachineLearningMode) gameMode).getGenerationNumber();
    }

    public FlappyBirdPlayer getBestPlayer() {
        FlappyBirdPlayer bestPlayer = getPlayers().get(0);
        for(FlappyBirdPlayer player : getPlayers()) {
            if(player.score > bestPlayer.score)
                bestPlayer = player;
        }
        return bestPlayer;
    }
}
