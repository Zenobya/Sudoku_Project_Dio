package br.com.dio.ui.custom.panel;

import br.com.dio.ui.custom.input.NumberText;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

public class SudokuSector extends JPanel {

    private static final Color BG          = new Color(10, 10, 10, 255);
    private static final Color BORDER_NEON = new Color(80, 160, 255);
    private static final Color BORDER_GLOW = new Color(80, 160, 255, 40);

    public SudokuSector(final List<NumberText> textFields) {
        var dimension = new Dimension(144, 144);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setBackground(BG);
        this.setLayout(new GridLayout(3, 3, 2, 2));
        this.setOpaque(true);
        this.setVisible(true);
        textFields.forEach(this::add);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight(), arc = 10;
        // halo externo
        g2.setColor(BORDER_GLOW);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRoundRect(2, 2, w - 4, h - 4, arc, arc);
        // borda neon
        g2.setColor(BORDER_NEON);
        g2.setStroke(new BasicStroke(1.4f));
        g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);
        g2.dispose();
    }
}
