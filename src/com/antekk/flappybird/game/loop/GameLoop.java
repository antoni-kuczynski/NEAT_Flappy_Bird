package com.antekk.flappybird.game.loop;

import com.antekk.flappybird.view.ErrorDialog;
import com.antekk.flappybird.view.GamePanel;

import static com.antekk.flappybird.view.GamePanel.TOP;

public class GameLoop extends Thread {
    private final GamePanel currentPanel;
    private GameState gameState;

    private final int timeBetweenFramesMillis = 1000 / 60;

    private void gameLoop() throws InterruptedException {
        while (gameState != GameState.LOST) {

            Thread.sleep(timeBetweenFramesMillis); //this sucks, but uses less cpu than time ms tracking

            if(gameState == GameState.PAUSED)
                continue;

            if(true) {

                currentPanel.paintImmediately(0, TOP, currentPanel.getWidth(), currentPanel.getHeight());
            }

            gameState = updateGameState();
        }
    }

    private GameState updateGameState() {
        if(gameState == GameState.PAUSED) {
            return GameState.PAUSED;
        }

        return GameState.RUNNING;
    }

    public void pauseAndUnpauseGame() {
        if(gameState == GameState.LOST)
            return;

        if(gameState == GameState.PAUSED) {
            gameState = GameState.RUNNING;
        } else {
            gameState = GameState.PAUSED;
        }
    }

    @Override
    public void run() {
        gameState = GameState.RUNNING;
        try {
            gameLoop();
        } catch (InterruptedException e) {
            new ErrorDialog("Game thread interrupted!", e);
        }
    }

    public GameLoop(GamePanel panel) {
        this.currentPanel = panel;
    }

    public GameState getGameState() {
        return gameState;
    }
}
