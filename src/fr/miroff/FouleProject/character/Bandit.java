package fr.miroff.FouleProject.character;

public class Bandit extends Character {
    public Bandit(int x, int y,int movementSpeed) {
        super(x, y,movementSpeed);
        this.health = 1;
    }


    public boolean attack(Civil civil) {
        if (!civil.escape()) {
            return civil.hurt(); // Le Civil n'a pas réussi à s'échapper, il est mort.
        }
        System.out.println("Le Civil s'est échappé !");
        return false; // Le Civil a réussi à s'échapper, il n'est pas mort.
    }
}
