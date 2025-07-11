package com.antekk.flappybird.view;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.keybinds.GameKeybinds;
import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.displays.BirdsStatsDisplay;
import com.antekk.flappybird.view.displays.ScoreDisplay;
import com.antekk.flappybird.view.themes.GameColors;
import com.formdev.flatlaf.ui.FlatDropShadowBorder;
import com.formdev.flatlaf.ui.FlatLineBorder;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Iterator;

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
    private GameLoop loop;
    private final ScoreDisplay scoreDisplay;
    private final BestPlayersDialog bestPlayersDialog;
    public final BirdsStatsDisplay birdsStatsDisplay;
    public int birdsStatDisplayWidth = 16 * getBlockSizePx();

    @Override
    protected synchronized void paintComponent(Graphics g1) {
        super.paintComponent(g1);

        Graphics2D g = (Graphics2D)  g1;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(loop.getGameState() == GameState.PAUSED) {
            drawPauseScreen(g1);
            g.setColor(GameColors.borderColor);
            g.fillRect(RIGHT, TOP, getWidth(), BOTTOM);
            return;
        }


        drawBackgroundAndGround(g);
        for(Iterator<PipeFormation> it = loop.getPipes().iterator(); it.hasNext();)
            it.next().draw(g);

        g.setColor(GameColors.borderColor);
        g.fillRect(RIGHT, TOP, getWidth(), BOTTOM);
        g.setColor(Color.BLACK);
        birdsStatsDisplay.draw(g);

        if(loop.getGameState() == GameState.STARTING) {
            g.drawImage(GameColors.startingMessage, (int) (3.5 * getBlockSizePx()), 2 * getBlockSizePx(), 5 * getBlockSizePx(), (int) (1.45 * 5 * getBlockSizePx()), null);
//            loop.getBirds().drawWithoutRotation(g);
            for(Bird bird : loop.getGameMode().getBirds())
                bird.drawWithoutRotation(g);
            return;
        }

        for(Bird bird : loop.getGameMode().getBirds())
            bird.draw(g);

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
        loop = new GameLoop(this);
        loop.setGameMode(ConfigJSON.getGameMode());

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
        toolbar.setBorder(new MatteBorder(0, 0, 2, 0, GameColors.borderColor));
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
        scoreDisplay = new ScoreDisplay(this);
        bestPlayersDialog = new BestPlayersDialog(this);

        setLayout(new BorderLayout());
        setDoubleBuffered(true);
//        setBackground(GameColors.boardColor);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

//        loop = new GameLoop(this);
        birdsStatsDisplay = new BirdsStatsDisplay(this);

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        GameKeybinds keybinds = new GameKeybinds(this);
        keybinds.setupKeyBindings(inputMap, actionMap);
        addMouseListener(keybinds);

        newGame.addActionListener(e -> {
            loop.endGame();
            loop = new GameLoop(this);
            loop.start();
            keybinds.setGameLoop(loop);
            repaint();
        });

        pauseGame.addActionListener(e -> {
            loop.pauseAndUnpauseGame();
            repaint();
        });

        showBestPlayers.addActionListener(e -> showBestPlayersDialog(!bestPlayersDialog.isVisible()));

        options.addActionListener(e -> new OptionsDialog(GamePanel.this).setVisible(true));

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
        return new Dimension(LEFT + RIGHT + birdsStatsDisplay.getPreferredSize().width, BOTTOM + TOP);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        SwingUtilities.getWindowAncestor(this).setPreferredSize(preferredSize);
        SwingUtilities.getWindowAncestor(this).setSize(preferredSize);
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

    public static void setBlockSizePx(int blockSizePx) {
        GamePanel.blockSizePx = blockSizePx;
    }

    public static int getBlockSizePx() {
        return blockSizePx;
    }
}
