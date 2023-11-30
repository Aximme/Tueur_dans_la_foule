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
    private static final int vision = 500;
    protected static final List<Integer> BASE_SPEEDS = Arrays.asList(1, 2, 3);
    protected static final Random RAND = new Random();
    protected int speed;

    final List<Building> buildings;

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

    protected void avoidBuildings() {
        int nextX = x + movementSpeed; // Déplacement vers la droite par défaut
        int nextY = y;

        // Vérifie les collisions avec les bâtiments pour éviter les obstacles
        for (Building building : buildings) {
            if (isCollidingWithBuilding(nextX, y, building)) {
                // Si une collision est détectée, ajustez la direction du déplacement
                if (Math.random() < 0.5) {
                    // Déplacez-vous vers le haut
                    nextX = x;
                    nextY = y + movementSpeed;
                } else {
                    // Déplacez-vous vers le bas
                    nextX = x;
                    nextY = y - movementSpeed;
                }
                break; // Sortez de la boucle, ne vérifiez pas les autres bâtiments
            }
        }

        // Applique le déplacement si pas de collision avec les bâtiments
        if (nextX >= 0 && nextX < Window.WINDOW_WIDTH && nextY >= 0 && nextY < Window.WINDOW_HEIGHT - 100) {
            x = nextX;
            y = nextY;
        }
    }


    public void move(List<Character> characters) {
        if (!canMove) {
            return;
        }

        if (target()) {
            pathfinding();
        } else {
            int nextX = getX();
            int nextY = getY();

            // Logique de mouvement aléatoire
            if (Math.random() < 0.5) {
                nextX += movementSpeed;
            } else {
                nextX -= movementSpeed;
            }

            if (Math.random() < 0.5) {
                nextY += movementSpeed;
            } else {
                nextY -= movementSpeed;
            }

            // Vérifie les collisions avec les bâtiments
            boolean canMoveX = true;
            boolean canMoveY = true;

            for (Building building : buildings) {
                if (isCollidingWithBuilding(nextX, getY(), building)) {
                    canMoveX = false;
                }
                if (isCollidingWithBuilding(getX(), nextY, building)) {
                    canMoveY = false;
                }
            }

            // Applique les déplacements si pas de collision avec les bâtiments
            if (canMoveX && nextX >= 0 && nextX < Window.WINDOW_WIDTH) {
                setX(nextX);
            }

            if (canMoveY && nextY >= 0 && nextY < Window.WINDOW_HEIGHT - 100) {
                setY(nextY);
            }
        }
    }


    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }


    protected boolean isCollidingWithBuilding(int pointX, int pointY, Building building) {
        return pointX >= building.getX() - 20 && pointX <= building.getX() + building.getWidth() + 20 &&
                pointY >= building.getY() - 20 && pointY <= building.getY() + building.getHeight() + 20;

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

        if (indice != -1) {
            Character c = Window.characters.get(indice);

            // Calcule les vecteurs de distance
            int distanceX = c.getX() - this.getX();
            int distanceY = c.getY() - this.getY();

            // Logique d'évitement des bâtiments
            int nextX = x + (int) Math.signum(distanceX) * movementSpeed;
            int nextY = y + (int) Math.signum(distanceY) * movementSpeed;

            // Vérifie les collisions avec les bâtiments
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

            // Applique les déplacements si pas de collision avec les bâtiments
            if (canMoveX && nextX >= 0 && nextX < Window.WINDOW_WIDTH) {
                x = nextX;
            }

            if (canMoveY && nextY >= 0 && nextY < Window.WINDOW_HEIGHT - 100) {
                y = nextY;
            }



            

            }
        }

    }


