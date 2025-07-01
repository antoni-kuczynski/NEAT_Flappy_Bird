package com.antekk.flappybird.game.bird;

import java.awt.*;

public interface PlayerBird {

    void draw(Graphics g);
    void drawWithoutRotation(Graphics g);
    void resetPosition();
    void flap();
}
