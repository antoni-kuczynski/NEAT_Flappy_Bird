package com.antekk.flappybird.view.themes;

import java.awt.*;

public class GameColors {
    public static Color boardColor;
    public static Color backgroundColor;
    public static Color foregroundColor;
    public static Color borderColor;
    public static Color shapeBorderColor;


    private static void setDarkThemeValues() {
        boardColor = new Color(49, 54, 63);
        backgroundColor = new Color(34, 40, 49);
        foregroundColor = new Color(238, 238, 238);
        borderColor = new Color(34, 40, 49);
        shapeBorderColor = Color.BLACK;
    }

    private static void setLightThemeValues() {
        boardColor = new Color(250, 250, 255);
        backgroundColor = new Color(238,238,238);
        foregroundColor = new Color(28, 28, 28);
        borderColor = new Color(28, 28, 28);
        shapeBorderColor = Color.BLACK;
    }

    public static void setTheme(Theme theme) {
        if(theme == Theme.LIGHT) {
            setLightThemeValues();
        } else if(theme == Theme.DARK) {
            setDarkThemeValues();
        }
    }
}
