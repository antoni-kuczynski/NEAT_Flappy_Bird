package com.antekk.flappybird.game;

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

public final class ConfigJSON {
    private static final File file = new File("tetris_config.json");
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
//        TetrisPlayer.defaultGameLevel = object.getInt("starting_level") - 1;
        Theme theme;
        try {
            theme = Theme.valueOf(object.getString("theme"));
        } catch (IllegalArgumentException e) {
            new ErrorDialog("Invalid theme value in config!", e);
            return;
        }

        GameColors.setTheme(theme);
    }

    public static void saveValues(int level, Theme theme, int blockSize) {
        object.put("starting_level", level);
        object.put("theme", theme);
        object.put("block_size", blockSize);
        writeToFile();
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
            object = new JSONObject();
            object.put("starting_level", 1);
            object.put("theme", "LIGHT");
            object.put("block_size", 30);
            writeToFile();
        }
    }


    private static void writeToFile() {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(object.toString(4));
        } catch (IOException e) {
            new ErrorDialog("Cannot write new values to config!", e);
        }
    }

    public static int getLevel() {
        return object.getInt("starting_level");
    }

    public static int getBlockSize() {
        return object.getInt("block_size");
    }

    public static Theme getTheme() {
        return Theme.valueOf(object.getString("theme"));
    }
}
