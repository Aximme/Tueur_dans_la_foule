package fr.miroff.FouleProject.character;

public class Personnage {
    protected int health;
    private int x;
    private int y;
    private static boolean canMove = true;

    public Personnage(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static void stopMovements() {
        canMove = false;
    }

    public void move() {

        if (!canMove) {
            return;
        }
        // Mouvement sur l'axe X
        if (Math.random() < 0.5) {
            if (x < 800) {
                x += 1;
            }
        } else if (x > 0) {
            x -= 1;
        }

        // Mouvement sur l'axe Y
        if (Math.random() < 0.5) {
            if (y < 600) {
                y += 1;
            }
        } else if (y > 0) {
            y -= 1;
        }
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

