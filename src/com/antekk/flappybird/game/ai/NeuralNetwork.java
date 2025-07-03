package com.antekk.flappybird.game.ai;

import java.util.ArrayList;
import java.util.Iterator;

public class NeuralNetwork implements Iterable<Neuron>, Cloneable {
    private ArrayList<Neuron> inputs = new ArrayList<>();
    private ArrayList<Neuron> hidden = new ArrayList<>();
    private Neuron output;
    private final int totalSize;
    public long fitnessTotalDistance = 0;

    public NeuralNetwork() {
        inputs.add(new Neuron(1));
        inputs.add(new Neuron(1));


        for(int i = 0; i < 6; i++) {
            hidden.add(new Neuron(2));
        }

        output = new Neuron(6);

        totalSize = inputs.size() + hidden.size() + 1;
    }

    public double predict(int input1, int input2) {
        return output.compute(
                hidden.get(0).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                ),
                hidden.get(1).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                ),
                hidden.get(2).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                ),
                hidden.get(3).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                ),
                hidden.get(4).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                ),
                hidden.get(5).compute(
                        inputs.get(0).compute(input1),
                        inputs.get(1).compute(input2)
                )
        );
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

    @Override
    public Iterator<Neuron> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < totalSize;
            }

            @Override
            public Neuron next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }

                Neuron neuron = getNeuronAt(index);

                index++;
                return neuron;
            }
        };
    }

    public void mutate() {
        double mutationFactor = Math.random() * 3;
        for(Neuron neuron : this) {
            neuron.bias *= mutationFactor;
//            for(int i = 0; i < neuron.getWeights().size(); i++)
//                neuron.getWeights().set(i, neuron.getWeights().get(i) * mutationFactor);
        }
    }

    public Neuron getNeuronAt(int index) {
        Neuron neuron;
        if (index < inputs.size()) {
            neuron = inputs.get(index);
        } else if (index < inputs.size() + hidden.size()) {
            neuron = hidden.get(index - inputs.size());
        } else {
            neuron = output;
        }
        return neuron;
    }

    public int size() {
        return totalSize;
    }

    @Override
    public NeuralNetwork clone() {
        try {
            NeuralNetwork clone = (NeuralNetwork) super.clone();
            clone.inputs = new ArrayList<>();
            clone.hidden = new ArrayList<>();

            for(Neuron neuron : this.inputs) {
                clone.inputs.add(neuron.clone());
            }

            for(Neuron neuron : this.hidden) {
                clone.hidden.add(neuron.clone());
            }

            clone.output = this.output.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}