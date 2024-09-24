package com.ninaPineda.app;
public class Game {
    private static Game instance;
    private int turnCounter, versuche;
    private Field field;
    private Field guessField;
    private Field guessFieldBackup;
    Level level = Level.getInstance();

    private Game() {
        field = new Field(level.getSize());
        guessField = field.turnRnd(level.getTurns());
        guessFieldBackup = guessField.copy();
        turnCounter = level.getTurns();
        versuche = 3;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public Field getGuessFieldBackup(){
        return guessFieldBackup;
    }

    public void reset() {
        field = new Field(level.getSize());
        guessField = field.turnRnd(level.getTurns());
        guessFieldBackup = guessField.copy();
        turnCounter = level.getTurns();
        versuche = 3;
    }

    public void turn(int x, int y){
        turnCounter--;
        System.out.println("Turncounter: " + turnCounter);
        this.guessField = guessField.click(x,y);      
    }   

    public boolean compareFields(){
        System.out.println("Felder sind gleich: " + (guessField.compareWith(field)));
        return guessField.compareWith(field);
    }

    public void again(){
        turnCounter = level.getTurns();
        versuche--;
        guessField = guessFieldBackup.copy();
        System.out.println("Versuche: " + versuche);
    }

    public boolean over(){
        System.out.println("Game ist Over: " + (turnCounter == 0));
        return turnCounter == 0;
    }

    public int getVersuche(){
        return versuche;
    }

    public int getTurns(){
        return turnCounter;
    }

    public boolean versucheUber(){
        return versuche > 0;
    }

    public boolean turnsUber(){
        return turnCounter > 0;
    }

    public void decreaseVersuche(){
        versuche--;
    }

    public void decreaseTurns(){
        turnCounter--;
    }

    public Field getField(){
        return this.field;
    }

    public Field getGuessField(){
        return this.guessField;
    }

    public void setGuessField(Field gf){
        this.guessField = gf;
    }

    public void winGuessField(){
        this.guessField.rst();
    }
}