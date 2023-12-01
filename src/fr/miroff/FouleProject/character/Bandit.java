package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Window;

public class Bandit extends Character {
    public Bandit(int x, int y, int movementSpeed, Window window) {
        super(x, y, movementSpeed, window);
        this.health = 1;
        this.window = window;
        this.speed = chooseRandomSpeed();
    }

    public boolean attack(Civil civil) {
        if (!civil.escape()) {
            return civil.hurt();
        }
        System.out.println("Le Civil s'est échappé !");
        return false;
    }
}
