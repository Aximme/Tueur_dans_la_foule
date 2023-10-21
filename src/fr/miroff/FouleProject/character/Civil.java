package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Window;

import java.util.Random;

public class Civil extends Character {
    private static final Random RAND = new Random();

    public Civil(int x, int y, int movementspeed, Window window) {
        super(x, y, movementspeed, window);
        this.health = 1;
        this.window = window;
    }

    public boolean escape() {
        return RAND.nextInt(3) == 0;
    }

}



