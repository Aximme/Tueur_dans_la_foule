package fr.miroff.FouleProject.character;

public class Personnage {
    protected int health;
    private int x;
    private int y;

    public Personnage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        // TODO: 16/10/2023 Do something that work to move characters INSIDE THE SCREEN and HANDLE COLLISIONS

        if (Math.random() < 0.5 || x < -1000) {
            x += 1;
        } else {
            x -= 1;
        }

        if (Math.random() < 0.5 || y < 1000) {
            y += 1;
        } else
            y -= 1;
    }

    private boolean isColliding(){
        // TODO: 17/10/2023 Handle collisions
        return false;
    }

    public boolean hurt() {
        this.health -= 1;

        return this.health == 0;
    }
}

