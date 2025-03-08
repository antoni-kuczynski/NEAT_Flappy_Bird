package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.game.GameController;

import java.awt.*;

abstract class Pipe {
    protected final int x;
    protected final int y;
    protected final int height;
    protected Rectangle endingRect;

    protected Pipe(int y, int heightInPx) {
        this.x = 300;
        this.y = y;
        this.height = heightInPx;
        setEndingRectangle();
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN.darker());
        g.fillRect(x, y, GameController.getBlockSizePx(), height);

        g.fillRect(endingRect.x, endingRect.y, endingRect.width, endingRect.height);
    }

    protected abstract void setEndingRectangle();


}
