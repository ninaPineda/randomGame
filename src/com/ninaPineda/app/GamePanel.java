package com.ninaPineda.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Locale;

public class GamePanel extends JPanel {

    private static final int PANEL_BORDER = 50;

    private final ImageIcon blumeEinsIcon;
    private final ImageIcon blumeZweiIcon;
    private final JLabel levelLabel, turnLabel, versucheLabel;
    private final JButton backButton, themeButton;
    private final Game game;
    private boolean clickable;
    private final MainFrame mainFrame;
    private static JPanel fieldPanel;
    private static JPanel guessPanel;
    private JButton[][] buttons;
    private JLabel[][] fields;
    private final ResourceBundle bundle;
    private final Image backgroundImage, backgroundImageDark;
    private final ImageIcon moonIcon, sunIcon;
    private static final Level level = Level.getInstance();
    private static final HighscoreManager manager = HighscoreManager.getInstance();

    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.game = Game.getInstance();
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

        this.moonIcon = loadIcon("/icons/halbmond.png", 15, 15);
        this.sunIcon = loadIcon("/icons/sonne.png", 15, 15);
        this.backgroundImage = loadImage("/icons/background.png");
        this.backgroundImageDark = loadImage("/icons/background_dark.png");

        this.blumeEinsIcon = loadIcon("/icons/blume.png", 25, 25);
        this.blumeZweiIcon = loadIcon("/icons/blume2.png", 25, 25);

        this.levelLabel = new JLabel();
        this.turnLabel = new JLabel();
        this.versucheLabel = new JLabel();
        this.backButton = new JButton();
        this.themeButton = new JButton();

        this.clickable = true;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 0, 20, 0));
        startNewGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (FlatLaf.isLafDark()) {
            g.drawImage(backgroundImageDark, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void win() {
        clickable = false;
        WinPanel confettiPanel = new WinPanel(1000);
        this.add(confettiPanel, 0);
        mainFrame.setScore(level.getLevel());
        repaint();
        revalidate();

        createTimer(1000, e -> {
            level.levelUp();
            clickable = true;
            startNewGame();
            repaint();
            revalidate();
        }).start();
    }

    private void lost() {
        manager.addHighscore(mainFrame.getName(), level.getLevel());
        int response = showConfirmDialog(bundle.getString("dialog.loose"), bundle.getString("dialog.loose.name"));

        if (response == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            game.reset();
            showMessage(bundle.getString("dialog.thanks"));
            mainFrame.showPanel("Start");
        }
    }

    private void startNewGame() {
        game.reset();
        renderView();
        configureThemeButton();
    }

    private void configureThemeButton() {
        for (ActionListener al : themeButton.getActionListeners()) {
            themeButton.removeActionListener(al);
        }
    
        themeButton.addActionListener(e -> toggleTheme());
    }

    private void renderView() {
        removeAll();
        add(createButtonPanel());
        showOriginalGrid();
        showGuessGrid();
        repaint();
        revalidate();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
    
        backButton.setText(bundle.getString("button.back"));
        backButton.addActionListener(e -> handleBackButton());
    
        themeButton.setIcon(FlatLaf.isLafDark() ? sunIcon : moonIcon);
        themeButton.setText("");
        themeButton.addActionListener(e -> toggleTheme());
    
        buttonPanel.add(backButton);
        buttonPanel.add(themeButton);
    
        customizeLabel(levelLabel, "Level: " + level.getLevel());
        customizeLabel(turnLabel, bundle.getString("label.klicks") + ": " + game.getTurns());
        customizeLabel(versucheLabel, bundle.getString("label.tries") + ": " + game.getVersuche());
    
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        labelPanel.setOpaque(false);

        labelPanel.add(levelLabel);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(turnLabel);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(versucheLabel);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(labelPanel, BorderLayout.SOUTH);
    
        return mainPanel;
    }
    
    private void customizeLabel(JLabel label, String text) {
        label.setText(text.toUpperCase());
        label.setFont(new Font("Arial", Font.BOLD, 12)); 
        label.setForeground(new Color(80, 126, 46));
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 0)); 
    }

    private void showOriginalGrid() {
        removeComponent(fieldPanel);
        fieldPanel = createGridPanel(level.getSize());

        fields = new JLabel[level.getSize()][level.getSize()];
        for (int i = 0; i < level.getSize(); i++) {
            for (int j = 0; j < level.getSize(); j++) {
                JLabel field = new JLabel();
                fields[i][j] = field;
                field.setIcon(game.getField().getPoint(i, j) ? blumeEinsIcon : blumeZweiIcon);
                field.setHorizontalAlignment(SwingConstants.CENTER);
                field.setVerticalAlignment(SwingConstants.CENTER);
                fieldPanel.add(field);
            }
        }
        fieldPanel.setBorder(new EmptyBorder(45, PANEL_BORDER, 60, PANEL_BORDER));
        add(fieldPanel);
    }

    private void showGuessGrid() {
        removeComponent(guessPanel);
        guessPanel = createGridPanel(level.getSize());

        buttons = new JButton[level.getSize()][level.getSize()];
        for (int i = 0; i < level.getSize(); i++) {
            for (int j = 0; j < level.getSize(); j++) {
                JButton button = new JButton();
                buttons[i][j] = button;
                button.setIcon(game.getGuessField().getPoint(i, j) ? blumeEinsIcon : blumeZweiIcon);
                button.setOpaque(true);
                button.setBackground(new Color(100, 100, 100, 102));
                button.setBorderPainted(false);

                int x = i;
                int y = j;
                button.addActionListener(e -> handleGuessButton(x, y));
                guessPanel.add(button);
            }
        }
        guessPanel.setBorder(new EmptyBorder(30, PANEL_BORDER, 40, PANEL_BORDER));
        add(guessPanel);
    }

    private void handleBackButton() {
        int response = showConfirmDialog(bundle.getString("dialog.back"), bundle.getString("dialog.back.name"));
        if (response == JOptionPane.YES_OPTION) {
            manager.addHighscore(mainFrame.getName(), level.getLevel());
            resetGame();
            mainFrame.showPanel("Start");
        }
    }

    private void handleGuessButton(int x, int y) {
        if (clickable) {
            game.turn(x, y);
            wonOrLost();
            renderView();
        }
    }

    private void wonOrLost() {
        if (game.over()) {
            if (game.compareFields()) {
                win();
            } else if (game.getVersuche() > 1) {
                wrongClick();
            } else {
                lost();
            }
        }
    }

    private void wrongClick() {
        clickable = false;
        createTimer(1000, e -> {
            game.again();
            clickable = true;
            renderView();
        }).start();
    }

    private Timer createTimer(int delay, ActionListener action) {
        Timer timer = new Timer(delay, action);
        timer.setRepeats(false);
        return timer;
    }

    private void resetGame() {
        level.reset();
        startNewGame();
    }

    private void toggleTheme() {
        try {
            if (FlatLaf.isLafDark()) {
                themeButton.setIcon(moonIcon);
                UIManager.setLookAndFeel(new FlatLightLaf());
            } else {
                themeButton.setIcon(sunIcon);
                UIManager.setLookAndFeel(new FlatDarkLaf());
            }
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createGridPanel(int size) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(size, size, 10, 10));
        panel.setOpaque(false);
        return panel;
    }

    private void removeComponent(JPanel panel) {
        if (panel != null && panel.getParent() != null) {
            remove(panel);
        }
    }

    private int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        return new ImageIcon(getScaledIcon(path, width, height));
    }

    private Image loadImage(String path) {
        return new ImageIcon(getClass().getResource(path)).getImage();
    }

    private Image getScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        return icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}