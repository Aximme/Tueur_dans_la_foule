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

    private final List<Building> buildings;

    public Character(int x, int y, int movementSpeed, Window window) {
        this.x = x;
        this.y = y;
        this.movementSpeed = movementSpeed;
        this.window = window;
        this.speed = chooseRandomSpeed();
        this.buildings = window.buildings;
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

            for (Building building : buildings) {
                if (!isCollidingWithBuilding(nextX, y, building)) {
                    x = nextX;
                }
                if (!isCollidingWithBuilding(x, nextY, building)) {
                    y = nextY;
                }
            }
        }
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    private boolean isCollidingWithBuilding(int pointX, int pointY, Building building) {
        //return pointX >= building.getX()-20 && pointX <= building.getX() + building.getWidth()+20 &&
         //       pointY >= building.getY()-20 && pointY <= building.getY() + building.getHeight()+20;
        return false;
    }

    public void interact(Character other) {
        other.hurt();
    }

    public boolean hurt() {
        this.health -= 1;
        if (this.health == 0) {
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

    public void adjustPositions(Character other) {
        int dx = other.getX() - this.getX();
        int dy = other.getY() - this.getY();

        double length = Math.sqrt(dx * dx + dy * dy);
        dx = (int) (dx / length);
        dy = (int) (dy / length);

        this.setX(this.getX() - dx);
        this.setY(this.getY() - dy);
        other.setX(other.getX() + dx);
        other.setY(other.getY() + dy);
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
    }
}
