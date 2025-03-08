package com.antekk.flappybird.game.bird;

import com.antekk.flappybird.game.pipes.BottomPipe;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.game.pipes.TopPipe;
import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.game.GameController.getBlockSizePx;

public class Bird {
    private int posX = GamePanel.getBoardCols() * getBlockSizePx() / 2;
    private int posY = (int) (3.33 * getBlockSizePx());
    public boolean isMovingUp = false;
    public int framesSinceBirdStartedMoving = 0;

    public void flap() {
        framesSinceBirdStartedMoving = 0;
        isMovingUp = true;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(posX, posY,
                getBlockSizePx(), getBlockSizePx());
    }

    public void moveUpBy(int dy) {
        posY -= dy;
    }

    public boolean collidesWithPipeFormation(PipeFormation pipeFormation) {
        BottomPipe bottomPipe = pipeFormation.getBottomPipe();
        TopPipe topPipe = pipeFormation.getTopPipe();

        //bottom pipe collision from top
        if((getX() >= bottomPipe.getEndingRect().x &&
                getX() <= bottomPipe.getEndingRect().x + bottomPipe.getEndingRect().width) &&
                getY() + getBlockSizePx() >= bottomPipe.getY()) {
            return true;
        }

        //top pipe collision from bottom
        if((getX() >= topPipe.getEndingRect().x &&
                getX() <= topPipe.getEndingRect().x + topPipe.getEndingRect().width) &&
                getY() <= topPipe.getY() + topPipe.getHeight()) {
            return true;
        }

        //collisions for pipes sides
        if((getX() + getBlockSizePx() >= topPipe.getX() && getX() <= topPipe.getX() + getBlockSizePx()) &&
                (getY() <= (topPipe.getY() + topPipe.getHeight()) || getY() >= (bottomPipe.getY()))) {
            return true;
        }

        Rectangle top = topPipe.getEndingRect();
        Rectangle bottom = bottomPipe.getEndingRect();

        if((getX() + getBlockSizePx() >= top.x && getX() <= top.x + top.width) &&
                ((getY() <= (top.y + top.height) && getY() + getBlockSizePx() >= top.y) ||
                (getY() + getBlockSizePx() >= bottom.y && getY() <= bottom.y + bottom.height))) {
            return true;
        }

        return false;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
