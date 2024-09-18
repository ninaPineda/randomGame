import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyPanel extends JPanel {

    private int turnCounter;
    private JLabel instructionLabel, levelLabel, turnLabel, versucheLabel;
    private JButton startButton;
    private Game game;
    static private JPanel fieldPanel;  
    static private JPanel guessPanel;  
    private JButton[][] buttons; 
    private JTextField[][] fields; 
    static private Level level = Level.getInstance();
    private int versuche;


    private final Color PASTEL_GREEN = new Color(144, 238, 144);
    private final Color PASTEL_BROWN = new Color(210, 180, 140);

    public MyPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        turnCounter = 0;
        versuche = 3;
        instructionLabel = new JLabel("Starte ein Spiel:");
        startButton = new JButton("Go!");
        versucheLabel = new JLabel("Versuch: " + versuche);
        turnLabel = new JLabel("Klicks: 0");

        add(instructionLabel);
        add(startButton);
        add(versucheLabel);
        add(turnLabel);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    startNewGame(); 
                    repaint();
            }
        });
    }

    private void win() {
        level.levelUp();
        startNewGame(); 
        repaint();
    }

    private void lost(){
        int response = JOptionPane.showConfirmDialog(this, "Du Looser hast verloren! Möchtest du ein neues Spiel starten?", "Spiel verloren", JOptionPane.YES_NO_OPTION);
        
        if (response == JOptionPane.YES_OPTION) {
            level.reset();
            startNewGame(); 
            repaint();
        } else {
            game.delete();
            JOptionPane.showMessageDialog(this, "Danke fürs Spielen!");
        }
    }

    private void startNewGame() {
        turnCounter = 0;
        versuche  = 3;
        game = new Game(level.getSize(), level.getTurns()); 

        //das hier klappt nicht so!!!
        remove(versucheLabel);
        versucheLabel = new JLabel("Versuch: " + versuche);
        add(versucheLabel);

        remove(turnLabel);
        turnLabel = new JLabel("Klicks: " + (level.getTurns()-turnCounter));
        add(turnLabel);
        
        if (levelLabel != null && levelLabel.getParent() != null) {
            remove(levelLabel);
        }

        levelLabel = new JLabel("Dein Level: " + level.getLevel());
        add(levelLabel);

        if (getComponentCount() > 3) { 
            remove(getComponent(3)); 
        }
        
        showOriginalGrid();    
        add(Box.createRigidArea(new Dimension(0, 20))); 
        showGuessGrid();      
        revalidate();
        repaint();
    }


    private void showOriginalGrid() {

        if (fieldPanel != null && fieldPanel.getParent() != null) {
            remove(fieldPanel);
        }
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(game.getSize(), game.getSize()));
        fieldPanel.setLayout(new GridLayout(game.getSize(), game.getSize(), 10, 10));
        fields = new JTextField[game.getSize()][game.getSize()];


        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                JTextField field = new JTextField();
                fields[i][j] = field;


                if (game.getField().getPoint(i, j)) {
                    field.setBackground(PASTEL_GREEN); 
                } else {
                    field.setBackground(PASTEL_BROWN); 
                }
                field.setEditable(false); 

                fieldPanel.add(field); 
            }
        }
        add(fieldPanel);
        revalidate();
        repaint();
    }


    private void showGuessGrid() {

        if (guessPanel != null && guessPanel.getParent() != null) {
            remove(guessPanel);
        }

        guessPanel = new JPanel();
        guessPanel.setLayout(new GridLayout(game.getSize(), game.getSize(), 10, 10));

        buttons = new JButton[game.getSize()][game.getSize()]; 


        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                JButton button = new JButton();
                buttons[i][j] = button;

                if (game.getGuessField().getPoint(i, j)) {
                    button.setBackground(PASTEL_GREEN); 
                } else {
                    button.setBackground(PASTEL_BROWN); 
                }

                button.setOpaque(true);
                button.setBorderPainted(false);

                int x = i;
                int y = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        game.setGuessField(game.getGuessField().click(x,y));
                        turnCounter++;
                        showGuessGrid();
                    }
                });

                guessPanel.add(button); 
            }
        }

        if (turnCounter < level.getTurns() && versuche > 0){
            
        } else if(turnCounter == level.getTurns() && versuche > 0){
            
            if(game.getGuessField().compareWith(game.getField())){
                add(guessPanel);
                revalidate();
                repaint();
                win();

            } else {
                if (versuche > 1){
                    versuche--;

                } else if(versuche == 0){
                    lost();
                }
                
            }
        } 
        add(guessPanel);
        revalidate();
        repaint();
    }
}




