public class Level {
    private int level, turns, size;
    private static Level instance;

    private Level() {
        level = 1;
        turns = 1;
        size = 5;
    }

    public static Level getInstance() {
        if (instance == null) {
            instance = new Level();
        }
        return instance;
    }

    public void levelUp() {
        if (this.level < 5) {
            this.level++;
        } else if (this.level < 10) {
            this.level++;
            this.turns = 2;
        } else if (this.level < 20) {
            this.level++;
            this.turns = 2;
            this.size = 6;
        } else {
            this.level++;
            this.turns = 3;
            this.size = 5;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getTurns() {
        return turns;
    }

    public int getSize() {
        return size;
    }

    public void reset() {
        level = 1;
        turns = 1;
        size = 5;
    }
}
