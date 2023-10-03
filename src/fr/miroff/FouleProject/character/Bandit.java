package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.character.Civil;


public class Bandit extends Personnage {
    public Bandit(int x, int y) {
        super(x, y);
        Object deplacement1 = this.deplacement;
        this.health = 1;
    }

    public boolean attack(Civil civil){
        return civil.hurt();
    }
}
