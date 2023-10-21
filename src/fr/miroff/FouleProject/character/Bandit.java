package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Window;

public class Bandit extends Character {
    public Bandit(int x, int y, int movementSpeed, Window window) {
        super(x, y,movementSpeed, window);
        this.health = 1;
        this.window = window;
    }


    public boolean attack(Civil civil) {
        if (!civil.escape()) {
            return civil.hurt(); // Le Civil n'a pas réussi à s'échapper, il est mort.
        }
        System.out.println("Le Civil s'est échappé !");
        return false; // Le Civil a réussi à s'échapper, il n'est pas mort.
    }
}
