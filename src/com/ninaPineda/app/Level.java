package com.ninaPineda.app;
public class Level {
    private int level, turns, size;
    private static Level instance;

    private Level() {
        level = 1;
        turns = 1;
        size = 4;
    }

    public static Level getInstance() {
        if (instance == null) {
            instance = new Level();
        }
        return instance;
    }

    public void levelUp() {
        if (this.level < 10) {
            this.level++;
        } else if (this.level < 20) {
            this.level++;
            this.size = 5;
        } else if (this.level < 30) {
            this.level++;
            this.size = 6;
        } else if (this.level < 40) {
            this.level++;
            this.turns = 2;
            this.size = 4;
        } else if (this.level < 50) {
            this.level++;
            this.size = 5;
        } else if (this.level < 60) {
            this.level++;
            this.size = 6;
        } else if (this.level < 70) {
            this.level++;
            this.turns = 3;
            this.size = 4;
        } else if (this.level < 80) {
            this.level++;
            this.size = 5;
        } else {
            this.level++;
            this.size = 6;
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
        size = 4;
    }
}
