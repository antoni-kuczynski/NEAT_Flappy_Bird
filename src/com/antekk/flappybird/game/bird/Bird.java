package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.GameController;

import java.awt.*;

public class Bird {
    private int posX = (int) (1.66 * GameController.getBlockSizePx());
    private int posY = (int) (3.33 * GameController.getBlockSizePx());
    public boolean isMovingUp = false;
    public int framesSinceBirdStartedMoving = 0;

    public void flap() {
        framesSinceBirdStartedMoving = 0;
        isMovingUp = true;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(posX, posY,
                GameController.getBlockSizePx(), GameController.getBlockSizePx());
    }

    public void moveUpBy(int dy) {
        posY -= dy;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
