package com.antekk.flappybird.game.loop;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.bird.Birds;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;
import com.antekk.flappybird.view.ErrorDialog;
import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.*;

public class GameLoop extends Thread {
    private final GamePanel currentPanel;
    private GameState gameState;
    private final FlappyBirdPlayer player = new FlappyBirdPlayer();
    private final ArrayList<PipeFormation> pipes = new ArrayList<>();
    private final Birds birds = new Birds(10);

    private boolean wasScoreAddedAtCurrentPipe = false;
    private int framesSincePipeSpawned = 0;
    private int framesSinceIdleSpriteChanged = 0;
    private final int timeBetweenFramesMillis = 1000 / 60;

    private void gameLoop() throws InterruptedException {
        pipes.add(new PipeFormation());
        while (gameState != GameState.LOST) {
            Thread.sleep(timeBetweenFramesMillis); //this sucks, but uses less cpu than time ms tracking

            if(gameState == GameState.ENDED) {
                return;
            }

            if(gameState == GameState.PAUSED) {
                groundX -= (int) (0.067 * getBlockSizePx());
                currentPanel.repaint();
                continue;
            }

            if (gameState == GameState.STARTING) {
                gameStartingLogic();
                continue;
            }

            pipeLogic();

            for(Bird bird : birds) {
                birdLogic(bird);
                birdDeathLogic(bird);
            }

            scoreLogic();

            gameState = updateGameState();
            currentPanel.paintImmediately(LEFT, TOP, RIGHT - LEFT, BOTTOM - TOP);
        }

        //game over falling animation
        int gameOverFallingFrames = 25;
        Bird bird = birds.getDefault(); //TODO: temp
        bird.rotationAngle = 15;
        bird.isMovingUp = false;
        while(bird.getSpritePosY() < GROUND) {
            bird.isMovingUp = false;
            bird.rotationAngle++;
            bird.moveUpBy((int) -Math.ceil(((double) getBlockSizePx() / 3 * Math.tan((double) gameOverFallingFrames / 60))));
            if (gameOverFallingFrames < 90)
                gameOverFallingFrames += 1;
            bird.framesSinceBirdStartedMoving = gameOverFallingFrames;
            currentPanel.repaint();
            Thread.sleep(timeBetweenFramesMillis);
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

    private void birdDeathLogic(Bird bird) {
        //bird collided with the ground
        if (bird.getSpritePosY() >= GROUND) {
            bird.isAlive = false;
        }

        //collision with pipes
        //can compare with only [0] and [1] here, prob should look into it later
        for (PipeFormation pipeFormation : pipes) {
            if (bird.collidesWithPipeFormation(pipeFormation)) {
                bird.isAlive = false;
            }
        }
    }

    private void scoreLogic() {
        Bird bird = birds.getDefault();
        for (PipeFormation pipeFormation : pipes) {
            if (bird.isBetweenPipes(pipeFormation) && !wasScoreAddedAtCurrentPipe) {
                player.addScore();
                wasScoreAddedAtCurrentPipe = true;
                break;
            }
        }

        boolean wasAdded = false;
        for (PipeFormation pipeFormation : pipes) {
            if (bird.isBetweenPipes(pipeFormation)) {
                wasAdded = true;
                break;
            }
        }

        if (!wasAdded)
            wasScoreAddedAtCurrentPipe = false;
    }

    private void pipeLogic() {
        if(framesSincePipeSpawned >= 90) {
            pipes.add(new PipeFormation());
            framesSincePipeSpawned = 0;
        }
        groundX -= (int) (0.067 * getBlockSizePx());

        for(Iterator<PipeFormation> it = pipes.iterator(); it.hasNext();) {
            PipeFormation pipe = it.next();
            pipe.moveX(-(int) (0.067 * getBlockSizePx()));
            if(pipe.getTopPipe().getX() + getBlockSizePx() < LEFT) {
                it.remove();
            }
        }
        framesSincePipeSpawned++;
    }

    private void birdLogic(Bird bird) {
        if (bird.framesSinceBirdStartedMoving >= 90 && bird.isMovingUp) {
            bird.isMovingUp = false;
            bird.framesSinceBirdStartedMoving = 0;
        }

        if (!bird.isMovingUp) {
            bird.rotationAngle++;
            bird.moveUpBy((int) -Math.ceil(((double) getBlockSizePx() / 6 * Math.sin((double) bird.framesSinceBirdStartedMoving / 60))));
            if (bird.framesSinceBirdStartedMoving < 90)
                bird.framesSinceBirdStartedMoving += 9;
        }

        if (bird.isMovingUp) {
            bird.rotationAngle = 0;
            bird.moveUpBy((int) Math.floor((double) getBlockSizePx() / 6 * Math.cos((double) bird.framesSinceBirdStartedMoving / 60)));
            bird.framesSinceBirdStartedMoving += 6;
        }
        bird.nextMove(pipes.get(0));
    }

    private void gameStartingLogic() {
        for(Bird bird : birds) {
            framesSinceIdleSpriteChanged++;
            if (framesSinceIdleSpriteChanged <= 20) {
                currentPanel.repaint();
                continue;
            }

            framesSinceIdleSpriteChanged = 0;

            groundX -= (int) (0.067 * getBlockSizePx());
            if (bird.framesSinceBirdStartedMoving != 0)
                bird.isMovingUp = !bird.isMovingUp;

            bird.rotationAngle = 0;
            bird.framesSinceBirdStartedMoving = 90;
        }
    }

    private GameState updateGameState() {
        if (gameState == GameState.PAUSED) {
            return GameState.PAUSED;
        }

        if(birds.areAllBirdsDead()) {
            return GameState.LOST; //TODO: temp
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

    public GameLoop(GamePanel panel) {
        this.currentPanel = panel;
        birds.resetPosition();
        pipes.clear();
    }

    public Bird getDefaultBird() {
        return birds.getDefault();
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
        player.pipesVerticalGap = PipeFormation.futureGap;
        PipeFormation.updatePipeGap();
    }

    public void endGame() {
        gameState = GameState.ENDED;
    }

    public GameState getGameState() {
        return gameState;
    }

    public FlappyBirdPlayer getPlayer() {
        return player;
    }

    public ArrayList<PipeFormation> getPipes() {
        return pipes;
    }

    public Birds getBirds() {
        return birds;
    }
}
