package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.pipes.PipeFormation;

import java.awt.*;

public interface PlayerBird {

    void draw(Graphics g);
    void drawWithoutRotation(Graphics g);
    void resetPosition();
    void flap();
    boolean isBetweenPipes(PipeFormation pipeFormation);
}
