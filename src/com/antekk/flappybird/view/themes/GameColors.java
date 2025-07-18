package com.antekk.flappybird.view.themes;

import com.antekk.flappybird.view.ErrorDialog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameColors {
    public static Color backgroundColor;
    public static Color foregroundColor;
    public static Color groundColor;
    public static Color shapeBorderColor;
    public static Color borderColor;
    private static boolean areDefaultSpritesInitialized = false;

    public static BufferedImage birdMidFlap;
    public static BufferedImage birdDownFlap;
    public static BufferedImage birdUpFlap;
    public static BufferedImage background;
    public static BufferedImage ground;
    public static BufferedImage pipe;
    public static BufferedImage pipeEnd;
    public static BufferedImage startingMessage;
    public static BufferedImage gameOver;
    public static ArrayList<BufferedImage> numbers = new ArrayList<>();
    private static final ClassLoader classLoader = GameColors.class.getClassLoader();

    private static InputStream getResource(String resource) {
        return classLoader.getResourceAsStream(resource);
    }

    
    private static void setDarkThemeValues() {
        backgroundColor = new Color(0, 135, 147);
        foregroundColor = new Color(238, 238, 238);
        groundColor = new Color(0, 135, 147);
        shapeBorderColor = Color.BLACK;
        borderColor = new Color(202, 202, 170);

        try {
            background = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/background-night.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDefaultSprites() throws IOException {
        birdMidFlap = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/yellowbird-midflap.png"));
        birdDownFlap = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/yellowbird-downflap.png"));
        birdUpFlap = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/yellowbird-upflap.png"));
        ground = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/base.png"));
        pipe = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/pipe-green1.png"));
        pipeEnd = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/pipe-green2.png"));
        startingMessage = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/message.png"));
        gameOver = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/gameover.png"));

        numbers.clear();
        for(int i = 0; i <= 9; i++) {
            numbers.add(ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/numbers/" + i + ".png")));
        }
        areDefaultSpritesInitialized = true;
    }

    private static void setLightThemeValues() {
        backgroundColor = new Color(78, 192, 202);
        foregroundColor = new Color(28, 28, 28);
        groundColor = new Color(78, 192, 202);
        shapeBorderColor = Color.BLACK;
        borderColor = new Color(202, 202, 170);

        try {
            background = ImageIO.read(getResource("com/antekk/flappybird/assets/sprites/background-day.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTheme(Theme theme) {
        if(!areDefaultSpritesInitialized) {
            try {
                setDefaultSprites();
            } catch (IOException e) {
                new ErrorDialog("Cannot load game sprites - missing assets.", e);
            }
        }

        if(theme == Theme.DAY) {
            setLightThemeValues();
        } else if(theme == Theme.NIGHT) {
            setDarkThemeValues();
        }
    }
}
