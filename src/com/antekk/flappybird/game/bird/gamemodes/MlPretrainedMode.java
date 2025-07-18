package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MlPretrainedMode implements GameMode {
    private Bird pretrainedBird;
    private ArrayList<FlappyBirdPlayer> players = new ArrayList<>();
    private NeuralNetwork birdsNeuralNetwork = new NeuralNetwork();

    @Override
    public void resetPosition() {
        pretrainedBird.resetPosition();
    }

    @Override
    public void flap() {
        pretrainedBird.flap();
    }

    @Override
    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        return pretrainedBird.isBetweenPipes(pipeFormation);
    }

    @Override
    public boolean areAllBirdsDead() {
        return !pretrainedBird.isAlive;
    }

    @Override
    public Iterator<Bird> iterator() {
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
                return pretrainedBird;
            }
        };
    }

    @Override
    public void init() {
        if(birdsNeuralNetwork == null) {
            pretrainedBird = new Bird(new NeuralNetwork());
            return;
        }
        pretrainedBird = new Bird(birdsNeuralNetwork);
    }

    @Override
    public boolean usesMachineLearning() {
        return true;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public Bird getBirdAt(int index) {
        if(index > 0) throw new IllegalArgumentException("Index can't be higher than 0 in pretrained mode (index = " + index + ").");
        return pretrainedBird;
    }

    @Override
    public ArrayList<FlappyBirdPlayer> getPlayers() {
        if(players.isEmpty()) players.add(pretrainedBird.getPlayer());
        return players;
    }

    @Override
    public FlappyBirdPlayer getBestPlayer() {
        return pretrainedBird.getPlayer();
    }

    @Override
    public Bird getPlayerControlledBird() {
        return null;
    }

    @Override
    public ArrayList<Bird> getBirds() {
        return new ArrayList<>(Arrays.asList(pretrainedBird));
    }

    @Override
    public String toString() {
        return "Pretrained mode";
    }

    public void setBirdsNeuralNetwork(NeuralNetwork birdsNeuralNetwork) {
        this.birdsNeuralNetwork = birdsNeuralNetwork;
        init();
    }
}
