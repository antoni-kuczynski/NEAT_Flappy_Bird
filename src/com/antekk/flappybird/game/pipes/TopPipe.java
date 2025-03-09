package com.antekk.flappybird.game.pipes;

import com.antekk.flappybird.view.GamePanel;
import com.antekk.flappybird.view.themes.GameColors;

import java.awt.*;

public class TopPipe extends Pipe {

    protected TopPipe(int heightInPx) {
        super(GamePanel.TOP, heightInPx);
    }

    @Override
    public void draw(Graphics g) {
        int i;
        for(i = 0; i <= height - (int) (0.46 * width); i++) {
            g.drawImage(GameColors.pipe, x, i + y, width, 1, null);
        }

        g.drawImage(GameColors.pipeEnd, x, i + y, width, (int) (0.46 * width), null);
    }
}
