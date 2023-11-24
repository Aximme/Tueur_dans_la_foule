package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.Window;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class Civil extends Character {
    private static final Random RAND = new Random();

    public Civil(int x, int y, int movementspeed, Window window) {
        super(x, y, movementspeed, window);
        this.health = 1;
        this.window = window;
    }

    public boolean escape() {
        return RAND.nextInt(3) == 0;
    }

    public void moveTowardsPoint(int targetX, int targetY) {
        if (!canMove) {
            return;
        }

        // Calculer les vecteurs de distance
        int distanceX = targetX - this.getX();
        int distanceY = targetY - this.getY();

        // Se déplacer
        if (distanceX > 0 && this.getX() < targetX) {
            this.setX(this.getX() + movementSpeed);
        } else if (distanceX < 0 && this.getX() > targetX) {
            this.setX(this.getX() - movementSpeed);
        }

        if (distanceY > 0 && this.getY() < targetY) {
            this.setY(this.getY() + movementSpeed);
        } else if (distanceY < 0 && this.getY() > targetY) {
            this.setY(this.getY() - movementSpeed);
        }
    }

    public void moveToNearestTarget(List<Target> targets) {
        if (!canMove || targets.isEmpty()) {
            return;
        }

        Target nearestTarget = targets.get(0);
        int minDistance = calculateDistance(nearestTarget.getX(), nearestTarget.getY());

        for (Target target : targets) {
            int distance = calculateDistance(target.getX(), target.getY());
            if (distance < minDistance) {
                nearestTarget = target;
                minDistance = distance;
            }
        }

        moveTowardsPoint(nearestTarget.getX(), nearestTarget.getY());

        if (this.getX() == nearestTarget.getX() && this.getY() == nearestTarget.getY()) {
            window.removeCivil(this);

        }
    }

    private int calculateDistance(int targetX, int targetY) {
        int distanceX = targetX - this.getX();
        int distanceY = targetY - this.getY();
        return Math.abs(distanceX) + Math.abs(distanceY);
    }
    public List<Target> createTargets() {
        List<Target> targets = new ArrayList<>();
        targets.add(new Target(0, 380));
        targets.add(new Target(725, 0));
        targets.add(new Target(725, 860));
        targets.add(new Target(1430, 380));

        return targets;
    }
}