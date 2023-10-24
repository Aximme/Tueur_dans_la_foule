package fr.miroff.FouleProject;

import java.awt.*;

public class Building {
    private int x;
    private int y;
    private int width;
    private int height;

    public Building(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }
}