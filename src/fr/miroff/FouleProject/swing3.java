package fr.miroff.FouleProject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;


public class swing3 extends JFrame {
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 400;
    private final int NUM_IMAGES = 3;

    private List<ResizableImage> images;

    public swing3() {
        setTitle("Multiple Images Display");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre au milieu de l'écran

        images = new ArrayList<>();
        images.add(new ResizableImage(new ImageIcon("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/10-10_resized/black_circle.png").getImage()));
        images.add(new ResizableImage(new ImageIcon("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/10-10_resized/blue_circle.png").getImage()));
        images.add(new ResizableImage(new ImageIcon("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/10-10_resized/red_circle.png").getImage()));
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawImages(g);
            }
        };

        add(panel);
        setVisible(true);
    }

    private void drawImages(Graphics g) {
        Random rand = new Random();

        for (int i = 0; i < NUM_IMAGES; i++) {
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);

            ResizableImage resizableImage = images.get(i);
            resizableImage.draw(g, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new swing3();
        });
    }
}

class ResizableImage {
    private Image image;

    public ResizableImage(Image image) {
        this.image = image;
    }

    public void draw(Graphics g, int x, int y) {
        // Dessinez l'image à la position spécifiée (x, y)
        g.drawImage(image, x, y, null);
    }
}


class Bouton extends JButton{
    private String name;
    private Image img;

    public Bouton(String str){
        super(str);
        this.name = str;
        try {
            img = ImageIO.read(new File("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/10-10_resized/fondBouton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        GradientPaint gp = new GradientPaint(0, 0, Color.blue, 0, 20, Color.cyan, true);
        g2d.setPaint(gp);
        g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
        g2d.setColor(Color.black);
        g2d.drawString(this.name, this.getWidth() / 2 - (this.getWidth() / 2 /4), (this.getHeight() / 2) + 5);
    }
}