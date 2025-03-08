package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.game.GameController.getBlockSizePx;

public class BottomPipe extends Pipe {

    protected BottomPipe(int heightInPx) {
        super(GamePanel.TOP + GamePanel.GROUND - heightInPx, heightInPx);
    }

    @Override
    protected void setEndingRectangle() {
        int rectWidth = (int) (1.5 * getBlockSizePx());
        int rectHeight = getBlockSizePx() / 3;
        endingRect = new Rectangle((int) (x - 0.25 * getBlockSizePx()), y, rectWidth, rectHeight);
    }
}
