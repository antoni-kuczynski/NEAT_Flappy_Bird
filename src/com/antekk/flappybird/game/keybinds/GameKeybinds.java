package com.antekk.flappybird.game.keybinds;

import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameKeybinds extends MouseAdapter {
    private GamePanel gamePanel;

    public GameKeybinds(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setupKeyBindings(InputMap inputMap, ActionMap actionMap) {
        if (gamePanel == null || inputMap == null || actionMap == null) {
            throw new RuntimeException("a value in keybinds is null");
        }

        GameKeybind.currentPanel = gamePanel;
        GameKeybind.inputMap = inputMap;
        GameKeybind.actionMap = actionMap;

        new GameKeybind("FLAP_PRIMARY", KeyEvent.VK_SPACE,
                () -> gamePanel.getBirds().getDefault().flap()
        ).bindKeyPressed();

        new GameKeybind("FLAP_SECONDARY", KeyEvent.VK_UP,
                () ->gamePanel.getBirds().getDefault().flap()
        ).bindKeyPressed();

        new GameKeybind("PAUSE_GAME_PRESSED", KeyEvent.VK_ESCAPE,
            () -> gamePanel.getGameLoop().pauseAndUnpauseGame()
        ).bindKeyPressed();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if(gamePanel.getGameLoop().getGameState() == GameState.STARTING)
            gamePanel.getGameLoop().startGame();
        gamePanel.getBirds().getDefault().flap();
    }
}

