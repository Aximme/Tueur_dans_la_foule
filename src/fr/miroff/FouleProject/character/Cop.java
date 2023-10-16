package fr.miroff.FouleProject.character;

public class Cop extends Personnage {
    public Cop(int x, int y) {
        super(x, y);
        this.health = 1;

    }

    public boolean attack(Bandit bandit) {
        return bandit.hurt();
    }

}
