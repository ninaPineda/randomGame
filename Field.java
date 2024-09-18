import java.awt.Point;
import java.util.Random;

public class Field {
    private int size;
    private boolean[][] field;
    private Random rnd;

    public Field(int size) {
        this.size = size;
        field = new boolean[size][size];
        rnd = new Random(); 

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = rnd.nextBoolean(); 
            }
        }
    }

    public int getSize(){
        return this.size;
    }

    public boolean getPoint(int x, int y){
        return this.field[x][y];
    }

    public void rst(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = true; 
            }
        }
    }

    public boolean compareWith(Field otherField) {
        if (this.size != otherField.getSize()) {
            return false;
        }
    
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.field[i][j] != otherField.getPoint(i, j)) {
                    return false; 
                }
            }
        }
    
        return true;
    }
    

    public Field turnRnd(int turns){
        rnd = new Random(); 
        int x, y;
        Point[] reminder = new Point[turns];
        Field newField = new Field(this.size);
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newField.field[i][j] = this.field[i][j];
            }
        }
        

        for (int i = 0; i < turns; i++){
            boolean problem = false;
            x = rnd.nextInt(size);
            y = rnd.nextInt(size);

            for (int j = 0; j < i; j++) {
                if (reminder[j].x == x && reminder[j].y == y) {
                    problem = true;
                    break;
                }
            }

            if (!problem) {
                newField = newField.click(x,y);
                reminder[i] = new Point(x, y);
            } else {
                i--;
            }
            
        }

        return newField;
    }

    public Field click(int x, int y) {
        Field newField = new Field(this.getSize());

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newField.field[i][j] = this.field[i][j];
            }
        }

        toggleIfValid(newField, x, y);        // Klicke auf das zentrale Feld
        toggleIfValid(newField, x - 1, y);    // Oben
        toggleIfValid(newField, x + 1, y);    // Unten
        toggleIfValid(newField, x, y - 1);    // Links
        toggleIfValid(newField, x, y + 1);    // Rechts
        toggleIfValid(newField, x - 1, y - 1);// Oben links
        toggleIfValid(newField, x + 1, y - 1);// Unten links
        toggleIfValid(newField, x - 1, y + 1);// Oben rechts
        toggleIfValid(newField, x + 1, y + 1);// Unten rechts

        return newField;
    }

    private void toggleIfValid(Field newField, int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            newField.field[x][y] = !newField.field[x][y];
        }
    }
}

