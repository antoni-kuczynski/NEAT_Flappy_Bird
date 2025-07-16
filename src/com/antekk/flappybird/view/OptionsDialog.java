package com.antekk.flappybird.view;

import com.antekk.flappybird.game.ConfigJSON;
import com.antekk.flappybird.game.ai.NeuralNetwork;
import com.antekk.flappybird.game.bird.gamemodes.GameMode;
import com.antekk.flappybird.game.bird.gamemodes.MlPretrainedMode;
import com.antekk.flappybird.game.loop.GameState;
import com.antekk.flappybird.game.pipes.PipeFormation;
import com.antekk.flappybird.view.themes.GameColors;
import com.antekk.flappybird.view.themes.Theme;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;
import static com.antekk.flappybird.view.GamePanel.setBlockSizePx;

public class OptionsDialog extends JDialog {
    private String loadedNeuralNetworkPath = "";
    private final JSpinner pipesGap = new JSpinner();
    private final JSpinner sizeSpinner = new JSpinner();
    private final JComboBox<GameMode> gameModeSwitcher = new JComboBox<>();
    private final JButton openNetworkButton;
    private final JButton saveNetworkButton;

    protected OptionsDialog(GamePanel parent) {
        super(SwingUtilities.getWindowAncestor(parent));
        setTitle("Options");
        setPreferredSize(new Dimension((int) (GamePanel.getBoardCols() * getBlockSizePx() * 0.75),
                (int) (0.6 * GamePanel.getBoardRows() * getBlockSizePx())));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        openNetworkButton = getLoadNetworkFromJSONButton();
        saveNetworkButton = getSaveNetworkToJSONButton(parent);

        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel generalOptions = new JPanel();
        BoxLayout layout = new BoxLayout(generalOptions, BoxLayout.Y_AXIS);
        generalOptions.setLayout(layout);

        DefaultComboBoxModel<Theme> model = new DefaultComboBoxModel<>();
        JComboBox<Theme> themeSelection = new JComboBox<>(model);
        for(Theme theme : Theme.values())
            model.addElement(theme);
        model.setSelectedItem(ConfigJSON.getTheme());


        SpinnerNumberModel pipesGapModel = getSpinnerNumberModel();
        pipesGap.setModel(pipesGapModel);
        pipesGap.setPreferredSize(new Dimension(80,25));

        JPanel setPipesGap = new JPanel();
        setPipesGap.add(new JLabel("Vertical gap between pipes: "));
        setPipesGap.add(pipesGap);

        generalOptions.add(setPipesGap);

        JPanel theme = new JPanel();
        theme.add(new JLabel("Time of day: "));
        theme.add(themeSelection);
        themeSelection.setSelectedItem(ConfigJSON.getTheme());
        themeSelection.addItemListener(e -> {
            GameColors.setTheme((Theme) e.getItem());
            parent.repaint();
        });

        generalOptions.add(theme);

        JPanel blockSize = new JPanel();
        blockSize.add(new JLabel("Block size (px): "));

        int minBlockSize = 35;
        int currentBlockSizeModel = ConfigJSON.getBlockSize();
        if(currentBlockSizeModel < minBlockSize) {
            currentBlockSizeModel = minBlockSize;
        }
        SpinnerNumberModel blockSizeModel = new SpinnerNumberModel(
                currentBlockSizeModel,
                minBlockSize,
                Integer.MAX_VALUE,
                5
        );

        sizeSpinner.setPreferredSize(new Dimension(80,25));
        blockSize.add(sizeSpinner);
        sizeSpinner.setModel(blockSizeModel);
        generalOptions.add(blockSize);

        JPanel showNewBestDialogPanel = new JPanel();
        JCheckBox showNewBestDialogBox = new JCheckBox();
        showNewBestDialogPanel.add(new JLabel("Show new best dialog: "));
        showNewBestDialogPanel.add(showNewBestDialogBox);
        showNewBestDialogBox.setSelected(ConfigJSON.showNewBestDialog());
        generalOptions.add(showNewBestDialogPanel);

        JPanel machineLearningOptions = new JPanel();
        BoxLayout layout1 = new BoxLayout(machineLearningOptions, BoxLayout.Y_AXIS);
        machineLearningOptions.setLayout(layout1);

        JPanel gameModeSelection = new JPanel();
        DefaultComboBoxModel<GameMode> gameModeModel = new DefaultComboBoxModel<>();
        gameModeSwitcher.setModel(gameModeModel);
        gameModeModel.addAll(GameMode.values());
        gameModeSelection.add(new JLabel("Game mode: "));
        gameModeSelection.add(gameModeSwitcher);
        gameModeModel.setSelectedItem(ConfigJSON.getGameMode());
        machineLearningOptions.add(gameModeSelection);

        JPanel saveNetworkPanel = new JPanel();
        saveNetworkPanel.add(new JLabel("<html>Save best player to JSON:<br><center><p style=\"font-size:9px\">(from current generation)</p></center></html>"));
        saveNetworkPanel.add(saveNetworkButton);
        machineLearningOptions.add(saveNetworkPanel);

        JPanel openNetworkPanel = new JPanel();
        openNetworkPanel.add(new JLabel("Load player from JSON:"));
        openNetworkPanel.add(openNetworkButton);
        machineLearningOptions.add(openNetworkPanel);

        JPanel bottomButtons = new JPanel();
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

            GameMode gameModeToSave = getGameModeBasedOnUserSelection(gameModeSwitcher);



            parent.getGameLoop().setGameMode(gameModeToSave);

            ConfigJSON.saveValues((Integer) pipesGap.getValue(), (Theme) themeSelection.getSelectedItem(), newBlockSize, showNewBestDialogBox.isSelected(),
                gameModeToSave, loadedNeuralNetworkPath
            );
            this.dispose();
            parent.setPreferredSize(parent.getPreferredSize());
            parent.repaint();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispose());

        bottomButtons.add(okButton);
        bottomButtons.add(Box.createRigidArea(new Dimension(getBlockSizePx(), 1)));
        bottomButtons.add(cancelButton);

        mainTabbedPane.addTab("General", generalOptions);
        mainTabbedPane.addTab("Machine learning", machineLearningOptions);
        add(mainTabbedPane, BorderLayout.PAGE_START);
        add(bottomButtons, BorderLayout.PAGE_END);

        pack();


        if(parent.getGameLoop().getGameState() == GameState.RUNNING || parent.getGameLoop().getGameState() == GameState.PAUSED) {
            setOptionsEnabled(false);
        }

        if(!parent.getGameLoop().getGameMode().isTrainingMode()) {
            setSaveNetworkButtonEnabled(false);
        }
    }

    public void setSaveNetworkButtonEnabled(boolean enable) {
        saveNetworkButton.setEnabled(enable);
        saveNetworkButton.setToolTipText(enable ? null : "Can only be used in training mode & game must be running");
    }

    public void setOptionsEnabled(boolean enabled) {
        pipesGap.setEnabled(enabled);
        sizeSpinner.setEnabled(enabled);
        gameModeSwitcher.setEnabled(enabled);
        openNetworkButton.setEnabled(enabled);


        pipesGap.setToolTipText(enabled ? null : "Can't change while in-game");
        sizeSpinner.setToolTipText(enabled ? null : "Can't change while in-game");
        gameModeSwitcher.setToolTipText(enabled ? null : "Can't change while in-game");
        openNetworkButton.setToolTipText(enabled ? null : "Can't use while in-game");
    }

    private JButton getLoadNetworkFromJSONButton() {
        JButton loadNetworkFromJSON = new JButton("Open");
        loadNetworkFromJSON.addActionListener(e -> {
            JFileChooser dialog = new JFileChooser();
            dialog.setDialogType(JFileChooser.OPEN_DIALOG);
            dialog.setDialogTitle("Open player from JSON");

            dialog.showOpenDialog(this);
            File file = dialog.getSelectedFile();

            if(file == null || file.getName().isBlank())
                return;

            loadedNeuralNetworkPath = file.getAbsolutePath();
        });
        return loadNetworkFromJSON;
    }

    private static JButton getSaveNetworkToJSONButton(GamePanel parent) {
        JButton saveNetworkToJSON = new JButton("Save");
        saveNetworkToJSON.addActionListener(e -> {
            if(!parent.getGameLoop().getGameMode().isTrainingMode())
                return;

            FileDialog dialog = new FileDialog((Frame) null);
            dialog.setMode(FileDialog.SAVE);
            dialog.setTitle("Save best player to JSON");
            dialog.setFile("*.json");

            dialog.setVisible(true);
            String fileName = dialog.getFile();

            if(fileName == null || fileName.isBlank())
                return;

            parent.getGameLoop().getSmartestBrain().saveToJSON(fileName);

        });
        return saveNetworkToJSON;
    }

    private GameMode getGameModeBasedOnUserSelection(JComboBox<GameMode> gameModeSwitcher) {
        GameMode gameMode = (GameMode) gameModeSwitcher.getSelectedItem();
        if(gameMode != null && gameMode.isPretrainedMode() && !loadedNeuralNetworkPath.isBlank()) {
            ((MlPretrainedMode) gameMode).setBirdsNeuralNetwork(NeuralNetwork.getFromJSON(loadedNeuralNetworkPath));
        }
        return gameMode;
    }

    private static SpinnerNumberModel getSpinnerNumberModel() {
        int currentPipesGap = ConfigJSON.getPipesVGap();
        if(currentPipesGap < 1.5 * getBlockSizePx() ||
                currentPipesGap > GamePanel.getBoardRows() / 2 * getBlockSizePx()) {
            currentPipesGap = 3 * getBlockSizePx();
        }

        return new SpinnerNumberModel(
                currentPipesGap,
                (int) (1.5 * getBlockSizePx()),
                GamePanel.getBoardRows() / 2 * getBlockSizePx(),
                10
        );
    }
}
