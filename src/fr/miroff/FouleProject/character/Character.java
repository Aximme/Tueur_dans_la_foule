package fr.miroff.FouleProject.character;
import fr.miroff.FouleProject.Building;
import fr.miroff.FouleProject.Window;
import java.util.Arrays;
import java.util.Random;
import java.util.List;

public class Character {
    protected int health;
    private int x;
    private int y;
    protected static boolean canMove = true;
    protected int movementSpeed;
    Window window;
    private static final int vision = 250 ;
    protected static final List<Integer> BASE_SPEEDS = Arrays.asList(1, 2, 3);
    protected static final Random RAND = new Random();
    protected int speed;


    public Character(int x, int y, int movementSpeed, Window window) {
        this.x = x;
        this.y = y;
        this.movementSpeed = movementSpeed;
        this.window = window;
        this.speed = chooseRandomSpeed();
    }
    protected int chooseRandomSpeed() {
        return BASE_SPEEDS.get(RAND.nextInt(BASE_SPEEDS.size()));
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



    public void move(List<Character> characters) {
        if (!canMove) {
            return;
        }

        if (target()) {pathfinding();}
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

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }



    private List<Building> buildings; // Ajout de la liste des bâtiments

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    private boolean isCollidingWithBuilding(int pointX, int pointY, Building building) {
        return pointX >= building.getX()-20 && pointX <= building.getX() + building.getWidth()+20 &&
                pointY >= building.getY()-20 && pointY <= building.getY() + building.getHeight()+20;

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
            return true;
        } else return false;
    }


    public void pathfinding() {
        // Cible la personne la plus proche
        int indice = characterClosest();
        Character c = Window.characters.get(indice);

        // Calcule les vecteurs de distance
        int distanceX = c.getX() - this.getX();
        int distanceY = c.getY() - this.getY();

        //Se déplace
        if (distanceX > 0) {
            x += movementSpeed;
        }
        else if (distanceX < 0) {
            x -= movementSpeed;
        }

        if (distanceY > 0){
            y += movementSpeed;
        }
        else if (distanceY < 0) {
            y -= movementSpeed;

        }

        // Gerer les collision
        for (int i = 0; i < Window.characters.size(); i++) {
            if (Window.characters.get(i) != this) {
                this.interact(Window.characters.get(i));
            }
        }
    }
}
