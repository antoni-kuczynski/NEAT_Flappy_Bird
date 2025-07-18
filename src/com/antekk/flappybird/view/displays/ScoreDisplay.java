package com.antekk.flappybird.view.displays;

import com.antekk.flappybird.view.GamePanel;
import com.antekk.flappybird.view.themes.GameColors;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public class ScoreDisplay {
    private final GamePanel panel;

    public ScoreDisplay(GamePanel panel) {
        this.panel = panel;
    }

    public void draw(Graphics g) {
        String s = String.valueOf(panel.getGameLoop().getBestPlayer().score);
        for(int i = 0; i < s.length(); i++) {
            char c = s.toCharArray()[i];
            g.drawImage(GameColors.numbers.get(c - 48), (int) (5 * getBlockSizePx() + (i * 0.67 * getBlockSizePx()) + (double) getBlockSizePx() / 6),
                    (int) (3.33 * getBlockSizePx()), (int) (0.67 * getBlockSizePx()), getBlockSizePx(), null);
        }

    }
}
