package com.antekk.flappybird.game.ai;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Neuron implements Cloneable{
    public double bias;
    private final Random random = new Random();
    private int id;
    private ArrayList<Double> weights = new ArrayList<>();

    Neuron(int amountOfWeights, int id) {
        this.id = id;
        bias = random.nextDouble(-1,1);
        for(int i = 0; i < amountOfWeights; i++) weights.add(random.nextDouble(-1,1));
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

    protected int getId() {
        return id;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    protected JSONObject getJSONObject() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("bias", bias);

        JSONObject weightsObject = new JSONObject();
        for(int i = 0; i < weights.size(); i++) {
            weightsObject.put(String.valueOf(i), weights.get(i));
        }
        object.put("weights", weightsObject);

        return object;
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