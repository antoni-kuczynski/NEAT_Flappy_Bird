package com.antekk.flappybird.view;

import com.antekk.flappybird.game.player.FlappyBirdPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static com.antekk.flappybird.view.GamePanel.getBlockSizePx;

public class BestPlayersDialog extends JDialog {
    private final DefaultTableModel model = new DefaultTableModel();
    private final JTable playerList = new JTable(model);
    private final JScrollPane scrollPane = new JScrollPane(playerList);

    protected void reloadData() {
        model.setRowCount(0);
        int place = 1;
        for(FlappyBirdPlayer player : FlappyBirdPlayer.playerStats.getPlayers()) {
            model.addRow(new String[] {
                    String.valueOf(place),
                    player.name,
                    String.valueOf(player.score),
                    String.valueOf(player.hGap),
            });
            place++;
        }
    }

    protected BestPlayersDialog(GamePanel parent) {
        super(SwingUtilities.getWindowAncestor(parent));

        setTitle("Best players");
        setPreferredSize(new Dimension(GamePanel.getBoardCols() * getBlockSizePx() * 2, (int) (0.8 * GamePanel.getBoardRows() * getBlockSizePx())));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model.addColumn("Place");
        model.addColumn("Name");
        model.addColumn("Score");
        model.addColumn("Gap between lines");

        reloadData();

        JLabel title = new JLabel("Players list");
        title.setFont(title.getFont().deriveFont(28f));
        title.setBorder(new EmptyBorder(new Insets(10,0,10,0)));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> this.dispose());


        add(title, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(okButton, BorderLayout.PAGE_END);

        pack();
    }
}
