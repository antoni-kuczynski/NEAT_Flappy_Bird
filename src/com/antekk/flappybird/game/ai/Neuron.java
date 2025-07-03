package com.antekk.flappybird.game.ai;

import java.util.ArrayList;
import java.util.Random;

public class Neuron implements Cloneable{
    public double bias;
    private final Random random = new Random();
    private static int number = 0;
    private int id;
    private ArrayList<Double> weights = new ArrayList<>();

    Neuron(int amountOfWeights) {
        bias = random.nextDouble(-1,1);
        for(int i = 0; i < amountOfWeights; i++) weights.add(random.nextDouble(-1,1));
        id = number;
        number++;
    }

    protected double compute(double... input) {
        double z = 0;
        for(int i = 0; i < input.length; i++) {
            z += input[i] * weights.get(i);
        }
        z += bias;

        return sigmoid(z);
    }

    private static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public String toString() {
        return "Neuron{" +
                "bias=" + bias +
                ", id=" + id +
                ", weights=" + weights +
                '}';
    }

    public int getId() {
        return id;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    @Override
    public Neuron clone() {
        try {
            Neuron clone = (Neuron) super.clone();
            clone.weights = new ArrayList<>();
            clone.weights.addAll(weights);
            clone.bias = bias;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}