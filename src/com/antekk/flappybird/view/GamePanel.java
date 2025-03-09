package com.antekk.flappybird.view;

import com.antekk.flappybird.game.bird.Bird;
import com.antekk.flappybird.game.loop.GameLoop;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.themes.GameColors;
import com.antekk.flappybird.view.themes.Theme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.antekk.flappybird.game.keybinds.GameKeybinds.setupKeyBindings;

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
    private final Bird bird = new Bird();
    private final ArrayList<PipeFormation> pipes = new ArrayList<>();
    private final ScoreDisplay scoreDisplay;


    private MouseAdapter gameMouseListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                loop.startGame();
                bird.flap();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                loop.startGame();
                bird.flap();
            }
        }
    };

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

        //660
        g.drawImage(GameColors.background, LEFT, TOP, backgroundWidth, GROUND, null);
        g.drawImage(GameColors.background, LEFT + backgroundWidth, TOP, backgroundWidth, GROUND, null);

        for(int i = groundX; i <= RIGHT; i+= 9 * getBlockSizePx()) {
            g.drawImage(GameColors.ground, i, GROUND + TOP, 9 * getBlockSizePx(), 3 * getBlockSizePx(), null);

        }

        for(PipeFormation pipe : pipes)
            pipe.draw(g);

        bird.draw(g);

        if(loop.getGameState() == GameState.STARTING) {
            g.drawImage(GameColors.startingMessage, (int) (3.5 * getBlockSizePx()), 2 * getBlockSizePx(), 5 * getBlockSizePx(), (int) (1.45 * 300), null);
            return;
        }


        scoreDisplay.draw(g);

        if(loop.getGameState() == GameState.LOST) {
            g.drawImage(GameColors.gameOver, 3 * getBlockSizePx(), 3 * getBlockSizePx(), 6 * getBlockSizePx(), (int) (0.219 * 6 * getBlockSizePx()), null);
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
        RIGHT = getBoardCols() * getBlockSizePx();
        BOTTOM =  getBoardRows() * getBlockSizePx();
        GROUND = BOTTOM - 3 * getBlockSizePx();
        backgroundWidth = (int) (0.5625 * GROUND);
        groundX = LEFT;
        scoreDisplay = new ScoreDisplay(this);
        parent.setPreferredSize(this.getPreferredSize());
        GameColors.setTheme(Theme.LIGHT);


        setLayout(new BorderLayout());
        setDoubleBuffered(true);
//        setBackground(GameColors.boardColor);
        Toolkit.getDefaultToolkit().setDynamicLayout(true);

        JButton newGame = new JButton("New game");
        JButton pauseGame = new JButton("Pause game");
        JButton showBestPlayers = new JButton("Best players");
        JButton options = new JButton("Options");

        newGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        pauseGame.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        showBestPlayers.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));
        options.setPreferredSize(new Dimension(3 * getBlockSizePx(), (int) (0.65 * getBlockSizePx())));

        BoxLayout layout = new BoxLayout(toolbar, BoxLayout.X_AXIS);
        toolbar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(233, 252, 217)));
        toolbar.setBackground(GameColors.groundColor);
        toolbar.setPreferredSize(new Dimension(0, getBlockSizePx()));
        toolbar.setLayout(layout);
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
            bird.resetPosition();
            loop = new GameLoop(this);
            loop.start();
            pipes.clear();
            repaint();
        });

        pauseGame.addActionListener(e -> {
            loop.pauseAndUnpauseGame();
            repaint();
        });

        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        setupKeyBindings(inputMap, actionMap, this);

        addMouseListener(gameMouseListener);

        repaint();
        loop.start();
        parent.setMinimumSize(this.getPreferredSize());
        parent.setPreferredSize(this.getPreferredSize());
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

    public Bird getBird() {
        return bird;
    }

    public ArrayList<PipeFormation> getPipes() {
        return pipes;
    }
}
