package fr.miroff.FouleProject.character;

public class Personnage {
    protected Object deplacement;
    protected int health;



    private int x;
    private int y;


    //constructeur
    public Personnage(int x, int y ) {
        this.x = x;
        this.y = y;
    }


    public void setDeplacement(Object deplacement) {
        double a = Math.random();
        double b = Math.random();
        if (a < 0.5 || x < -1000){
            x += 1;
        }
        else{
            x -= 1;
        }

        if (b < 0.5 || y < 1000){
            y += 1;
        }
        else
            y -= 1;
        this.deplacement = deplacement;
    }

    private ListePerso pointManager; // Ajoutez cette référence

    // Autres membres de la classe...

    public Personnage(int x, int y, ListePerso pointManager) {
        this.x = x;
        this.y = y;
        this.pointManager = pointManager; // Stockez la référence
    }

    public boolean hurt() {
        this.health -= 1;
        boolean isDead = this.health == 0;

        if (isDead) {
            // Appeler la méthode de suppression de l'agent en utilisant pointManager
            pointManager.supprimerAgent(this.x, this.y);
        }

        return isDead;
    }
}

