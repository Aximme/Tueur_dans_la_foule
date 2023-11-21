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
    private static final int vision = 500 ;


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
        if (target()) {
            pathfinding();
        }
        else {
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
                if (y < Window.WINDOW_HEIGHT - 100) { //Window Height & -100 Pour affichage en mode fenêtre
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
    }

    private boolean isCollidingWithBuilding(int pointX, int pointY, Building building) {
        return pointX >= building.getX() && pointX <= building.getX() + building.getWidth() &&
                pointY >= building.getY() && pointY <= building.getY() + building.getHeight();

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


    public boolean attackIsPossible(Character other) {
        return (this instanceof Bandit && other instanceof Civil) || (this instanceof Cop && other instanceof Bandit);
    }

    public double distanceBetween(Character c) {
        return Math.sqrt(Math.pow(this.x - c.getX(), 2) + Math.pow(this.y - c.getY(), 2));
    }

    public int characterClosest() {
        double distanceMin = vision;
        int indice = -1;

        for (int i = 0; i < Window.characters.size(); i++) {
            if (attackIsPossible(Window.characters.get(i))) {
                double distance = distanceBetween(Window.characters.get(i));
                if (distanceMin > distance) {
                    distanceMin = distance;
                    indice = i;
                }
            }
        }
        return indice;
    }

    public boolean target() {
        int indice = characterClosest();

        if (indice != -1) {
            double distance = distanceBetween(Window.characters.get(indice));
            return distance < vision;
        } else return false;
    }


    public void pathfinding() {
        if (target()) {
            // Cible la personne la plus proche
            int indice = characterClosest();
            Character c = Window.characters.get(indice);

            // Calcule les vecteurs de distance
            int distanceX = c.getX() - this.getX();
            int distanceY = c.getY() - this.getY();

            //Se déplace
            if (distanceX > 0) {
                if (x < (Window.WINDOW_WIDTH + movementSpeed)) {
                    x += movementSpeed;
                }
            }
            else if (distanceX < 0) {
                if (x > (Window.WINDOW_WIDTH - movementSpeed)) {
                    {
                        x -= movementSpeed;
                    }
                }
            }

            if (distanceY > 0){
                if (y < (Window.WINDOW_HEIGHT - 100 + movementSpeed)) { //Window Height & -100 Pour affichage en mode fenêtre
                    y += movementSpeed;
                }
            }
            else if (distanceY < 0) {
                if (y > (Window.WINDOW_HEIGHT - 100- movementSpeed)) { //Window Height & -100 Pour affichage en mode fenêtre
                    y -= movementSpeed;
                }
            }

            for (int i = 0; i < Window.characters.size(); i++) {
                if (Window.characters.get(i) != this) {
                    this.interact(Window.characters.get(i));
                }
            }
        }
    }
}
