package com.antekk.flappybird.view.displays;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.bird.gamemodes.Birds;
import com.antekk.flappybird.game.bird.gamemodes.MachineLearningMode;
import com.antekk.flappybird.game.loop.GameLoop;

import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.RIGHT;
import static com.antekk.flappybird.view.themes.GameColors.birdMidFlap;

public class BirdsStatsDisplay {
    private final GameLoop loop;


    public BirdsStatsDisplay(GameLoop loop) {
        this.loop = loop;
    }

    public synchronized void draw(Graphics g) {
        Birds birds = loop.getBirds();
        if(!(birds.getGameMode() instanceof MachineLearningMode))
            return;

        g.setFont(g.getFont().deriveFont(32f));
        g.drawString("Generation " + birds.getGenerationNumber(), RIGHT + 2 * getBlockSizePx(), 2 * getBlockSizePx());

        g.setFont(g.getFont().deriveFont(18f));
        for(int i = 0; i < birds.size(); i++) {
            Bird bird = birds.getBirdAt(i);
            int y = (int) ((int) ((i+1) * 1.2 * getBlockSizePx()) + 1.2 * getBlockSizePx());
                g.drawImage(birdMidFlap, RIGHT + getBlockSizePx(),y,getBlockSizePx(), getBlockSizePx(), null);
            g.drawString("Fitness = " + bird.getFitness(), (int) (2.5 * getBlockSizePx()) + RIGHT, y + getBlockSizePx() / 2);
        }
    }

    public Dimension getPreferredSize() {
        if(!(loop.getBirds().getGameMode() instanceof MachineLearningMode))
            return new Dimension(0,0);

        return new Dimension(
                10 * getBlockSizePx(),
                16 * getBlockSizePx()
        );
    }
}
