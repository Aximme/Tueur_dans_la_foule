package fr.miroff.FouleProject.character;
import fr.miroff.FouleProject.Building;
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
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static void stopMovements() {
        canMove = false;
    }

    public static void resumeMovements() {
        canMove = true;
    }

    private List<Building> buildings; // Ajout de la liste des bâtiments

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void move(List<Character> characters) {
        if (!canMove) {
            return;
        }
        int nextX = x;
        int nextY = y;

        if (Math.random() < 0.5) {
            if (x < Window.WINDOW_WIDTH) {
                nextX += movementSpeed;
            }
        } else if (x > 0) {
            nextX -= movementSpeed;
        }

        if (Math.random() < 0.5) {
            if (y < Window.WINDOW_HEIGHT - 100) { //Window Height & -100 Pour affichage en mode fenêtré
                nextY += movementSpeed;
            }
        } else if (y > 0) {
            nextY -= movementSpeed;
        }
        boolean canMoveX = true;
        boolean canMoveY = true;

        for (Building building : buildings) {
            if (isCollidingWithBuilding(nextX, y, building)) {
                canMoveX = false;
            }
            if (isCollidingWithBuilding(x, nextY, building)) {
                canMoveY = false;
            }
        }
        if (canMoveX) {
            x = nextX;
        }
        if (canMoveY) {
            y = nextY;
        }

        for (Character other : characters) {
            if (other != this) {
                this.interact(other);
            }
        }
    }

    private boolean isCollidingWithBuilding(int pointX, int pointY, Building building) {
        return pointX >= building.getX()-20 && pointX <= building.getX() + building.getWidth()+20 &&
                pointY >= building.getY()-20 && pointY <= building.getY() + building.getHeight()+20;

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

    public boolean attaqueIsPossible(Character other) {
        if ((this instanceof Bandit && other instanceof Civil) || (this instanceof Cop && other instanceof Bandit)) {
            return true;
        } else return false;
    }

    public double distanceBetween(Character c) {
        return Math.sqrt(Math.pow(this.x - c.getX(), 2)) + Math.pow(this.y - c.getY(), 2);
    }

    public int characterClosest() {
        double distanceMin = distanceBetween(Window.characters.get(0));
        int indice = -1;

        for (int i = 0; i < Window.characters.size(); i++) {
            double distance = distanceBetween(Window.characters.get(i));
            if (distanceMin > distance) {
                distanceMin = distance;
                if (attaqueIsPossible(Window.characters.get(indice))) {
                    indice = i;
                }
            }
        }
        return indice;
    }

    public boolean target() {
        int indice = characterClosest();
        if (indice != -1) {
            return true;
        } else return false;
    }

    public void pathfinding(Character x) {
        // Calculez le vecteur de déplacement vers le personnage cible (x)
        int targetX = x.getX();
        int targetY = x.getY();
        int deltaX = targetX - this.getX();
        int deltaY = targetY - this.getY();

        // Calcul de la distance euclidienne jusqu'à la cible
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance < this.movementSpeed) {
            // Atteint la cible, effectuez une action (par exemple, attaquez)
            if (this.attaqueIsPossible(x)) {
                this.interact(x);
            }
        } else {
            // Déplacez-vous vers la cible en direction de deltaX et deltaY
            int moveX = (int) (this.movementSpeed * (deltaX / distance));
            int moveY = (int) (this.movementSpeed * (deltaY / distance));
            this.setX(this.getX() + moveX);
            this.setY(this.getY() + moveY);
        }

    }

}

