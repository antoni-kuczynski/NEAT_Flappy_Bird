package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.ai.Neuron;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.util.ArrayList;
import java.util.Iterator;

public class MlTrainingMode implements GameMode {
    private int generationNumber = 1;
    protected int populationSize = 0;
    private ArrayList<FlappyBirdPlayer> players = new ArrayList<>();
    private ArrayList<Bird> mlBirdsArray = new ArrayList<>();

    @Override
    public void resetPosition() {
        for(Bird bird : mlBirdsArray) bird.resetPosition();
    }

    @Override
    public void flap() {
        for(Bird bird : mlBirdsArray) bird.flap();
    }

    @Override
    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        for(Bird bird : mlBirdsArray) {
            if(bird.isBetweenPipes(pipeFormation)) return true;
        }
        return false;
    }

    @Override
    public boolean areAllBirdsDead() {
        for(Bird bird : mlBirdsArray) {
            if(bird.isAlive)
                return false;
        }
        return true;
    }

    @Override
    public Iterator<Bird> iterator() {
        return mlBirdsArray.iterator();
    }

    @Override
    public void init() {
        this.populationSize = 10;
        mlBirdsArray = new ArrayList<>();
        for(int i = 0; i < populationSize; i++) mlBirdsArray.add(new Bird(new NeuralNetwork()));
        initPlayers();
    }

    @Override
    public boolean usesMachineLearning() {
        return true;
    }

    @Override
    public String toString() {
        return "Training mode";
    }

    @Override
    public int size() {
        return mlBirdsArray.size();
    }

    @Override
    public Bird getBirdAt(int index) {
        return mlBirdsArray.get(index);
    }

    @Override
    public ArrayList<FlappyBirdPlayer> getPlayers() {
        if(players.isEmpty())
            initPlayers();
        return players;
    }

    @Override
    public FlappyBirdPlayer getBestPlayer() {
        FlappyBirdPlayer bestPlayer = players.get(0);
        for(FlappyBirdPlayer player : players) {
            if(player.score > bestPlayer.score)
                bestPlayer = player;
        }
        return bestPlayer;
    }

    @Override
    public Bird getPlayerControlledBird() {
        return null;
    }

    @Override
    public ArrayList<Bird> getBirds() {
        return mlBirdsArray;
    }

    private void initPlayers() {
        players.clear();
        for(Bird bird : mlBirdsArray) players.add(bird.getPlayer());
    }

    private int birdsFitnessSortComparator(Bird b1, Bird b2) {
        if(b1.getFitness() > b2.getFitness())
            return -1;
        else if(b1.getFitness() == b2.getFitness())
            return 0;
        else
            return 1;
    }

    public void newPopulation() {
        ArrayList<Bird> newPopulation = new ArrayList<>();
        mlBirdsArray.sort(this::birdsFitnessSortComparator);

        ArrayList<Bird> top4 = get4FirstBirds();
        for(Bird bird : top4) {
            newPopulation.add(new Bird(bird.brain.clone()));
        }

        newPopulation.add(new Bird(new NeuralNetwork()));

        newPopulation.add(new Bird(top4.get((int) (Math.random() * 4)).brain.clone()));

        newPopulation.add(createOffSpring(top4.get(0), top4.get(1)));

        for (int i = 0; i < 3; i++) {
            int rand1 = (int)(Math.random() * 4);
            int rand2 = (int)(Math.random() * 4);

            while(rand1 == rand2)
                rand2 = (int)(Math.random() * 4);

            Bird parent1 = top4.get(rand1);
            Bird parent2 = top4.get(rand2);
            newPopulation.add(createOffSpring(parent1, parent2));
        }

        for(int i = 5; i < newPopulation.size(); i++) {
            NeuralNetwork network = newPopulation.get(i).brain;
            network.performSwapMutation(0.6f);
        }

        mlBirdsArray.clear();
        mlBirdsArray = newPopulation;

        for(Bird bird : mlBirdsArray)
            bird.resetPosition();

        initPlayers();

        generationNumber++;
    }

    private Bird createOffSpring(Bird p1, Bird p2) {
        if(p1.brain.size() != p2.brain.size())
            throw new IllegalArgumentException("parent1 and parent2 neural network sizes are not equal");

        NeuralNetwork parent1 = p1.brain.clone();
        NeuralNetwork parent2 = p2.brain.clone();


        int size = parent1.size();
        int cutPoint = (int) (Math.random() * (size - 1));
        for(int i = 0; i < size; i++) {
            if(i <= cutPoint)
                continue;
            Neuron parent1Neuron = parent1.getNeuronAt(i);
            Neuron parent2Neuron = parent2.getNeuronAt(i);

            float bias1 = parent1Neuron.bias;
            parent1Neuron.bias = parent2Neuron.bias;
            parent2Neuron.bias = bias1;
        }

        return (int) (Math.random() * 2) == 1 ? new Bird(parent1) : new Bird(parent2);
    }

    private ArrayList<Bird> get4FirstBirds() {
        ArrayList<Bird> arr = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            Bird top4Bird = mlBirdsArray.get(i);
            top4Bird.resetPosition();
            arr.add(top4Bird);
        }
        return arr;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }
}
