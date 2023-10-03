package fr.miroff.FouleProject.character;

import fr.miroff.FouleProject.coord.Coordinate;

import java.awt.*;

public class Personnage {
    protected Object deplacement;
    protected int health;



    private int x;
    private int y;


    //constructeur
    public Personnage(int x, int y ) {
        this.x = x;
        this.y = y;
    }


    public void setDeplacement(Object deplacement) {
        double a = Math.random();
        double b = Math.random();
        if (a < 0.5 || x < -1000){
            x += 1;
        }
        else{
            x -= 1;
        }

        if (b < 0.5 || y < 1000){
            y += 1;
        }
        else
            y -= 1;
        this.deplacement = deplacement;
    }

    public boolean hurt(){
        this.health -= 1;
        return this.health == 0;
    }

}

