package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.game.GameController.getBlockSizePx;

public abstract class Pipe {
    protected int x;
    protected final int y;
    protected final int height;
    protected Rectangle endingRect;

    protected Pipe(int y, int heightInPx) {
        this.x = GamePanel.RIGHT + 2 * getBlockSizePx();
        this.y = y;
        this.height = heightInPx;
        setEndingRectangle();
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(x, y, getBlockSizePx(), height);

        g.fillRect(endingRect.x, endingRect.y, endingRect.width, endingRect.height);
    }

    protected abstract void setEndingRectangle();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public Rectangle getEndingRect() {
        return endingRect;
    }

    protected void moveX(int dx) {
        x += dx;
        endingRect.x += dx;
    }
}
