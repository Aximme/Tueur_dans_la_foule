package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.character.Civil;


public class Bandit extends Personnage {
    public Bandit(int x, int y) {
        super(x, y);
        Object deplacement1 = this.deplacement;
        this.health = 1;
    }



    public boolean attack(Civil civil) {
        if (!civil.tryToEscape()) {
            return civil.hurt(); // Le Civil n'a pas réussi à s'échapper, il est mort.
        }
        System.out.println("Le Civil s'est échappé !");
        return false; // Le Civil a réussi à s'échapper, il n'est pas mort.
    }
}
