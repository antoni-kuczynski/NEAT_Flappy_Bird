package com.antekk.flappybird.game.loop;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;
import com.antekk.flappybird.view.ErrorDialog;
import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.util.Iterator;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.*;

public class GameLoop extends Thread {
    private final GamePanel currentPanel;
    private GameState gameState;
    private final FlappyBirdPlayer player = new FlappyBirdPlayer();
    private boolean wasScoreAddedAtCurrentPipe = false;
    private int framesSincePipeSpawned = 0;
    private int framesSinceIdleSpriteChanged = 0;

    private final int timeBetweenFramesMillis = 1000 / 60;

    private void gameLoop() throws InterruptedException {
        while (gameState != GameState.LOST) {
            Thread.sleep(timeBetweenFramesMillis); //this sucks, but uses less cpu than time ms tracking

            if(gameState == GameState.PAUSED) {
                groundX -= (int) (0.067 * getBlockSizePx());
                currentPanel.repaint();
                continue;
            }

            Bird bird = currentPanel.getBird();
            if(gameState == GameState.STARTING) {
                groundX -= (int) (0.067 * getBlockSizePx());

                framesSinceIdleSpriteChanged++;
                if(framesSinceIdleSpriteChanged <= 45) {
                    currentPanel.repaint();
                    continue;
                }

                if(currentPanel.getBird().framesSinceBirdStartedMoving != 0)
                    currentPanel.getBird().isMovingUp = !currentPanel.getBird().isMovingUp;

                currentPanel.getBird().framesSinceBirdStartedMoving = 90;

                framesSinceIdleSpriteChanged = 0;
                currentPanel.repaint();
                continue;
            }

            if(framesSincePipeSpawned >= 90) {
                currentPanel.getPipes().add(new PipeFormation());
                framesSincePipeSpawned = 0;
            }
            groundX -= (int) (0.067 * getBlockSizePx());

            for(Iterator<PipeFormation> it = currentPanel.getPipes().iterator(); it.hasNext();) {
                PipeFormation pipe = it.next();
                pipe.moveX(-(int) (0.067 * getBlockSizePx()));
                if(pipe.getTopPipe().getX() + getBlockSizePx() < LEFT) {
                    it.remove();
                }
            }


            if(bird.framesSinceBirdStartedMoving >= 90 && bird.isMovingUp) {
                bird.isMovingUp = false;
                bird.framesSinceBirdStartedMoving = 0;
            }

            if(!bird.isMovingUp) {
                bird.moveUpBy((int) -Math.ceil(((double) getBlockSizePx() / 6 * Math.sin((double) bird.framesSinceBirdStartedMoving / 60))));
                if(bird.framesSinceBirdStartedMoving < 90)
                    bird.framesSinceBirdStartedMoving += 7;
            }

            if(bird.isMovingUp) {
                bird.moveUpBy((int) Math.floor((double) getBlockSizePx() / 6 * Math.cos((double) bird.framesSinceBirdStartedMoving / 60)));
                bird.framesSinceBirdStartedMoving += 5;

            }

            framesSincePipeSpawned++;

            for (PipeFormation pipeFormation : currentPanel.getPipes()) {
                if (bird.isBetweenPipes(pipeFormation) && !wasScoreAddedAtCurrentPipe) {
                    player.addScore();
                    wasScoreAddedAtCurrentPipe = true;
                    break;
                }
            }

            boolean wasAdded = false;
            for (PipeFormation pipeFormation : currentPanel.getPipes()) {
                if (bird.isBetweenPipes(pipeFormation)) {
                    wasAdded = true;
                    break;
                }
            }

            if(!wasAdded)
                wasScoreAddedAtCurrentPipe = false;

            gameState = updateGameState();
            currentPanel.paintImmediately(LEFT, TOP, RIGHT - LEFT, BOTTOM - TOP);
        }

        player.name = JOptionPane.showInputDialog(
                null,
                "Enter your name",
                "Game over",
                JOptionPane.INFORMATION_MESSAGE
        );

        if(player.name != null && !player.name.isEmpty())
            FlappyBirdPlayer.getStatsFile().addPlayer(player);
    }

    private GameState updateGameState() {
        if (gameState == GameState.PAUSED) {
            return GameState.PAUSED;
        }

        //bird collided with the ground
        if (currentPanel.getBird().getY() >= GROUND) {
            return GameState.LOST;
        }

        //collision with pipes
        //can compare with only [0] and [1] here, prob should look into it later
        for (PipeFormation pipeFormation : currentPanel.getPipes()) {
            if (currentPanel.getBird().collidesWithPipeFormation(pipeFormation)) {
                return GameState.LOST;
            }
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

    public FlappyBirdPlayer getPlayer() {
        return player;
    }
}
