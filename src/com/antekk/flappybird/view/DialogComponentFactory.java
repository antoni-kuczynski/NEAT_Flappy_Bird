package com.antekk.flappybird.view;

import com.antekk.flappybird.game.ConfigJSON;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public final class DialogComponentFactory {

    static JButton getSaveNetworkToJSONButton(GamePanel parent) {
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
        saveNetworkToJSON.setTransferHandler(getButtonTransferHandler(fileName -> {
            if(fileName == null || fileName.isBlank())
                return;
            parent.getGameLoop().getSmartestBrain().saveToJSON(fileName);
        }));

        return saveNetworkToJSON;
    }

    static JButton getLoadNetworkFromJSONButton(OptionsDialog optionsDialog) {
        JButton loadNetworkFromJSON = new JButton("Open");
        loadNetworkFromJSON.addActionListener(e -> {
            FileDialog dialog = new FileDialog(optionsDialog);
            dialog.setMode(FileDialog.LOAD);
            dialog.setTitle("Open player from JSON");
            dialog.setVisible(true);

            File file = dialog.getFiles()[0];

            optionsDialog.processLoadedNeuralNetworkFile(file.getAbsolutePath());
        });

        loadNetworkFromJSON.setTransferHandler(getButtonTransferHandler(optionsDialog::processLoadedNeuralNetworkFile));
        return loadNetworkFromJSON;
    }

    static TransferHandler getButtonTransferHandler(FileRunnable afterFileDrop) {
        return new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                Transferable t = support.getTransferable();
                try {
                    java.util.List<File> l = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    File fileToAdd = l.get(l.size() - 1);
                    afterFileDrop.afterFileDrop(fileToAdd.getAbsolutePath());
                } catch (UnsupportedFlavorException e) {
                    new ErrorDialog("Requested data not supported in this flavor.", e);
                    return false;
                } catch (IOException ee) {
                    new ErrorDialog("I/O error.", ee);
                }
                return true;
            }
        };
    }

    static SpinnerNumberModel getSpinnerNumberModel() {
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

    private interface FileRunnable {
        void afterFileDrop(String filename);
    }
}
