package fr.miroff.FouleProject.character;

import java.awt.*;

public enum Character {

    BANDIT(Color.RED),
    CIVIL(Color.BLACK),
    COP(Color.BLUE);

    public final Color color;
    public int count;

    Character(Color color) {
        this.color = color;
    }
}
