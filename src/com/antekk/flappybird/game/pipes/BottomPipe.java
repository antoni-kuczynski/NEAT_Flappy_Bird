package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;
import com.antekk.flappybird.view.themes.GameColors;

import java.awt.*;

public class BottomPipe extends Pipe {

    protected BottomPipe(int heightInPx) {
        super(GamePanel.TOP + GamePanel.GROUND - heightInPx, heightInPx);
    }

    @Override
    public void draw(Graphics g) {
        int i;
        for(i = (int) (0.46 * width); i <= height; i++) {
            g.drawImage(GameColors.pipe, x, i + y, width, 1, null);
        }
        g.drawImage(GameColors.pipeEnd, x, y, width, (int) (0.46 * width), null);
    }

}
