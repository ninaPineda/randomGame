package com.ninaPineda.app;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class SettingsPanel extends JPanel{
    private JButton backButton, changeNameButton;
    private JTextField nameField;
    private final Image backgroundStart, backgroundStartDark;
    
    public SettingsPanel(MainFrame mainFrame) {
                // Load Images
                backgroundStart = loadImage("/icons/background_start.png");
                backgroundStartDark = loadImage("/icons/background_start_dark.png");
                ImageIcon back = new ImageIcon(getScaledIcon("/icons/back_arrow.png", 20, 20));
                backButton = new JButton(back);
        setOpaque(false);

        nameField = new JTextField(15);
        nameField.setText("Hier Namen ändern");
        changeNameButton = new JButton("Namen ändern");

        nameField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
                nameField.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                
            }
        
        });
        
        // ActionListener zum Ändern des Namens
        changeNameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = nameField.getText().trim();
                if (!newName.isEmpty()) {
                    mainFrame.setName(newName); 
                    nameField.setText(newName); 
                    mainFrame.showPanel("Start");
                } else {
                    JOptionPane.showMessageDialog(SettingsPanel.this, "Name darf nicht leer sein!");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("Start");
            }
        });

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(40,5,10,30));

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(nameField);
        bottomPanel.add(changeNameButton);
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(40,30,10,30));
        add(bottomPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (FlatLaf.isLafDark()) {
            g.drawImage(backgroundStartDark, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundStart, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
