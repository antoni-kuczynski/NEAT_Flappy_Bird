package com.antekk.flappybird.view;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.bird.Birds;

import javax.swing.*;
import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.RIGHT;
import static com.antekk.flappybird.view.themes.GameColors.birdMidFlap;

public class BirdsStatsDisplay {
    private final Birds birds;


    public BirdsStatsDisplay(Birds birds) {
        this.birds = birds;
    }

    public synchronized void draw(Graphics g) {
        g.setFont(g.getFont().deriveFont(32f));
        g.drawString("Generation " + birds.getGenerationNumber(), RIGHT + 2 * getBlockSizePx(), 2 * getBlockSizePx());

        g.setFont(g.getFont().deriveFont(18f));
        for(int i = 0; i < birds.size(); i++) {
            Bird bird = birds.get(i);
            int y = (int) ((int) ((i+1) * 1.2 * getBlockSizePx()) + 1.2 * getBlockSizePx());
                g.drawImage(birdMidFlap, RIGHT + getBlockSizePx(),y,getBlockSizePx(), getBlockSizePx(), null);
            g.drawString("Fitness = " + bird.getFitness(), (int) (2.5 * getBlockSizePx()) + RIGHT, y + getBlockSizePx() / 2);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(
                10 * getBlockSizePx(),
                16 * getBlockSizePx()
        );
    }
}
