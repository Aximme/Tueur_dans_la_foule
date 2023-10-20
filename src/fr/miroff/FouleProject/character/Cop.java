package fr.miroff.FouleProject.character;

public class Cop extends Character {
    public Cop(int x, int y, int movementSpeed) {
        super(x, y,movementSpeed);
        this.health = 1;

    }

    public boolean attack(Bandit bandit) {
        return bandit.hurt();
    }

}
