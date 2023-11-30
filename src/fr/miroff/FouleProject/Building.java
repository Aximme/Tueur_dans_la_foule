package fr.miroff.FouleProject;

import java.awt.*;

public class Building {
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isCircular;
    private int radius;

    //Constructeur rectangle noir/texture
    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isCircular = false;
    }

    //Constructeur bâtiment circulaire transparent
    public Building(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.isCircular = true;
    }

    public void draw(Graphics g) {
        if (isCircular) {
            g.setColor(Color.RED);//Couleur rouge pour visualiser, sera transparent dans le futur
            g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}