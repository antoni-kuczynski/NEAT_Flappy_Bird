package com.antekk.flappybird.game.ai;

import java.util.LinkedHashMap;
import java.util.Random;

class Neuron {
    public double bias;
    private final LinkedHashMap<Neuron, Double> incomingConnections = new LinkedHashMap<>();
    private final Random random = new Random();
    private static int number = 0;
    private int id;

    Neuron(Neuron... connections) {
        for(Neuron neuron : connections) {
            incomingConnections.putIfAbsent(neuron, random.nextDouble(-1,1));
        }
        id = number;
        number++;
    }

    protected void addConnection(Neuron neuron) {
        incomingConnections.putIfAbsent(neuron, random.nextDouble(-1,1));
    }

    protected double compute(double input, Neuron neuron) {
        double z = 0;
        z += input * incomingConnections.get(neuron);

        System.out.println("Neuron " + id + " = " + z);
        return z;
    }

    @Override
    public String toString() {
        if(incomingConnections.isEmpty())
            return "Neuron " + id + " { " + 0 + " connections" + ", bias = " + bias + "}";

        StringBuilder s = new StringBuilder();
        s.append("{");
        for(Neuron neuron : incomingConnections.keySet()) {
            String neuronString = "Neuron " + neuron.id + " with weight = " + incomingConnections.get(neuron);
            s.append(neuronString).append(", ");
        }
        s.append("}");

        return "Neuron " + id + " { " + incomingConnections.size() + " connections = " + s +  ", bias = " + bias + "}";
    }

    public int getId() {
        return id;
    }
}