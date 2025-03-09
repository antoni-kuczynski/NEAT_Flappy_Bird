package com.antekk.flappybird.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ErrorDialog extends JDialog {

    public ErrorDialog(String text, Exception e) {
        Dimension dimension = new Dimension(9 * GamePanel.getBlockSizePx(), 4 * GamePanel.getBlockSizePx());
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setTitle("Error");


        JLabel basicText = new JLabel("<html>" + text + "</html>");
        basicText.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel panel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel stackTracePanel = new JPanel(new BorderLayout());

        JButton ok = new JButton("OK");
        JButton viewStackTraceButton = new JButton("View stack trace");
        JTextArea stackTraceArea = new JTextArea();
        JScrollPane stackTraceScrollPane = new JScrollPane(stackTraceArea);

        stackTraceArea.setText(getErrorDialogMessage(e));
        stackTraceArea.setEditable(false);
        stackTraceArea.setLineWrap(true);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPanel.add(ok, BorderLayout.LINE_END);
        buttonPanel.add(viewStackTraceButton, BorderLayout.LINE_START);

        stackTracePanel.add(Box.createRigidArea(new Dimension(10,10)), BorderLayout.LINE_START);
        stackTracePanel.add(stackTraceScrollPane, BorderLayout.CENTER);
        stackTracePanel.add(Box.createRigidArea(new Dimension(10,10)), BorderLayout.LINE_END);
        stackTracePanel.setVisible(false);

        panel.add(basicText, BorderLayout.PAGE_START);
        panel.add(stackTracePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.PAGE_END);

        add(panel);

        getRootPane().setDefaultButton(ok);
        SwingUtilities.invokeLater(ok::requestFocusInWindow);
        System.err.println(stackTraceArea.getText());

        viewStackTraceButton.addActionListener(e1 -> {
            if(!stackTracePanel.isVisible()) {
                stackTracePanel.setVisible(true);
                this.setSize(getWidth() + 100, getHeight() + 300);
            } else {
                stackTracePanel.setVisible(false);
                this.setSize(getWidth() - 100, getHeight() - 300);
            }
        });

        ok.addActionListener(e1 -> {
            dispose();
//            Window.getTaskbar().setWindowProgressState(Window.getWindow(), Taskbar.State.OFF);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                Window.getTaskbar().setWindowProgressState(Window.getWindow(), Taskbar.State.OFF);
            }

            @Override
            public void windowClosed(WindowEvent e) {
//                Window.getTaskbar().setWindowProgressState(Window.getWindow(), Taskbar.State.OFF);
            }
        });

        setVisible(true);
        pack();
    }

    private static String getErrorDialogMessage(Exception e) {
        Toolkit.getDefaultToolkit().beep();
//        taskbar.setWindowProgressState(Window.getWindow(), Taskbar.State.ERROR);
//        taskbar.setWindowProgressValue(Window.getWindow(), 100);
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append(e).append("\n");
        errorMessage.append("Caused by:\n");
        StackTraceElement[] errorStackTrace = e.getStackTrace();
        for (StackTraceElement element : errorStackTrace) {
            errorMessage.append(element.toString());
            errorMessage.append("\n");
        }
        return errorMessage.toString();
    }
}
