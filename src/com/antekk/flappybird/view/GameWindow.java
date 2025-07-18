package com.antekk.flappybird.view;

import com.antekk.flappybird.view.themes.GameColors;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        FlatDarculaLaf.setup();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Flappy Bird");
        GamePanel panel = new GamePanel(this);
        this.add(panel);
        this.setIconImage(GameColors.birdMidFlap);
        this.setVisible(true);
        this.pack();
    }
}
