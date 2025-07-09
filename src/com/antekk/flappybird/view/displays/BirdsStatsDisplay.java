package com.antekk.flappybird.view.displays;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.bird.gamemodes.MachineLearningMode;
import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.view.GamePanel;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.RIGHT;
import static com.antekk.flappybird.view.themes.GameColors.birdMidFlap;

public class BirdsStatsDisplay {
    private final GamePanel panel;


    public BirdsStatsDisplay(GamePanel panel) {
        this.panel = panel;
    }

    public synchronized void draw(Graphics g) {
        GameLoop loop = panel.getGameLoop();
        if(!loop.getGameMode().isMlMode())
            return;

        g.setFont(g.getFont().deriveFont(32f));
        g.drawString("Generation " + loop.getGenerationNumber(), RIGHT + 2 * getBlockSizePx(), 2 * getBlockSizePx());

        g.setFont(g.getFont().deriveFont(18f));
        for(int i = 0; i < loop.getAmountOfBirds(); i++) {
            Bird bird = loop.getBirdAt(i);
            int y = (int) ((int) ((i+1) * 1.3 * getBlockSizePx()) + 1.2 * getBlockSizePx());
                g.drawImage(birdMidFlap, RIGHT + getBlockSizePx(),y,getBlockSizePx(), getBlockSizePx(), null);
            g.drawString("Fitness = " + bird.getFitness(), (int) (2.5 * getBlockSizePx()) + RIGHT, y + getBlockSizePx() / 2);
            g.drawString("Score = " + bird.getPlayer().score,
                    (int) (2.5 * getBlockSizePx()) + RIGHT, y + getBlockSizePx() / 2 + g.getFontMetrics().getHeight());
        }
    }

    public Dimension getPreferredSize() {
        if(!panel.getGameLoop().getGameMode().isMlMode())
            return new Dimension(0,0);

        return new Dimension(
                10 * getBlockSizePx(),
                16 * getBlockSizePx()
        );
    }
}
