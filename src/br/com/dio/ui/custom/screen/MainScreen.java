package br.com.dio.ui.custom.screen;

import br.com.dio.model.Space;
import br.com.dio.service.BoardService;
import br.com.dio.service.NotifierService;
import br.com.dio.ui.custom.button.CheckGameStatusButton;
import br.com.dio.ui.custom.button.FinishGameButton;
import br.com.dio.ui.custom.button.ResetButton;
import br.com.dio.ui.custom.frame.MainFrame;
import br.com.dio.ui.custom.input.NumberText;
import br.com.dio.ui.custom.panel.MainPanel;
import br.com.dio.ui.custom.panel.SudokuSector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.JOptionPane.showMessageDialog;

public class MainScreen {

    private static final Dimension SCREEN_DIM  = new Dimension(550, 580);
    private static final Color BG              = new Color(0, 0, 0);

    private final BoardService boardService;
    private final NotifierService notifierService;

    private JButton checkGameStatusButton;
    private JButton finishGameButton;
    private JButton resetButton;
    private JLabel statusLabel;

    public MainScreen(final Map<String, String> gameConfig) {
        this.boardService = new BoardService(gameConfig);
        this.notifierService = new NotifierService();
    }

    public void buildMainScreen() {
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
        rootPanel.setBackground(BG);
        rootPanel.setPreferredSize(SCREEN_DIM);

        rootPanel.add(Box.createVerticalStrut(16));
        rootPanel.add(buildBoardPanel());
        rootPanel.add(Box.createVerticalStrut(10));
        rootPanel.add(buildButtonPanel());
        rootPanel.add(Box.createVerticalStrut(30));

        JFrame mainFrame = new MainFrame(SCREEN_DIM, rootPanel);
    }

    private JPanel buildBoardPanel() {
        MainPanel boardPanel = new MainPanel(new Dimension(528, 528));

        for (int r = 0; r < 9; r += 3)
            for (int c = 0; c < 9; c += 3) {
                var spaces = getSpacesFromSector(boardService.getSpaces(), c, c + 2, r, r + 2);
                boardPanel.add(generateSection(spaces));
            }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setBackground(BG);
        wrapper.add(boardPanel);
        return wrapper;
    }

    private JPanel buildButtonPanel() {
        addResetButton();
        addCheckGameStatusButton();
        addFinishGameButton();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        panel.setBackground(BG);
        panel.add(resetButton);
        panel.add(checkGameStatusButton);
        panel.add(finishGameButton);
        return panel;
    }

    private List<Space> getSpacesFromSector(final List<List<Space>> spaces,
                                            final int initCol, final int endCol,
                                            final int initRow, final int endRow) {
        List<Space> spaceSector = new ArrayList<>();
        for (int r = initRow; r <= endRow; r++)
            for (int c = initCol; c <= endCol; c++)
                spaceSector.add(spaces.get(c).get(r));
        return spaceSector;
    }

    private JPanel generateSection(final List<Space> spaces) {
        List<NumberText> fields = new ArrayList<>(spaces.stream().map(NumberText::new).toList());
        fields.forEach(t -> notifierService.subscribe(CLEAR_SPACE, t));
        return new SudokuSector(fields);
    }

    private void addFinishGameButton() {
        finishGameButton = new FinishGameButton(e -> {
            if (boardService.gameIsFinished()) {
                setStatus("Parabens! Voce concluiu o jogo!");
                showMessageDialog(null, "Parabéns, você concluiu o jogo!");
                resetButton.setEnabled(false);
                checkGameStatusButton.setEnabled(false);
                finishGameButton.setEnabled(false);
            } else {
                setStatus("Ha inconsistências no tabuleiro. Ajuste e tente novamente.");
                showMessageDialog(null, "Seu jogo tem alguma inconsistência, ajuste e tente novamente");
            }
        });
    }

    private void addCheckGameStatusButton() {
        checkGameStatusButton = new CheckGameStatusButton(e -> {
            var hasErrors  = boardService.hasErrors();
            var gameStatus = boardService.getStatus();
            var message = switch (gameStatus) {
                case NON_STARTED -> "O jogo não foi iniciado";
                case INCOMPLETE  -> "O jogo está incompleto";
                case COMPLETE    -> "O jogo está completo";
            };
            message += hasErrors ? " e contém erros." : " e não contém erros.";
            setStatus(message);
            showMessageDialog(null, message);
        });
    }

    private void addResetButton() {
        resetButton = new ResetButton(e -> {
            var result = showConfirmDialog(
                null,
                "Deseja realmente reiniciar o jogo?",
                "Reiniciar",
                YES_NO_OPTION,
                QUESTION_MESSAGE
            );
            if (result == 0) {
                boardService.reset();
                notifierService.notify(CLEAR_SPACE);
                setStatus("Jogo reiniciado. Boa sorte!");
            }
        });
    }

    private void setStatus(String message) {
        if (statusLabel != null) statusLabel.setText(message);
    }
}
