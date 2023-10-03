package fr.miroff.FouleProject.character;

import java.awt.Point;




public class ListePerso {
    private int[][] pointMatrix;

    public ListePerso(int width, int height) {
        pointMatrix = new int[width][height];
    }

    public void addPoint(Point point, int type) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        pointMatrix[x][y] = type;
    }

    public int[][] getListePerso() {
        return pointMatrix;
    }

    public void afficherMatrice() {
        for (int x = 0; x < pointMatrix.length; x++) {
            for (int y = 0; y < pointMatrix[x].length; y++) {
                System.out.print(pointMatrix[x][y] + " ");
            }
            System.out.println(); // Passer à la ligne après chaque ligne de la matrice
        }
    }
}
