package com.ninaPineda.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Map;

public class StartPanel extends JPanel {

    private final JButton settingsButton, startButton, themeButton;
    private final JLabel nameLabel;
    private JScrollPane highscoreTable;
    private final MainFrame mainFrame;
    private final ResourceBundle bundle;
    private final Image backgroundStart, backgroundStartDark;
    private final HighscoreManager manager;
    private static final Color darkGreen = new Color(80,126,46);
    private static final Color lightGreen = new Color(162,198,118);

    public StartPanel(MainFrame mf) {
        this.mainFrame = mf;
        this.manager = HighscoreManager.getInstance();
        this.bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        
        // Load Images
        backgroundStart = loadImage("/icons/background_start.png");
        backgroundStartDark = loadImage("/icons/background_start_dark.png");

        // Theme Switch Button
        ImageIcon cog = new ImageIcon(getScaledIcon("/icons/zahnrad.png", 20, 20));
        ImageIcon moonIcon = new ImageIcon(getScaledIcon("/icons/halbmond.png", 20, 20));
        ImageIcon sunIcon = new ImageIcon(getScaledIcon("/icons/sonne.png", 20, 20));
        themeButton = createThemeButton(moonIcon, sunIcon);

        // Initialize Components
        settingsButton = createImageButton(cog, e -> mainFrame.showPanel("Settings"));
        startButton = createButton("button.start", this::handleStartButton);
        nameLabel = createStyledLabel(mainFrame.getName(), 24, darkGreen);
        highscoreTable = createHighscoreTable();


        setupLayout();

        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    private void handleStartButton(java.awt.event.ActionEvent e) {
        if (nameLabel.getText().equals(bundle.getString("name.default"))) {
            JOptionPane.showMessageDialog(this, bundle.getString("dialog.noName"));
        } else {
            mainFrame.showPanel("Game");
        }
    }

    private JButton createButton(String labelKey, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(bundle.getString(labelKey).toUpperCase());
        button.addActionListener(actionListener);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));  
        button.setBackground(darkGreen);
        button.setForeground(Color.BLACK);
        button.setSize(getWidth()/4, getWidth()/8);
        return button;
    }

    private JButton createImageButton(ImageIcon image,java.awt.event.ActionListener actionListener){
        JButton button = new JButton(image);
        button.addActionListener(actionListener);
        button.setOpaque(false);
        return button;
    }

    private JLabel createStyledLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        label.setForeground(color);
        return label;
    }

    private JScrollPane createHighscoreTable() {
    String[] columnNames = { "", "Name", "Level" };

    Object[][] data = new Object[manager.getHighscores().size()][3];
    int index = 0;
    int num = 1;

    for (Map.Entry<String, Integer> entry : manager.getHighscores().entrySet()) {
        data[index][0] = num++; 
        data[index][1] = entry.getKey().toUpperCase();
        data[index][2] = entry.getValue();
        index++;
    }

    JTable table = new JTable(data, columnNames);
    table.setFont(new Font("SansSerif", Font.PLAIN, 16));
    table.setRowHeight(40);
    table.getColumnModel().getColumn(0).setPreferredWidth(30); // Breite für Nummern
    table.getColumnModel().getColumn(1).setPreferredWidth(200); // Breite für Namen
    table.getColumnModel().getColumn(2).setPreferredWidth(50); // Breite für Punkte
    table.setFillsViewportHeight(true);
    table.setOpaque(false);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setOpaque(false);
    scrollPane.setPreferredSize(new Dimension(300, 200));  
    return scrollPane;
}

    private JButton createThemeButton(ImageIcon moonIcon, ImageIcon sunIcon) {
        JButton button = new JButton(moonIcon);
        button.setText("");
        button.addActionListener(e -> switchTheme(button, moonIcon, sunIcon));
        return button;
    }

    private void switchTheme(JButton button, ImageIcon moonIcon, ImageIcon sunIcon) {
        try {
            if (button.getIcon().equals(moonIcon)) {
                button.setIcon(sunIcon);
                UIManager.setLookAndFeel(new FlatDarkLaf());
            } else {
                button.setIcon(moonIcon);
                UIManager.setLookAndFeel(new FlatLightLaf());
            }
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel startPanel = new JPanel(new BorderLayout());
        startPanel.add(startButton, BorderLayout.SOUTH);
        JPanel name = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startPanel.add(createStyledLabel(bundle.getString("label.name") + ": ", 24, darkGreen));
        startPanel.add(nameLabel, BorderLayout.NORTH);
        startPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(themeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.add(settingsButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        buttonPanel.setOpaque(false);

        JPanel highscorePanel = new JPanel(new BorderLayout());
        highscorePanel.add(highscoreTable, BorderLayout.CENTER);
        highscorePanel.setOpaque(true);

        add(buttonPanel, BorderLayout.NORTH);
        add(highscorePanel, BorderLayout.SOUTH);
        add(startPanel, BorderLayout.CENTER);
    }

    private Image loadImage(String path) {
        return new ImageIcon(getClass().getResource(path)).getImage();
    }

    private Image getScaledIcon(String path, int width, int height) {
        return new ImageIcon(getClass().getResource(path)).getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (FlatLaf.isLafDark()) {
            g.drawImage(backgroundStartDark, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundStart, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void refresh() {
        nameLabel.setText(mainFrame.getName());
        highscoreTable = createHighscoreTable();
        revalidate();
    }
}