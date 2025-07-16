package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public interface GameMode {
    void resetPosition();
    void flap();
    boolean isBetweenPipes(PipeFormation pipeFormation);
    boolean areAllBirdsDead();
    Iterator<Bird> iterator();
    void init();
    boolean usesMachineLearning();
    int size();
    Bird getBirdAt(int index);
    ArrayList<FlappyBirdPlayer> getPlayers();


    FlappyBirdPlayer getBestPlayer();
    Bird getPlayerControlledBird();
    ArrayList<Bird> getBirds();

    static GameMode valueOf(String s) {
        return switch (s) {
            case "PLAYER_MODE" -> new PlayerMode();
            case "ML_MODE" -> new MachineLearningMode();
            case "PRE_TRAINED_MODE" -> new PretrainedMode();
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }

    static ArrayList<GameMode> values() {
        return new ArrayList<>(Arrays.asList(
                new PlayerMode(),
                new MachineLearningMode(),
                new PretrainedMode()
        ));
    }

    default boolean isPlayerMode() {
        return this instanceof PlayerMode;
    }

    default boolean isMlMode() {
        return this instanceof MachineLearningMode;
    }

    default boolean isPretrainedMode() {
        return this instanceof PretrainedMode;
    }
}
