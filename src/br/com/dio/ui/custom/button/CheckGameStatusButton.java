package br.com.dio.ui.custom.button;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckGameStatusButton extends JButton {

    private static final Color NEON      = new Color(0, 255, 153);
    private static final Color NEON_DIM  = new Color(0, 180, 110);
    private static final Color BG_NORMAL = new Color(0, 20, 12);
    private static final Color BG_HOVER  = new Color(0, 40, 24);
    private static final Color GLOW      = new Color(0, 255, 153, 55);

    private boolean hovered = false;

    public CheckGameStatusButton(final ActionListener actionListener) {
        super("Verificar");
        this.addActionListener(actionListener);
        this.setPreferredSize(new Dimension(120, 25));
        this.setFont(new Font("Courier New", Font.BOLD, 13));
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight(), arc = 5;
        if (hovered) { g2.setColor(GLOW); g2.fillRoundRect(-3, -3, w+6, h+6, arc+10, arc+10); }
        g2.setColor(hovered ? BG_HOVER : BG_NORMAL);
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        g2.setColor(hovered ? NEON : NEON_DIM);
        g2.setStroke(new BasicStroke(hovered ? 1.8f : 1.2f));
        g2.drawRoundRect(1, 1, w-2, h-2, arc, arc);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2, (h + fm.getAscent() - fm.getDescent()) / 2);
        g2.dispose();
    }
}
