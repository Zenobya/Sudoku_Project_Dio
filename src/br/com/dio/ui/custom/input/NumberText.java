package br.com.dio.ui.custom.input;

import br.com.dio.model.Space;
import br.com.dio.service.EventEnum;
import br.com.dio.service.EventListener;
import br.com.dio.ui.custom.screen.RoundedBorder;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;

public class NumberText extends JTextField implements EventListener {

    private static final Color BG_NORMAL   = new Color(18, 18, 18);
    private static final Color BG_FIXED    = new Color(30, 60, 114);
    private static final Color BG_FOCUS    = new Color(25, 25, 25);
    private static final Color FG_EDITABLE = new Color(255, 215, 0);
    private static final Color FG_FIXED    = new Color(220, 220, 220);
    private static final Color BORDER_CELL = new Color(40, 40, 40);
    private static final Color BORDER_FOCUS= new Color(100, 180, 255);
    private static final Color BG_ERROR = new Color(60, 0, 0);
    private static final Color FG_ERROR = new Color(255, 120, 120);
    private static final Color BORDER_ERROR = new Color(255, 80, 80);

    private final Space space;

    public NumberText(final Space space) {
        this.space = space;
        var dimension = new Dimension(50, 50);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setVisible(true);
        this.setHorizontalAlignment(CENTER);
        this.setDocument(new NumberTextLimit());
        this.setEditable(!space.isFixed());

        applyStyle(false);

        if (space.isFixed()) {
            this.setText(space.getActual().toString());
        }

        this.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { applyStyle(true); }
            @Override public void focusLost(FocusEvent e)   { applyStyle(false); }
        });
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!space.isFixed()) {
                    setBackground(new Color(30, 30, 30));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                applyStyle(hasFocus());
            }
        });

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e)  { changeSpace(); }
            @Override public void removeUpdate(DocumentEvent e)  { changeSpace(); }
            @Override public void changedUpdate(DocumentEvent e) { changeSpace(); }

            private void changeSpace() {
                if (getText().isEmpty()) {
                    space.clearSpace();
                    applyStyle(hasFocus());
                    return;
                }

                space.setActual(Integer.parseInt(getText()));

                applyStyle(hasFocus());
            }
        });
    }

    private void applyStyle(boolean focused) {
        boolean fixed = space.isFixed();
        boolean error = !fixed && space.getActual() != null && !space.isValid();

        Color bg;
        Color fg;
        Color border;

        if (error) {
            bg = BG_ERROR;
            fg = FG_ERROR;
            border = BORDER_ERROR;
        } else {
            bg     = focused ? BG_FOCUS : (fixed ? BG_FIXED : BG_NORMAL);
            fg     = fixed ? FG_FIXED : FG_EDITABLE;
            border = focused ? BORDER_FOCUS : BORDER_CELL;
        }

        int size = fixed ? 22 : 20;

        this.setBackground(bg);
        this.setForeground(fg);
        this.setCaretColor(FG_EDITABLE);
        this.setFont(new Font("Segoe UI", Font.BOLD, size));
        this.setBorder(new RoundedBorder(border, 12));
        this.setDisabledTextColor(FG_FIXED);
    }

    @Override
    public void update(final EventEnum eventType) {
        if (eventType.equals(CLEAR_SPACE) && !space.isFixed()) {
            this.setText("");
        }
    }
}
