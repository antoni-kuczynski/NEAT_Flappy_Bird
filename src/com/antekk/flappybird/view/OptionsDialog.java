package com.antekk.flappybird.view;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.themes.GameColors;
import com.antekk.flappybird.view.themes.Theme;

import javax.swing.*;
import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.setBlockSizePx;

public class OptionsDialog extends JDialog {

    protected OptionsDialog(GamePanel parent) {
        super(SwingUtilities.getWindowAncestor(parent));
        setTitle("Options");
        setPreferredSize(new Dimension(GamePanel.getBoardCols() * getBlockSizePx() * 2,
                (int) (0.8 * GamePanel.getBoardRows() * getBlockSizePx())));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel generalOptions = new JPanel();
        BoxLayout layout = new BoxLayout(generalOptions, BoxLayout.Y_AXIS);
        generalOptions.setLayout(layout);

        DefaultComboBoxModel<Theme> model = new DefaultComboBoxModel<>();
        JComboBox<Theme> themeSelection = new JComboBox<>(model);
        for(Theme theme : Theme.values())
            model.addElement(theme);
        model.setSelectedItem(ConfigJSON.getTheme());

        SpinnerNumberModel pipesGapModel = new SpinnerNumberModel(ConfigJSON.getPipesVGap(),parent.getBird().getHeight(),GamePanel.getBoardRows() * getBlockSizePx(),10);
        JSpinner pipesGap = new JSpinner(pipesGapModel);
        pipesGap.setPreferredSize(new Dimension(80,25));

        JPanel setPipesGap = new JPanel();
        setPipesGap.add(new JLabel("Vertical gap between pipes: "));
        setPipesGap.add(pipesGap);

        generalOptions.add(setPipesGap);

        JPanel theme = new JPanel();
        theme.add(new JLabel("Theme: "));
        theme.add(themeSelection);
        themeSelection.addItemListener(e -> {
            GameColors.setTheme((Theme) e.getItem());
            //TODO
            parent.repaint();
        });

        generalOptions.add(theme);

        JPanel blockSize = new JPanel();
        blockSize.add(new JLabel("Block size (px): "));
        SpinnerNumberModel blockSizeModel = new SpinnerNumberModel(ConfigJSON.getBlockSize(),35,Integer.MAX_VALUE,5);
        JSpinner sizeSpinner = new JSpinner(blockSizeModel);
        sizeSpinner.setPreferredSize(new Dimension(80,25));
        blockSize.add(sizeSpinner);

        generalOptions.add(blockSize);

        JPanel buttons = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            int newBlockSize = (int) blockSizeModel.getValue();
            int newPipesGap = (int) pipesGapModel.getValue();
            if(newBlockSize != getBlockSizePx()) {
                JOptionPane.showMessageDialog(
                        null,
                        "You need to restart the game for the block size setting to work properly!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                setBlockSizePx(newBlockSize);
            }

            if(newPipesGap != PipeFormation.futureGap &&
                    (parent.getGameLoop().getGameState() == GameState.RUNNING ||
                    parent.getGameLoop().getGameState() == GameState.PAUSED)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Pipes vertical gap setting will take effect after starting a new game.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            if(newPipesGap != PipeFormation.futureGap)
                PipeFormation.futureGap = newPipesGap;

            ConfigJSON.saveValues((Integer) pipesGap.getValue(), (Theme) themeSelection.getSelectedItem(), newBlockSize);
            this.dispose();
            parent.repaint();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());

        buttons.add(okButton);
        buttons.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 1)));
        buttons.add(cancelButton);

        tabbedPane.addTab("General", generalOptions);
        add(tabbedPane, BorderLayout.PAGE_START);
        add(buttons, BorderLayout.PAGE_END);

        pack();
    }
}
