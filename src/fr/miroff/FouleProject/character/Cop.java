package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Window;

public class Cop extends Character {
    public Cop(int x, int y, int movementSpeed, Window window) {
        super(x, y,movementSpeed, window);
        this.health = 1;
        this.window = window;
    }

    public boolean attack(Bandit bandit) {
        return bandit.hurt();
    }

}
