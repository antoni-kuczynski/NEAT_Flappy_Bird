package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;
import com.antekk.flappybird.view.themes.GameColors;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public abstract class Pipe {
    protected int x;
    protected final int y;
    protected final int width = getBlockSizePx();
    protected final int height;

    protected Pipe(int y, int heightInPx) {
        this.x = GamePanel.RIGHT + 2 * getBlockSizePx();
        this.y = y;
        this.height = heightInPx;
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    protected void moveX(int dx) {
        x += dx;
    }
}
