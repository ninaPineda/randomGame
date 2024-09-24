package com.ninaPineda.app;

import javax.swing.*;

import java.awt.*;
import java.util.ResourceBundle;
import java.util.Locale;

public class MainFrame extends JFrame{
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private String currentPlayer;
    private int score;
    private StartPanel startPanel;
    private GamePanel gamePanel;
    private SettingsPanel settingsPanel;
    private ResourceBundle bundle;

    public MainFrame() {
        setSize(400, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        currentPlayer = bundle.getString("name.default");
        setTitle(bundle.getString("title"));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        startPanel = new StartPanel(this);
        gamePanel = new GamePanel(this);
        settingsPanel = new SettingsPanel(this);

        mainPanel.add(startPanel, "Start");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(settingsPanel,"Settings");

        add(mainPanel);

        cardLayout.show(mainPanel, "Start");
    }

    public void setName(String name){
        this.currentPlayer = name;
    }

    public String getName(){
        return this.currentPlayer;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int score){
        if (score > this.score)
        this.score = score;
    }

    public void showPanel(String name){
        startPanel.refresh();
        cardLayout.show(mainPanel, name);
    }
}
