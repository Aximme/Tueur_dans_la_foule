package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Personnage;

import javax.swing.*;
import java.util.Scanner;
import java.util.concurrent.*;

public class Main {

    private static Window mainWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> mainWindow = new Window());

        try(final ExecutorService executor = Executors.newFixedThreadPool(10)){
            while (true) {
                CountDownLatch latch = new CountDownLatch(Window.characters.size());

                for (Personnage character : Window.characters) {
                    executor.submit(() -> {
                        character.move();
                        latch.countDown();
                    });
                }

                try {
                    latch.await();

                    if (mainWindow != null) {
                        mainWindow.display();
                    }

                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
