package com.antekk.flappybird.game.keybinds;

import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameKeybinds {

    public static void setupKeyBindings(InputMap inputMap, ActionMap actionMap, GamePanel gamePanel) {
        if (gamePanel == null || inputMap == null || actionMap == null) {
            throw new RuntimeException("a value in keybinds is null");
        }

        GameKeybind.currentPanel = gamePanel;
        GameKeybind.inputMap = inputMap;
        GameKeybind.actionMap = actionMap;

        new GameKeybind("HARD_DROP", KeyEvent.VK_SPACE,
                () -> gamePanel.getBird().flap()
        ).bindKeyPressed();

        new GameKeybind("ROTATE_RIGHT", KeyEvent.VK_UP,
                () ->gamePanel.getBird().flap()
        ).bindKeyPressed();

        new GameKeybind("PAUSE_GAME_PRESSED", KeyEvent.VK_ESCAPE,
            () -> gamePanel.getGameLoop().pauseAndUnpauseGame()
        ).bindKeyPressed();
    }
}

