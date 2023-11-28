package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Character;
import fr.miroff.FouleProject.character.Civil;
import fr.miroff.FouleProject.character.Target;
import javax.swing.*;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static Window mainWindow;

    public static void main(String[] args) {

        CountDownLatch buildingsReadyLatch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
        mainWindow = new Window();
        mainWindow.generateBuildings();
        buildingsReadyLatch.countDown();
    });

        SwingUtilities.invokeLater(() -> mainWindow = new Window());



        try(final ExecutorService executor = Executors.newFixedThreadPool(10)){
            while (true) {
                CountDownLatch latch = new CountDownLatch(Window.characters.size());

                for (Character character : Window.characters) {
                    executor.submit(() -> {
                        try {
                            if (character instanceof Civil) {
                                Civil civil = (Civil) character;
                                List<Target> targets = civil.createTargets();
                                civil.moveToNearestTarget(targets);
                            } else {
                                character.move(Window.characters);
                            }
                            mainWindow.handleCollisions(character);
                            latch.countDown();
                        } catch (Exception e) {
                            handleUncaughtException(e);
                        }
                    });
                }

                try {
                    latch.await();

                    Window.characters.removeIf(character -> !character.isAlive());

                    if (mainWindow != null) {
                        mainWindow.display();
                    }

                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    private static void handleUncaughtException(Throwable throwable) {
        System.err.println("Uncaught exception in thread: " + Thread.currentThread().getName());
        throwable.printStackTrace();
    }
}