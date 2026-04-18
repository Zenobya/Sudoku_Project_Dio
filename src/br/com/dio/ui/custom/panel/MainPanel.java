package br.com.dio.ui.custom.panel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class MainPanel extends JPanel {

    private static final Color BG = new Color(0, 0, 0);

    public MainPanel(final Dimension dimension) {
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setBackground(BG);
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 6));
    }

}
