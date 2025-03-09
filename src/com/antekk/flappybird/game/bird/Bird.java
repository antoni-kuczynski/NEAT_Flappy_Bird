package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.pipes.BottomPipe;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.pipes.TopPipe;
import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.themes.GameColors.*;

public class Bird {
    private int posX;
    private int posY;
    private int width;
    private int height;
    public boolean isMovingUp = false;
    public int framesSinceBirdStartedMoving = 0;


    public void resetPosition() {
        posX = (GamePanel.getBoardCols() - 2) * getBlockSizePx() / 2;
        posY = (int) (3.33 * getBlockSizePx());
        width = (int) (1.41 * getBlockSizePx());
        height = getBlockSizePx();
    }

    public void flap() {
        framesSinceBirdStartedMoving = 0;
        isMovingUp = true;
    }

    public void draw(Graphics g) {
        //TODO: bird rotation when falling
        if(isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdUpFlap, getX(), getY(), getWidth(), getHeight(), null);
        } else if(!isMovingUp && framesSinceBirdStartedMoving != 0) {
            g.drawImage(birdDownFlap, getX(), getY(), getWidth(), getHeight(), null);
        } else {
            g.drawImage(birdMidFlap, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public void moveUpBy(int dy) {
        posY -= dy;
    }

    public boolean collidesWithPipeFormation(PipeFormation pipeFormation) {
        BottomPipe bottomPipe = pipeFormation.getBottomPipe();
        TopPipe topPipe = pipeFormation.getTopPipe();

        //bottom pipe collision from top
        if((getX() + getWidth() >= bottomPipe.getX() &&
                getX() <= bottomPipe.getX() + bottomPipe.getWidth()) &&
                getY() + getHeight() >= bottomPipe.getY()) {
            return true;
        }

        //top pipe collision from bottom
        if((getX() >= topPipe.getX() &&
                getX() <= topPipe.getX() + topPipe.getWidth()) &&
                getY() <= topPipe.getY() + topPipe.getHeight()) {
            return true;
        }

        //collisions for pipes sides
        if((getX() + getWidth() >= topPipe.getX() && getX() <= topPipe.getX() + getBlockSizePx()) &&
                (getY() <= (topPipe.getY() + topPipe.getHeight()) || getY() >= (bottomPipe.getY()))) {
            return true;
        }

        return false;
    }

    public boolean isBetweenPipes(PipeFormation pipeFormation) {
        return (getX() + getWidth() >= pipeFormation.getTopPipe().getX() &&
                getX() <= pipeFormation.getTopPipe().getX() + pipeFormation.getTopPipe().getWidth() &&
                getY() >= pipeFormation.getTopPipe().getY() + pipeFormation.getTopPipe().getHeight() &&
                getY() + getHeight() <= pipeFormation.getBottomPipe().getY());
    }

    public Bird() {
        resetPosition();
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
