package com.antekk.flappybird.game.loop;

import com.antekk.flappybird.game.GameController;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.view.ErrorDialog;
import com.antekk.flappybird.view.GamePanel;

public class GameLoop extends Thread {
    private final GamePanel currentPanel;
    private GameState gameState;

    private final int timeBetweenFramesMillis = 1000 / 180;

    private void gameLoop() throws InterruptedException {
        while (gameState != GameState.LOST) {
            Thread.sleep(timeBetweenFramesMillis); //this sucks, but uses less cpu than time ms tracking

            if(gameState == GameState.PAUSED || gameState == GameState.STARTING)
                continue;

            Bird bird = currentPanel.getBird();
            if(bird.framesSinceBirdStartedMoving >= 90 && bird.isMovingUp) {
                bird.isMovingUp = false;
                bird.framesSinceBirdStartedMoving = 0;
            }


            if(!bird.isMovingUp) {
                bird.moveUpBy((int) -Math.ceil((4 * Math.sin((double) bird.framesSinceBirdStartedMoving / 60))));
                if(bird.framesSinceBirdStartedMoving < 90)
                    bird.framesSinceBirdStartedMoving++;
            }



            if(bird.isMovingUp) {
                bird.moveUpBy((int) Math.floor((4 * Math.cos((double) bird.framesSinceBirdStartedMoving / 60))));
                bird.framesSinceBirdStartedMoving += 3;

            }

            currentPanel.paintImmediately(bird.getX(),GameController.getBlockSizePx(),
                    GameController.getBlockSizePx(), currentPanel.getHeight() - GameController.getBlockSizePx());

            gameState = updateGameState();
        }
    }

    private GameState updateGameState() {
        if(gameState == GameState.PAUSED) {
            return GameState.PAUSED;
        }

        if(currentPanel.getBird().getY() + GameController.getBlockSizePx() >= GamePanel.BOTTOM) {
            return GameState.LOST;
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
        gameState = GameState.STARTING;
        try {
            gameLoop();
        } catch (InterruptedException e) {
            new ErrorDialog("Game thread interrupted!", e);
        }
    }

    public void startGame() {
        gameState = GameState.RUNNING;
    }

    public GameLoop(GamePanel panel) {
        this.currentPanel = panel;
    }

    public GameState getGameState() {
        return gameState;
    }
}
