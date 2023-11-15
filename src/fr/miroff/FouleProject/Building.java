package fr.miroff.FouleProject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        //g.setColor(Color.BLACK);
        //g.fillRect(x, y, width, height);
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage textureImage = null;
        try {
            textureImage = ImageIO.read(new File("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/mur-briques.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Rectangle2D anchor = new Rectangle2D.Double(x, y, textureImage.getWidth(), textureImage.getHeight());

        TexturePaint texturePaint = new TexturePaint(textureImage, anchor);

        g2d.setPaint(texturePaint);

        g2d.fillRect(x, y, width, height);
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


}
