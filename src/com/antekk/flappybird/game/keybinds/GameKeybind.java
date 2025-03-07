package com.antekk.flappybird.game.keybinds;

import com.antekk.flappybird.view.GamePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

record GameKeybind(String name, int keyCode, Runnable action) {
    static GamePanel currentPanel = null;
    static InputMap inputMap;
    static ActionMap actionMap;

    void bindKeyPressed() {
        bind(0, false);
    }

    void bindKeyReleased() {
        bind(0, true);
    }


    private void bind(int modifiers, boolean onKeyRelease) {
        GameKeybind.inputMap.put(KeyStroke.getKeyStroke(keyCode(), modifiers, onKeyRelease), name());
        GameKeybind.actionMap.put(name(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action().run();
                currentPanel.paintImmediately(0, GamePanel.TOP, currentPanel.getWidth(), currentPanel.getHeight());
            }
        });
    }
}
