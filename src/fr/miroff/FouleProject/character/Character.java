package fr.miroff.FouleProject.character;
import java.util.List;

public class Character {
    protected int health;
    private int x;
    private int y;

    public Character(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(List<Character> characters) {
        // Mouvement sur l'axe X
        if (Math.random() < 0.5) {
            if (x < 1000) {
                x += 1;
            }
        } else if (x > 0) {
            x -= 1;
        }

        // Mouvement sur l'axe Y
        if (Math.random() < 0.5) {
            // supposons que 1000 est la hauteur maximale de l'écran
            if (y < 1000) {
                y += 1;
            }
        } else if (y > 0) {
            y -= 1;
        }
        for (Character other : characters) {
            if (other != this) {  // Ne pas interagir avec soi-même
                this.interact(other);
            }
        }
    }

    private boolean isColliding(Character other){
        double distance = Math.sqrt(Math.pow(this.x-other.x,2)+ Math.pow(this.y-other.y,2));
        int sumOfRadii= 10 + 10; //rayon de 10
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
        return this.health == 0;
    }

    public boolean isAlive() {
        return this.health > 0;
    }


}

