package com.antekk.flappybird;

import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.view.GameWindow;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameWindow::new);
    }


}