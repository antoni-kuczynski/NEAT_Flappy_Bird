package com.antekk.flappybird.game.ai;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.view.ErrorDialog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class NeuralNetwork implements Iterable<Neuron>, Cloneable {
    private ArrayList<Neuron> inputs = new ArrayList<>();
    private ArrayList<Neuron> hidden = new ArrayList<>();
    private Neuron output;
    private final int totalSize;
    public long fitnessTotalDistance = 0;
    private Bird owner;
    private static final Random random = new Random();
    private int maxAchievedScore = -1;
    private int pipesVGapNetworkPlayedOn = -1;

    public NeuralNetwork() {
        inputs.add(new Neuron(1,0));
        inputs.add(new Neuron(1,1));


        for(int i = 0; i < 6; i++) {
            hidden.add(new Neuron(2, 2 + i));
        }

        output = new Neuron(6, 8);

        totalSize = inputs.size() + hidden.size() + 1;
    }

    private NeuralNetwork(ArrayList<Neuron> inputs, ArrayList<Neuron> hidden, Neuron output) {
        this.inputs = inputs;
        this.hidden = hidden;
        this.output = output;
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

    public void performSwapMutation(float swapProbability) {
        for(int i = 0; i < this.size(); i++) {
            if(random.nextFloat() <= swapProbability)
                continue;

            int randomIndex = (int) (Math.random() * this.size());
            float biasI = getNeuronAt(i).bias;
            getNeuronAt(i).bias = getNeuronAt(randomIndex).bias;
            getNeuronAt(randomIndex).bias = biasI;
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

    public void saveToJSON(String filename) {
        JSONObject object = new JSONObject();
        JSONObject layers = new JSONObject();

        layers.put("input", getLayer(inputs));
        layers.put("hidden", getLayer(hidden));
        layers.put("output", getLayer(new ArrayList<>(Arrays.asList(output))));

        object.put("layers", layers);
        object.put("total_size", totalSize);
        object.put("fitness", fitnessTotalDistance);

        JSONObject gameParams = new JSONObject();
        gameParams.put("pipe_gap", owner.getPlayer().pipesVerticalGap);
        gameParams.put("achieved_score", owner.getPlayer().score);

        object.put("game_params", gameParams);

        try(FileWriter writer = new FileWriter(filename)) {
            writer.write(object.toString(4));
        } catch (IOException e) {
            new ErrorDialog("Cannot save the neural network to json", e);
        }
    }

    public static NeuralNetwork getFromJSON(String jsonPath) {
        StringBuilder builder = new StringBuilder();

        ArrayList<String> lines;
        try {
            lines = (ArrayList<String>) Files.readAllLines(Path.of(jsonPath));
        } catch (IOException e) {
            new ErrorDialog("Cannot read neural network json file at \"" + jsonPath + "\" - I/O error", e);
            return null;
        }

        for(String s : lines) {
            builder.append(s).append("\n");
        }

        JSONObject object = new JSONObject(builder.toString());
        ArrayList<Neuron> loadedInputs = new ArrayList<>();
        ArrayList<Neuron> loadedHidden = new ArrayList<>();
        Neuron loadedOutput;

        JSONObject layers = object.getJSONObject("layers");
        JSONArray inputLayer = layers.getJSONArray("input");
        JSONArray hiddenLayer = layers.getJSONArray("hidden");
        JSONArray outputLayer = layers.getJSONArray("output");


        for(int i = 0; i < inputLayer.length(); i++) {
            loadedInputs.add(Neuron.getFromJSON(inputLayer.getJSONObject(i)));
        }

        for(int i = 0; i < hiddenLayer.length(); i++) {
            loadedHidden.add(Neuron.getFromJSON(hiddenLayer.getJSONObject(i)));
        }

        loadedOutput = Neuron.getFromJSON(outputLayer.getJSONObject(0));

        loadedInputs.sort(Comparator.comparingInt(Neuron::getId));
        loadedHidden.sort(Comparator.comparingInt(Neuron::getId));

        JSONObject gameParams = object.getJSONObject("game_params");
        NeuralNetwork network = new NeuralNetwork(loadedInputs, loadedHidden, loadedOutput);
        network.maxAchievedScore = gameParams.getInt("achieved_score");
        network.pipesVGapNetworkPlayedOn = gameParams.getInt("pipe_gap");
        return network;
    }

    private JSONArray getLayer(ArrayList<Neuron> layer) {
        JSONArray neuronLayer = new JSONArray();
        for(Neuron n : layer) {
            neuronLayer.put(n.getJSONObject());
        }
        return neuronLayer;
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

    public void setOwner(Bird owner) {
        this.owner = owner;
    }

    public int getMaxAchievedScore() {
        return maxAchievedScore;
    }

    public int getPipesVGapNetworkPlayedOn() {
        return pipesVGapNetworkPlayedOn;
    }
}