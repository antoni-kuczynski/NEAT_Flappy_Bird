package com.antekk.flappybird.game.ai;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class Neuron implements Cloneable{
    public float bias;
    private static final Random random = new Random();
    private int id;
    private ArrayList<Float> weights = new ArrayList<>();

    Neuron(int amountOfWeights, int id) {
        this.id = id;
        bias = random.nextFloat(-1,1);
        for(int i = 0; i < amountOfWeights; i++) weights.add(random.nextFloat(-1,1));
    }

    private Neuron(int id, float bias, ArrayList<Float> weights) {
        this.bias = bias;
        this.id = id;
        this.weights = weights;
    }

    protected double compute(double... input) {
        double z = 0;
        for(int i = 0; i < input.length; i++) {
            z += input[i] * weights.get(i);
        }
        z += bias;

        return reLU(z);
    }

    private static float reLU(double x) {
        return (float) ((x + Math.abs(x)) / 2);
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

    public ArrayList<Float> getWeights() {
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
        object.put("amount_of_connections", weights.size());

        return object;
    }

    protected static Neuron getFromJSON(JSONObject object) {
        int id = object.getInt("id");
        int amountOfConnections = object.getInt("amount_of_connections");
        ArrayList<Float> weights = new ArrayList<>();
        float bias = object.getFloat("bias");

        JSONObject weightsJSON = object.getJSONObject("weights");
        for(int i = 0; i < amountOfConnections; i++) {
            String key = String.valueOf(i);
            weights.add(weightsJSON.getFloat(key));
        }

        return new Neuron(id, bias, weights);
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