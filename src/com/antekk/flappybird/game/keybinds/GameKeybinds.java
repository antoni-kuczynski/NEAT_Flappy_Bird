package com.antekk.flappybird.game.keybinds;

import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameKeybinds extends MouseAdapter {
    private GameLoop gameLoop;

    public GameKeybinds(GamePanel gamePanel) {
        this.gameLoop = gamePanel.getGameLoop();
        GameKeybind.currentPanel = gamePanel;
    }

    public void setupKeyBindings(InputMap inputMap, ActionMap actionMap) {
        if (gameLoop == null || inputMap == null || actionMap == null) {
            throw new RuntimeException("a value in keybinds is null");
        }

        GameKeybind.inputMap = inputMap;
        GameKeybind.actionMap = actionMap;

        new GameKeybind("FLAP_PRIMARY", KeyEvent.VK_SPACE,
                () -> gameLoop.getPlayerControlledBird().flap()
        ).bindKeyPressed();

        new GameKeybind("FLAP_SECONDARY", KeyEvent.VK_UP,
                () -> gameLoop.getPlayerControlledBird().flap()
        ).bindKeyPressed();

        new GameKeybind("PAUSE_GAME_PRESSED", KeyEvent.VK_ESCAPE,
            () -> gameLoop.pauseAndUnpauseGame()
        ).bindKeyPressed();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        if(gameLoop.getGameState() == GameState.STARTING)
            gameLoop.startGame();

        if(gameLoop.getPlayerControlledBird() != null)
            gameLoop.getPlayerControlledBird().flap();
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }
}

