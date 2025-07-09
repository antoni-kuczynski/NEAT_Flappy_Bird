package com.antekk.flappybird.game.loop;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.bird.gamemodes.Birds;
import com.antekk.flappybird.game.bird.gamemodes.MachineLearningMode;
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
    private final ArrayList<PipeFormation> pipes = new ArrayList<>();
    private final Birds birds = new Birds(ConfigJSON.getGameMode());
    private int framesSincePipeSpawned = 0;
    private int framesSinceIdleSpriteChanged = 0;
    private final int timeBetweenFramesMillis = 1000 / 60;

    private void gameLoop() throws InterruptedException {
        pipes.add(new PipeFormation());
        while (gameState != GameState.LOST) {
            Thread.sleep(timeBetweenFramesMillis); //this sucks, but uses less cpu than time ms tracking

            groundX -= (int) (0.067 * getBlockSizePx());

            if(gameState == GameState.ENDED) {
                return;
            }

            if(gameState == GameState.PAUSED) {
                continue;
            }

            if (gameState == GameState.STARTING) {
                gameStartingLogic();
                continue;
            }

            if(gameState == GameState.NEXT_GENERATION) {
                ((MachineLearningMode) birds.getGameMode()).newPopulation(birds);
                pipes.clear();
                framesSincePipeSpawned = 0;
                pipes.add(new PipeFormation());
                gameState = GameState.RUNNING;
                continue;
            }

            pipeLogic();

            for(Bird bird : birds) {
                birdLogic(bird);
                birdDeathLogic(bird);
            }

            scoreLogic(birds);

            gameState = updateGameState();
            currentPanel.paintImmediately(LEFT, TOP, RIGHT - LEFT + currentPanel.birdsStatDisplayWidth, BOTTOM - TOP);
        }

        if(!ConfigJSON.showNewBestDialog())
            return;

        getBestPlayer().name = JOptionPane.showInputDialog(
                null,
                "Enter your name",
                "Game over - Score: " + getBestPlayer().score,
                JOptionPane.INFORMATION_MESSAGE
        );

        if(getBestPlayer().name != null && !getBestPlayer().name.isEmpty())
            FlappyBirdPlayer.getStatsFile().addPlayer(getBestPlayer());
    }

    private void birdDeathLogic(Bird bird) throws InterruptedException {
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
        if(bird.isAlive || bird.getSpritePosY() >= GROUND || bird.getSpritePosY() < TOP) return;

        if(birds.getGameMode() instanceof MachineLearningMode) return;

        bird.deathAnimationThread(timeBetweenFramesMillis, currentPanel).start();
    }

    private void scoreLogic(Birds birds) {
        for(Bird bird : birds) {
            for (PipeFormation pipeFormation : pipes) {
                FlappyBirdPlayer player = bird.getPlayer();
                if (bird.isAlive && !player.wasScoreAddedAtPipe && bird.isBetweenPipes(pipeFormation)) {
                    player.addScore();
                    player.wasScoreAddedAtPipe = true;
                }
            }
        }

        for (Bird bird : birds) {
            boolean isBetweenAll = false;
            for (PipeFormation pipeFormation : pipes) {
                if (bird.isBetweenPipes(pipeFormation))
                    isBetweenAll = true;
            }
            bird.getPlayer().wasScoreAddedAtPipe = isBetweenAll;
        }
    }

    private void pipeLogic() {
        if(framesSincePipeSpawned >= 90) {
            pipes.add(new PipeFormation());
            framesSincePipeSpawned = 0;
        }

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
        if(!bird.isAlive && bird.getSpriteXPos() >= LEFT - bird.getSpriteWidth()) {
            bird.moveHorizontallyBy((int) (0.067 * getBlockSizePx()));
            return;
        }

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

        bird.totalTraveledDistance += (int) (0.067 * getBlockSizePx());
        bird.nextMove(pipes);
    }

    private void gameStartingLogic() {
        for(Bird bird : birds) {
            framesSinceIdleSpriteChanged++;
            if (framesSinceIdleSpriteChanged <= 20) {
                currentPanel.repaint();
                continue;
            }

            framesSinceIdleSpriteChanged = 0;

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

        if(birds.getGameMode() instanceof MachineLearningMode && birds.areAllBirdsDead()) { //TODO
            return GameState.NEXT_GENERATION;
        } else if(!(birds.getGameMode() instanceof MachineLearningMode) && birds.areAllBirdsDead()) {
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

    public GameLoop(GamePanel panel) {
        this.currentPanel = panel;
        birds.resetPosition();
        pipes.clear();
    }

    public Bird getPlayerControlledBird() {
        return birds.getPlayerControlledBird();
    }

    @Override
    public void run() {
        gameState = GameState.STARTING;
//        birdsStatsDialogThread().start();
        try {
            gameLoop();
        } catch (InterruptedException e) {
            new ErrorDialog("Game thread interrupted!", e);
        }
    }

    public FlappyBirdPlayer getBestPlayer() {
        return birds.getBestPlayer();
    }

    private ArrayList<FlappyBirdPlayer> getPlayers() {
        return birds.getPlayers();
    }

    public void startGame() {
        gameState = GameState.RUNNING;
        for(FlappyBirdPlayer p : getPlayers())
            p.pipesVerticalGap = PipeFormation.futureGap;
        PipeFormation.updatePipeGap();
    }

    public void endGame() {
        gameState = GameState.ENDED;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<PipeFormation> getPipes() {
        return pipes;
    }

    public Birds getBirds() {
        return birds;
    }
}
