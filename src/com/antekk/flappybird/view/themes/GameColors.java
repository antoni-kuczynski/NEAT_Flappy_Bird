package com.antekk.flappybird.view.themes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameColors {
    public static Color backgroundColor;
    public static Color foregroundColor;
    public static Color groundColor;
    public static Color shapeBorderColor;

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


    private static void setDarkThemeValues() {
        backgroundColor = new Color(0, 135, 147);
        foregroundColor = new Color(238, 238, 238);
        groundColor = new Color(0, 135, 147);
        shapeBorderColor = Color.BLACK;

        try {
            background = ImageIO.read(new File("assets/sprites/background-night.png"));
            setDefaultSprites();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setDefaultSprites() throws IOException {
        birdMidFlap = ImageIO.read(new File("assets/sprites/yellowbird-midflap.png"));
        birdDownFlap = ImageIO.read(new File("assets/sprites/yellowbird-downflap.png"));
        birdUpFlap = ImageIO.read(new File("assets/sprites/yellowbird-upflap.png"));
        ground = ImageIO.read(new File("assets/sprites/base.png"));
        pipe = ImageIO.read(new File("assets/sprites/pipe-green1.png"));
        pipeEnd = ImageIO.read(new File("assets/sprites/pipe-green2.png"));
        startingMessage = ImageIO.read(new File("assets/sprites/message.png"));
        gameOver = ImageIO.read(new File("assets/sprites/gameover.png"));

        numbers.clear();
        for(int i = 0; i <= 9; i++) {
            numbers.add(ImageIO.read(new File("assets/sprites/numbers/" + i + ".png")));
        }
    }

    private static void setLightThemeValues() {
        backgroundColor = new Color(78, 192, 202);
        foregroundColor = new Color(28, 28, 28);
        groundColor = new Color(78, 192, 202);
        shapeBorderColor = Color.BLACK;

        try {
            background = ImageIO.read(new File("assets/sprites/background-day.png"));
            setDefaultSprites();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTheme(Theme theme) {
        if(theme == Theme.DAY) {
            setLightThemeValues();
        } else if(theme == Theme.NIGHT) {
            setDarkThemeValues();
        }
    }
}
