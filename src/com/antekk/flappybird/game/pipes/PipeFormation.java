package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public class PipeFormation {
    private TopPipe topPipe;
    private BottomPipe bottomPipe;
    private static int gap = 3 * getBlockSizePx();
    public static int futureGap = 0;


    public PipeFormation() {
        int minPipeHeight = getBlockSizePx();
        int dy = GamePanel.GROUND - GamePanel.TOP;

        int top = (int) (Math.random() * (dy - 2 * minPipeHeight - gap)) + minPipeHeight;
        int bottom = dy - top - gap;

        topPipe = new TopPipe(top);
        bottomPipe = new BottomPipe(bottom);
    }

    public void draw(Graphics g) {
        topPipe.draw(g);
        bottomPipe.draw(g);
    }

    public int getCenterY() {
        return (bottomPipe.y - (topPipe.y + topPipe.height)) / 2;
    }

    public TopPipe getTopPipe() {
        return topPipe;
    }

    public BottomPipe getBottomPipe() {
        return bottomPipe;
    }

    public void moveX(int dx) {
        getTopPipe().moveX(dx);
        getBottomPipe().moveX(dx);
    }

    public static void updatePipeGap() {
        gap = futureGap;
    }

}
