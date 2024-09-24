package com.ninaPineda.app;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class WinPanel extends JPanel {

    private int numConfetti;
    private Random random;

    public WinPanel(int numConfetti) {
        this.numConfetti = numConfetti;
        this.random = new Random();
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < numConfetti; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            int size = random.nextInt(10) + 5; 
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(color);
            g.fillOval(x, y, size, size); 
        }
    }
}
