package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Character;
import fr.miroff.FouleProject.character.Civil;
import fr.miroff.FouleProject.character.Target;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static Window mainWindow;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            mainWindow = new Window();
            mainWindow.generateBuildings();
        });

        try (final ExecutorService executor = Executors.newFixedThreadPool(10)) {
            while (true) {
                CountDownLatch latch = new CountDownLatch(Window.characters.size());

                for (Character character : Window.characters) {
                    executor.submit(() -> {
                        if (character instanceof Civil) {
                            Civil civil = (Civil) character;
                            List<Target> targets = civil.createTargets();
                            civil.moveToNearestTarget(targets);
                        } else {
                            character.move();
                        }
                        mainWindow.handleCollisions(character);
                        latch.countDown();
                    });
                }

                try {
                    latch.await();

                    Window.characters.removeIf(character -> !character.isAlive());

                    if (mainWindow != null) {
                        mainWindow.display();
                    }

                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}