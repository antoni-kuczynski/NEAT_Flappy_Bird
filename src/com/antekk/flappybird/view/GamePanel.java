package com.antekk.flappybird.view;

import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.view.themes.GameColors;

import javax.swing.*;
import java.awt.*;

import static com.antekk.flappybird.game.GameController.getBlockSizePx;
import static com.antekk.flappybird.game.keybinds.GameKeybinds.setupKeyBindings;

public class GamePanel extends JPanel {
    public static int LEFT;
    public static int TOP;
    public static int RIGHT;
    public static int BOTTOM;
    private final JPanel toolbar = new JPanel();
    private final GameLoop loop = new GameLoop(this);

    @Override
    protected synchronized void paintComponent(Graphics g1) {
        super.paintComponent(g1);

        Graphics2D g = (Graphics2D)  g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(loop.getGameState() == GameState.PAUSED) {
            drawPauseScreen(g1);
            return;
        }

    }


    private void drawPauseScreen(Graphics g) {
        g.setColor(GameColors.boardColor);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(GameColors.foregroundColor);
        g.drawString("Game paused", getBlockSizePx() * 2, getBlockSizePx() * 4);
    }

    protected GamePanel(JFrame parent) {
        LEFT = 0;
        TOP = getBlockSizePx();
        RIGHT = getBoardRows() * getBlockSizePx();
        BOTTOM =  getBoardCols() * getBlockSizePx();
        parent.setPreferredSize(this.getPreferredSize());

        setLayout(new BorderLayout());
        setDoubleBuffered(true);
        setBackground(GameColors.backgroundColor);

        JButton newGame = new JButton("New game");
        JButton pauseGame = new JButton("Pause game");
        JButton showBestPlayers = new JButton("Best players");
        JButton options = new JButton("Options");

        newGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        pauseGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        showBestPlayers.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        options.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));

        BoxLayout layout = new BoxLayout(toolbar, BoxLayout.X_AXIS);
        toolbar.setLayout(layout);
        toolbar.setBackground(GameColors.backgroundColor);

        toolbar.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 3)));
        toolbar.add(newGame);
        toolbar.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 3)));
        toolbar.add(pauseGame);
        toolbar.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 3)));
        toolbar.add(showBestPlayers);
        toolbar.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 3)));
        toolbar.add(options);

        add(toolbar, BorderLayout.PAGE_START);

        newGame.setFocusable(false);
        pauseGame.setFocusable(false);
        showBestPlayers.setFocusable(false);
        options.setFocusable(false);

        newGame.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null,
                    "Do you really want to start a new game at level " + -1 + "?",
                    "Are you sure?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if(option == 1)
                return;

        });

        pauseGame.addActionListener(e -> {
            loop.pauseAndUnpauseGame();
            repaint();
        });

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        setupKeyBindings(inputMap, actionMap, this);

        loop.start();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(RIGHT + LEFT, BOTTOM + TOP);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if(toolbar != null)
            toolbar.setBackground(bg);
    }

    public static int getBoardRows() {
        return 16;
    }

    public static int getBoardCols() {
        return 9;
    }

    public GameLoop getGameLoop() {
        return loop;
    }
}
