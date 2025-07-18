package com.antekk.flappybird.game;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.bird.gamemodes.GameMode;
import com.antekk.flappybird.game.bird.gamemodes.PlayerMode;
import com.antekk.flappybird.game.bird.gamemodes.MlPretrainedMode;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.ErrorDialog;
import com.antekk.flappybird.view.GamePanel;
import com.antekk.flappybird.view.themes.GameColors;
import com.antekk.flappybird.view.themes.Theme;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public final class ConfigJSON {
    private static final File file = new File("flappy_bird_config.json");
    private static JSONObject object;

    public static void initializeValuesFromConfigFile() {
        if(object == null) {
            try {
                initialize();
            } catch (IOException e) {
                new ErrorDialog("Cannot initialize the config file", e);
            }
        }

        GamePanel.setBlockSizePx(object.getInt("block_size"));
        Theme theme;
        try {
            theme = Theme.valueOf(object.getString("theme"));
        } catch (IllegalArgumentException e) {
//            throw new RuntimeException(e);
            theme = Theme.values()[0];
        }

        GameColors.setTheme(theme);
        PipeFormation.futureGap = object.getInt("vertical_pipes_gap");
        PipeFormation.updatePipeGap();

    }

    public static void saveValues(int pipesVGap, Theme theme, int blockSize, boolean showNewBestDialog,
                                  GameMode gameMode, String pretrainedJSON) {
        object.put("vertical_pipes_gap", pipesVGap);
        object.put("theme", theme);
        object.put("block_size", blockSize);
        object.put("show_new_best_dialog", showNewBestDialog);
        object.put("game_mode", gameMode.toString());
        object.put("pretrained_json_file", pretrainedJSON);
        writeToFile();
    }

    private static void generateNewJsonObject() {
        object = new JSONObject();
        object.put("vertical_pipes_gap", 3 * getBlockSizePx());
        object.put("theme", "DAY");
        object.put("block_size", 50);
        object.put("show_new_best_dialog", true);
        object.put("game_mode", new PlayerMode().toString());
        object.put("pretrained_json_file", "");
    }

    private static void initialize() throws IOException {
        if(!file.exists() && !file.createNewFile()) {
            throw new FileSystemException("Cannot create " + file.getAbsolutePath() + " file.");
        }

        StringBuilder jsonText = new StringBuilder();
        for(String s : Files.readAllLines(file.toPath())) {
            jsonText.append(s).append("\n");
        }

        try {
            object = new JSONObject(jsonText.toString());
        } catch (JSONException e) {
            generateNewJsonObject();
            writeToFile();
        }
        if(!object.has("vertical_pipes_gap") || !object.has("theme") || !object.has("block_size")
            || !object.has("show_new_best_dialog") || !object.has("game_mode") || !object.has("pretrained_json_file")) {
            generateNewJsonObject();
        }
    }


    private static void writeToFile() {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(object.toString(4));
        } catch (IOException e) {
            new ErrorDialog("Cannot write new values to config!", e);
        }
    }

    public static int getPipesVGap() {
        return object.getInt("vertical_pipes_gap");
    }

    public static int getBlockSize() {
        return object.getInt("block_size");
    }

    public static Theme getTheme() {
        String theme;
        try {
            theme = object.getString("theme");
            return Theme.valueOf(theme);
        } catch (Exception e) {
            return Theme.values()[0];
        }
    }

    public static boolean showNewBestDialog() {
        return object.getBoolean("show_new_best_dialog");
    }

    public static String getPretrainedJSONFilePath() {
        return object.getString("pretrained_json_file");
    }

    public static GameMode getGameMode() {
        GameMode gameMode = GameMode.valueOf(object.getString("game_mode"));

        if(gameMode.isPretrainedMode() && !getPretrainedJSONFilePath().isBlank()) {
            ((MlPretrainedMode) gameMode).setBirdsNeuralNetwork(NeuralNetwork.getFromJSON(getPretrainedJSONFilePath()));
        }
        return gameMode;
    }
}

