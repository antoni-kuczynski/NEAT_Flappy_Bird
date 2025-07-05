package com.antekk.flappybird.game.bird.gamemodes;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.pipes.PipeFormation;

import java.awt.*;
import java.util.Iterator;

public interface GameMode {
    void draw(Graphics g, Birds birds);
    void drawWithoutRotation(Graphics g, Birds birds);
    void resetPosition(Birds birds);
    void flap(Birds birds);
    boolean isBetweenPipes(PipeFormation pipeFormation, Birds birds);
    boolean areAllBirdsDead(Birds birds);
    Iterator<Bird> iterator(Birds birds);
    void init(Birds birds);
    boolean usesMachineLearning();
    int size(Birds birds);
    Bird getBirdAt(int index, Birds birds);

    static GameMode valueOf(String s) {
        return switch (s) {
            case "PLAYER_MODE" -> new PlayerMode();
            case "ML_MODE" -> new MachineLearningMode();
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
