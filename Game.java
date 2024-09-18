public class Game {
    private int size;
    private Field field;
    private Field guessField;

    public Game(int size, int turns) {
        this.size = size;
        field = new Field(size);
        this.guessField = field.turnRnd(turns);
    }

    public void delete(){
        this.field = null;
        this.guessField = null;
    }

    public Field getField(){
        return this.field;
    }

    public Field getGuessField(){
        return this.guessField;
    }

    public int getSize(){
        return this.size;
    }

    public void setGuessField(Field gf){
        this.guessField = gf;
    }

    public void winGuessField(){
        this.guessField.rst();
    }
}

