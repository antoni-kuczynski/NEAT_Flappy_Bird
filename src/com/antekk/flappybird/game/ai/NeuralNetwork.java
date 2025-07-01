package com.antekk.flappybird.game.ai;

import java.util.ArrayList;

public class NeuralNetwork {
    private final ArrayList<Neuron> inputs = new ArrayList<>();
    private final ArrayList<Neuron> hidden = new ArrayList<>();
    private final Neuron output;


    public NeuralNetwork() {
        inputs.add(new Neuron());
        inputs.add(new Neuron());


        for(int i = 0; i < 6; i++) {
            hidden.add(new Neuron(inputs.get(0), inputs.get(1)));
        }

        output = new Neuron();

        for(Neuron neuron : hidden)
            output.addConnection(neuron);
    }

    public double predict(int input1, int input2) {
        double z = 0;
        for(Neuron hiddenNeuron : hidden) {
            double in1 = hiddenNeuron.compute(input1, inputs.get(0));
            double in2 = hiddenNeuron.compute(input2, inputs.get(1));

//            System.out.println("\tTotal for Neuron " + hiddenNeuron.getId() + " = " + (in1 + in2));
//            System.out.println("\tComputed output for Neuron " + hiddenNeuron.getId() + " = " + output.compute(in1 + in2, hiddenNeuron));
            z += output.compute(in1 + in2, hiddenNeuron);
        }

        double prediction = sigmoid(z);
        System.out.println("\tPrediction = " + prediction);
        return prediction;
    }

    private static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append("Input neurons:").append("\n\t");
        for(Neuron neuron : inputs)
            s.append(neuron.toString()).append("\n\t");
        s.append("\rHidden neurons:").append("\n\t");
        for(Neuron neuron : hidden)
            s.append(neuron.toString()).append("\n\t");

        s.append("\rOutput neuron:").append("\n\t");
        s.append(output.toString());
        return s.toString();
    }
}