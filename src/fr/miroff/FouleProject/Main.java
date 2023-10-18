package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Character;

import javax.swing.*;
import java.util.concurrent.*;

public class Main {

    private static Window mainWindow;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> mainWindow = new Window());

        try(final ExecutorService executor = Executors.newFixedThreadPool(10)){
            while (true) {
                CountDownLatch latch = new CountDownLatch(Window.characters.size());

                for (Character character : Window.characters) {
                    executor.submit(() -> {
                        character.move(Window.characters);
                        latch.countDown();
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
}
