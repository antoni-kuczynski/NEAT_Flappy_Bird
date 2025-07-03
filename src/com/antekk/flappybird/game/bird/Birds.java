package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.ai.Neuron;
import com.antekk.flappybird.game.pipes.PipeFormation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Birds implements PlayerBird, Iterable<Bird> {
    private ArrayList<Bird> birds = new ArrayList<>();
    private int badPopulationStreak = 0;
    private int generationNumber = 1;

    public Birds(int amount) {
        for(int i = 0; i < amount; i++) birds.add(new Bird(new NeuralNetwork()));
    }

    @Override
    public void draw(Graphics g) {
        for(Bird bird : birds) bird.draw(g);
    }

    @Override
    public void drawWithoutRotation(Graphics g) {
        for(Bird bird : birds) bird.drawWithoutRotation(g);
    }

    @Override
    public void resetPosition() {
        for(Bird bird : birds) bird.resetPosition();
    }

    @Override
    public void flap() {
        for(Bird bird : birds) bird.flap();
    }

    @Override
    public Iterator<Bird> iterator() {
        return birds.iterator();
    }

    private int birdsFitnessSortComparator(Bird b1, Bird b2) {
        if(b1.getFitness() > b2.getFitness())
            return -1;
        else if(b1.getFitness() == b2.getFitness())
            return 0;
        else
            return 1;
    }

    public boolean areAllBirdsDead() {
        for(Bird bird : this) {
            if(bird.isAlive)
                return false;
        }
        return true;
    }

    @Override
    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        for(Bird bird : this) {
            if(bird.isBetweenPipes(pipeFormation)) return true;
        }
        return false;
    }

    public void newPopulation() {
        ArrayList<Bird> newPopulation = new ArrayList<>();
        birds.sort(this::birdsFitnessSortComparator);

        if(birds.get(0).getFitness() <= 0)
            badPopulationStreak++;

        if(badPopulationStreak >= 2) {
            System.out.println("more than 2 bad populations, resetting...");
            for(int i = 0; i < birds.size(); i++) {
                newPopulation.add(new Bird(new NeuralNetwork()));
            }
            badPopulationStreak = 0;
            return;
        }

        ArrayList<Bird> top4 = get4BestBirds();
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

        birds = newPopulation;

        for(Bird bird : this)
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

    private ArrayList<Bird> get4BestBirds() {
        ArrayList<Bird> arr = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            Bird top4Bird = birds.get(i);
            top4Bird.resetPosition();
            arr.add(top4Bird);
        }
        return arr;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }

    public Bird getDefault() {
        return birds.get(0);
    }

    public Bird get(int index) {
        return birds.get(index);
    }

    public int size() {
        return birds.size();
    }
}
