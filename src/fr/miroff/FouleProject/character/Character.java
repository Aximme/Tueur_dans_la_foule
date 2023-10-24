package fr.miroff.FouleProject.character;
import fr.miroff.FouleProject.Window;

import java.util.List;

public class Character {
    protected int health;
    private int x;
    private int y;
    private static boolean canMove = true;
    private int movementSpeed;
    Window window;


    public Character(int x, int y, int movementSpeed, Window window) {
        this.x = x;
        this.y = y;
        this.movementSpeed = movementSpeed;
        this.window = window;
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
    public static void resumeMovements() {
        canMove = true;
    }

    public void move(List<Character> characters) {
        if (!canMove) {
            return;
        }

        if (Math.random() < 0.5) {
            if (x < Window.WINDOW_WIDTH) {
                x += movementSpeed;
            }
        } else if (x > 0) {
            x -= movementSpeed;
        }

        if (Math.random() < 0.5) {
            if (y < Window.WINDOW_HEIGHT - 100) { //Window Height & -100 Pour affichage en mode fenêtré
                y += movementSpeed;
            }
        } else if (y > 0) {
            y -= movementSpeed;
        }

        for (Character other : characters) {
            if (other != this) {
                this.interact(other);
            }
        }
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    private boolean isColliding(Character other) {
        double distance = Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
        int sumOfRadii = 10 + 10;
        return distance <= sumOfRadii;
    }

    public void interact(Character other) {
        if (this.isColliding(other)) {
            if (this instanceof Bandit && other instanceof Civil) {
                other.hurt();
            } else if (this instanceof Cop && other instanceof Bandit) {
                other.hurt();
            } else {
                this.x -= 1;
                this.y -= 1;
                other.x += 1;
                other.y += 1;
            }
        }
    }

    public boolean hurt() {
        this.health -= 1;
        if (this.health == 0) {
            // L'agent est mort, appeler la méthode de mise à jour des compteurs dans la classe Window
            if (window != null) {
                window.updateCounters(this);
            }
        }
        return this.health == 0;
    }

    public boolean isAlive() {
        return this.health > 0;
    }
}
