package com.antekk.flappybird.view.themes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameColors {
    public static Color boardColor;
    public static Color backgroundColor;
    public static Color foregroundColor;
    public static Color borderColor;
    public static Color shapeBorderColor;

    public static BufferedImage birdMidFlap;
    public static BufferedImage birdDownFlap;
    public static BufferedImage birdUpFlap;
    public static BufferedImage background;
    public static BufferedImage ground;
    public static BufferedImage pipe;
    public static BufferedImage pipeEnd;


    private static void setDarkThemeValues() {
        boardColor = new Color(49, 54, 63);
        backgroundColor = new Color(34, 40, 49);
        foregroundColor = new Color(238, 238, 238);
        borderColor = new Color(34, 40, 49);
        shapeBorderColor = Color.BLACK;

        try {
            birdMidFlap = ImageIO.read(new File("assets/sprites/yellowbird-midflap.png"));
            birdDownFlap = ImageIO.read(new File("assets/sprites/yellowbird-downflap.png"));
            birdUpFlap = ImageIO.read(new File("assets/sprites/yellowbird-upflap.png"));
            background = ImageIO.read(new File("assets/sprites/background-night.png"));
            ground = ImageIO.read(new File("assets/sprites/base.png"));
            pipe = ImageIO.read(new File("assets/sprites/pipe-green1.png"));
            pipeEnd = ImageIO.read(new File("assets/sprites/pipe-green2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setLightThemeValues() {
        boardColor = new Color(147, 197, 243);
        backgroundColor = new Color(238,238,238);
        foregroundColor = new Color(28, 28, 28);
        borderColor = new Color(28, 28, 28);
        shapeBorderColor = Color.BLACK;

        try {
            birdMidFlap = ImageIO.read(new File("assets/sprites/yellowbird-midflap.png"));
            birdDownFlap = ImageIO.read(new File("assets/sprites/yellowbird-downflap.png"));
            birdUpFlap = ImageIO.read(new File("assets/sprites/yellowbird-upflap.png"));
            background = ImageIO.read(new File("assets/sprites/background-day.png"));
            ground = ImageIO.read(new File("assets/sprites/base.png"));
            pipe = ImageIO.read(new File("assets/sprites/pipe-green1.png"));
            pipeEnd = ImageIO.read(new File("assets/sprites/pipe-green2.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTheme(Theme theme) {
        if(theme == Theme.LIGHT) {
            setLightThemeValues();
        } else if(theme == Theme.DARK) {
            setDarkThemeValues();
        }
    }
}
