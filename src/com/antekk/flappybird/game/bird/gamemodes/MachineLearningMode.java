package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.ai.Neuron;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class MachineLearningMode implements GameMode {
    private int badPopulationStreak = 0;
    private int generationNumber = 1;
    protected int populationSize = 0;

    @Override
    public synchronized void draw(Graphics g, Birds birds) {
        for(Iterator<Bird> it = birds.iterator(); it.hasNext();) it.next().draw(g);
    }

    @Override
    public void drawWithoutRotation(Graphics g, Birds birds) {
        for(Bird bird : birds) bird.drawWithoutRotation(g);
    }

    @Override
    public void resetPosition(Birds birds) {
        for(Bird bird : birds) bird.resetPosition();
    }

    @Override
    public void flap(Birds birds) {
        for(Bird bird : birds) bird.flap();
    }

    @Override
    public boolean isBetweenPipes(PipeFormation pipeFormation, Birds birds) {
        for(Bird bird : birds) {
            if(bird.isBetweenPipes(pipeFormation)) return true;
        }
        return false;
    }

    @Override
    public boolean areAllBirdsDead(Birds birds) {
        for(Bird bird : birds.mlBirdsArray) {
            if(bird.isAlive)
                return false;
        }
        return true;
    }

    @Override
    public Iterator<Bird> iterator(Birds birds) {
        return birds.mlBirdsArray.iterator();
    }

    @Override
    public void init(Birds birds) {
        this.populationSize = 10;
        birds.mlBirdsArray = new ArrayList<>();
        for(int i = 0; i < populationSize; i++) birds.mlBirdsArray.add(new Bird(new NeuralNetwork()));
        birds.playerControlledBird = null;
    }

    @Override
    public boolean usesMachineLearning() {
        return true;
    }

    @Override
    public String toString() {
        return "ML_MODE";
    }

    @Override
    public int size(Birds birds) {
        return birds.mlBirdsArray.size();
    }

    @Override
    public Bird getBirdAt(int index, Birds birds) {
        return birds.mlBirdsArray.get(index);
    }

    private int birdsFitnessSortComparator(Bird b1, Bird b2) {
        if(b1.getFitness() > b2.getFitness())
            return -1;
        else if(b1.getFitness() == b2.getFitness())
            return 0;
        else
            return 1;
    }

    public void newPopulation(Birds birds) {
        ArrayList<Bird> newPopulation = new ArrayList<>();
        birds.mlBirdsArray.sort(this::birdsFitnessSortComparator);

        if(birds.mlBirdsArray.get(0).getFitness() <= 0)
            badPopulationStreak++;

        if(badPopulationStreak >= 2) {
            System.out.println("more than 2 bad populations, resetting...");
            for(int i = 0; i < birds.mlBirdsArray.size(); i++) {
                newPopulation.add(new Bird(new NeuralNetwork()));
            }
            badPopulationStreak = 0;
            return;
        }

        ArrayList<Bird> top4 = get4BestBirds(birds);
        for(Bird bird : top4) {
            newPopulation.add(new Bird(bird.brain.clone()));
        }

        newPopulation.add(createOffSpring(top4.get(0), top4.get(1)));

        newPopulation.add(createOffSpring(top4.get((int) (Math.random() * 4)), top4.get((int) (Math.random() * 4))));
        newPopulation.add(createOffSpring(top4.get((int) (Math.random() * 4)), top4.get((int) (Math.random() * 4))));
        newPopulation.add(createOffSpring(top4.get((int) (Math.random() * 4)), top4.get((int) (Math.random() * 4))));


        newPopulation.add(new Bird(top4.get((int) (Math.random() * 4)).brain.clone()));
        newPopulation.add(new Bird(top4.get((int) (Math.random() * 4)).brain.clone()));

        for(int i = 4; i < newPopulation.size(); i++) {
            newPopulation.get(i).brain.mutate();
        }

        birds.mlBirdsArray = newPopulation;

        for(Bird bird : birds.mlBirdsArray)
            bird.resetPosition();
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

            double bias1 = parent1Neuron.bias;
            parent1Neuron.bias = parent2Neuron.bias;
            parent2Neuron.bias = bias1;
        }

        return (int) (Math.random() * 2) == 1 ? new Bird(parent1) : new Bird(parent2);
    }

    private ArrayList<Bird> get4BestBirds(Birds birds) {
        ArrayList<Bird> arr = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            Bird top4Bird = birds.mlBirdsArray.get(i);
            top4Bird.resetPosition();
            arr.add(top4Bird);
        }
        return arr;
    }

    protected int getGenerationNumber() {
        return generationNumber;
    }
}
