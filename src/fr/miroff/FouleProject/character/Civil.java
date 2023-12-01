package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Building;
import fr.miroff.FouleProject.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Civil extends Character {
    private static final Random RAND = new Random();

    public Civil(int x, int y, int movementSpeed, Window window) {
        super(x, y, movementSpeed, window);
        this.health = 1;
        this.window = window;
        this.speed = chooseRandomSpeed();
    }

    public boolean escape() {
        return true;
    }

    public void moveTowardsPoint(int targetX, int targetY) {
        if (!canMove) {
            return;
        }


        int distanceX = targetX - this.getX();
        int distanceY = targetY - this.getY();

        int nextX;

        if (distanceX > 0) {
            nextX = this.getX() + movementSpeed;
        } else if (distanceX < 0) {
            nextX = this.getX() - movementSpeed;
        } else {
            nextX = this.getX();
        }

        int nextY;

        if (distanceY > 0) {
            nextY = this.getY() + movementSpeed;
        } else if (distanceY < 0) {
            nextY = this.getY() - movementSpeed;
        } else {
            nextY = this.getY();
        }


        boolean collisionX = false;
        boolean collisionY = false;

        for (Building building : buildings) {
            if (isCollidingWithBuilding(nextX, this.getY(), building) || isCollidingWithCircularBuilding(nextX, this.getY(), building)) {
                collisionX = true;
                this.setY(nextY);

            }
        }

        for (Building building : buildings) {
            if (isCollidingWithBuilding(this.getX(), nextY, building) || isCollidingWithCircularBuilding(this.getX(), nextY, building)) {
                collisionY = true;
                this.setX(nextX);
            }
        }

        // Applique le déplacement si pas de collision avec les bâtiments
        if (!collisionX && nextX >= 0 && nextX < Window.WINDOW_WIDTH && !collisionY && nextY >= 0 && nextY < Window.WINDOW_HEIGHT - 100) {
            this.setX(nextX);
            this.setY(nextY);
        }
    }

    public void moveToNearestTarget(List<Target> targets) {
        if (!canMove || targets.isEmpty()) {
            return;
        }

        Target nearestTarget = null;
        int minDistance = Integer.MAX_VALUE;

        for (Target target : targets) {
            int distance = calculateDistance(target.getX(), target.getY());

            if (distance < minDistance) {
                nearestTarget = target;
                minDistance = distance;
            }
        }

        if (nearestTarget != null) {
            moveTowardsPoint(nearestTarget.getX(), nearestTarget.getY());

            int tolerance=15;

            if (Math.abs(this.getX() - nearestTarget.getX()) <= tolerance &&
                    Math.abs(this.getY() - nearestTarget.getY()) <= tolerance) {
                window.removeCivil(this);
            }
        }
    }

    private int calculateDistance(int targetX, int targetY) {
        int distanceX = targetX - this.getX();
        int distanceY = targetY - this.getY();
        return Math.abs(distanceX) + Math.abs(distanceY);
    }

    public List<Target> createTargets() {
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(0, 560));
        targets.add(new Target(959, 720));
        targets.add(new Target(447, 54));

        return targets;
    }
}
