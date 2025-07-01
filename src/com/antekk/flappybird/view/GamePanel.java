package com.antekk.flappybird.view;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.bird.Birds;
import com.antekk.flappybird.game.keybinds.GameKeybinds;
import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.themes.GameColors;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    public static int LEFT;
    public static int TOP;
    public static int RIGHT;
    public static int BOTTOM;
    public static int GROUND;
    private static int backgroundWidth;
    public static int groundX;
    private static int blockSizePx = 60;
    private final JPanel toolbar = new JPanel();
    private GameLoop loop = new GameLoop(this);
    private final Birds birds;
    private final ArrayList<PipeFormation> pipes = new ArrayList<>();
    private final ScoreDisplay scoreDisplay;
    private final BestPlayersDialog bestPlayersDialog;

    public static int getBlockSizePx() {
        return blockSizePx;
    }

    @Override
    protected synchronized void paintComponent(Graphics g1) {
        super.paintComponent(g1);

        Graphics2D g = (Graphics2D)  g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(loop.getGameState() == GameState.PAUSED) {
            drawPauseScreen(g1);
            return;
        }

        drawBackgroundAndGround(g);

        for(PipeFormation pipe : pipes)
            pipe.draw(g);



        if(loop.getGameState() == GameState.STARTING) {
            g.drawImage(GameColors.startingMessage, (int) (3.5 * getBlockSizePx()), 2 * getBlockSizePx(), 5 * getBlockSizePx(), (int) (1.45 * 5 * getBlockSizePx()), null);
            birds.drawWithoutRotation(g);
            return;
        }
        birds.draw(g);
        scoreDisplay.draw(g);

        if(loop.getGameState() == GameState.LOST) {
            g.drawImage(GameColors.gameOver, 3 * getBlockSizePx(), 2 * getBlockSizePx(), 6 * getBlockSizePx(), (int) (0.219 * 6 * getBlockSizePx()), null);
        }
    }

    private void drawBackgroundAndGround(Graphics g) {
        g.drawImage(GameColors.background, LEFT, TOP, backgroundWidth, GROUND, null);
        g.drawImage(GameColors.background, LEFT + backgroundWidth, TOP, backgroundWidth, GROUND, null);

        for(int i = groundX; i <= RIGHT; i+= 9 * getBlockSizePx()) {
            g.drawImage(GameColors.ground, i, GROUND + TOP, 9 * getBlockSizePx(), 3 * getBlockSizePx(), null);

        }
        toolbar.setBackground(GameColors.groundColor);
    }


    private void drawPauseScreen(Graphics g) {
        drawBackgroundAndGround(g);

        g.setColor(GameColors.foregroundColor);
        g.setFont(g.getFont().deriveFont((float) getBlockSizePx()));
        g.drawString("Game paused", getBlockSizePx() * 2, getBlockSizePx() * 4);
    }

    protected GamePanel(JFrame parent) {
        ConfigJSON.initializeValuesFromConfigFile();

        JButton newGame = new JButton("New game");
        JButton pauseGame = new JButton("Pause game");
        JButton showBestPlayers = new JButton("Best players");
        JButton options = new JButton("Options");

        newGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        pauseGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        showBestPlayers.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        options.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));



        newGame.setFocusable(false);
        pauseGame.setFocusable(false);
        showBestPlayers.setFocusable(false);
        options.setFocusable(false);

        BoxLayout layout = new BoxLayout(toolbar, BoxLayout.X_AXIS);
        toolbar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(233, 252, 217)));
        toolbar.setBackground(GameColors.groundColor);
        toolbar.setLayout(layout);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(newGame);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(pauseGame);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(showBestPlayers);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(options);

        LEFT = 0;
        TOP = toolbar.getPreferredSize().height;
        RIGHT = getBoardCols() * getBlockSizePx();
        BOTTOM =  getBoardRows() * getBlockSizePx();
        GROUND = BOTTOM - 3 * getBlockSizePx();
        backgroundWidth = (int) (0.5625 * GROUND);
        groundX = LEFT;
        birds = new Birds(10);
        scoreDisplay = new ScoreDisplay(this);
        bestPlayersDialog = new BestPlayersDialog(this);
        parent.setPreferredSize(this.getPreferredSize());

        setLayout(new BorderLayout());
        setDoubleBuffered(true);
//        setBackground(GameColors.boardColor);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);



        newGame.addActionListener(e -> {
            loop.endGame();
            loop = new GameLoop(this);
            loop.start();
            birds.resetPosition();
            pipes.clear();
            repaint();
        });

        pauseGame.addActionListener(e -> {
            loop.pauseAndUnpauseGame();
            repaint();
        });

        showBestPlayers.addActionListener(e -> showBestPlayersDialog(!bestPlayersDialog.isVisible()));

        options.addActionListener(e -> new OptionsDialog(GamePanel.this).setVisible(true));

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        GameKeybinds keybinds = new GameKeybinds(this);
        keybinds.setupKeyBindings(inputMap, actionMap);
        addMouseListener(keybinds);

        add(toolbar, BorderLayout.PAGE_START);

        loop.start();
        parent.setMinimumSize(this.getPreferredSize());
        parent.setPreferredSize(this.getPreferredSize());
        repaint();
    }

    public void showBestPlayersDialog(boolean show) {
        if(!show) {
            bestPlayersDialog.dispose();
            return;
        }

        if(getGameLoop().getGameState() == GameState.RUNNING) {
            getGameLoop().pauseAndUnpauseGame();
        }
        repaint();

        bestPlayersDialog.reloadData();
        bestPlayersDialog.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(LEFT + RIGHT, BOTTOM + TOP);
    }

    public static int getBoardRows() {
        return 16;
    }

    public static int getBoardCols() {
        return 12;
    }

    public GameLoop getGameLoop() {
        return loop;
    }

    public Birds getBirds() {
        return birds;
    }

    public ArrayList<PipeFormation> getPipes() {
        return pipes;
    }

    public static void setBlockSizePx(int blockSizePx) {
        GamePanel.blockSizePx = blockSizePx;
    }
}
