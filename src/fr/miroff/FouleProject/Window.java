package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Bandit;
import fr.miroff.FouleProject.character.Civil;
import fr.miroff.FouleProject.character.Cop;
import fr.miroff.FouleProject.character.Character;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JFrame {
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 800;
    private int banditRemaining = 1;
    private int civilRemaining = 1;
    private int copRemaining = 1;
    public static final ArrayList<Character> characters = new ArrayList<>();
    private JPanel drawingPanel;
    private JSlider speedSlider;
    private int banditCount = 0;
    private int civilCount = 0;
    private int copCount = 0;
    private int banditDeaths = 0;
    private int civilDeaths = 0;
    private int copDeaths = 0;
    private int movementSpeed = 1;
    private JLabel banditCounterLabel;
    private JLabel civilCounterLabel;
    private JLabel copCounterLabel;
    private JLabel deathsLabel;
    private SummaryWindow summaryWindow;
    private Image backgroundImage;

    private void generateCharacters() {
        Random rand = new Random();
        characters.clear();

        banditCount = 0;
        civilCount = 0;
        copCount = 0;
        banditDeaths = 0;
        civilDeaths = 0;
        copDeaths = 0;

        for (int i = 0; i < banditRemaining; i++) {
            int x, y;
            boolean isNearBuilding;
            do {
                x = rand.nextInt(WINDOW_WIDTH);
                y = rand.nextInt(WINDOW_HEIGHT - 100);
                isNearBuilding = isNearBuilding(x, y);
            } while (isNearBuilding);

            characters.add(new Bandit(x, y, movementSpeed, this));
            banditCount++;
        }

        for (int i = 0; i < civilRemaining; i++) {
            int x, y;
            boolean isNearBuilding;
            do {
                x = rand.nextInt(WINDOW_WIDTH);
                y = rand.nextInt(WINDOW_HEIGHT - 100);
                isNearBuilding = isNearBuilding(x, y);
            } while (isNearBuilding);

            characters.add(new Civil(x, y, movementSpeed, this));
            civilCount++;
        }

        for (int i = 0; i < copRemaining; i++) {
            int x, y;
            boolean isNearBuilding;
            do {
                x = rand.nextInt(WINDOW_WIDTH);
                y = rand.nextInt(WINDOW_HEIGHT - 100);
                isNearBuilding = isNearBuilding(x, y);
            } while (isNearBuilding);

            characters.add(new Cop(x, y, movementSpeed, this));
            copCount++;
        }

        for (Character character : characters) {
            character.setMovementSpeed(movementSpeed);
        }
        for (Character character : characters) {
            character.setBuildings(buildings);
        }

        updateCounters();
    }

    private boolean isNearBuilding(int x, int y) { ///boolean qui va servir à savori si le personnage créer et assez loin d'un batiment
        int distanceThreshold = 300; // possible d'ajuster la distance
        for (Building building : buildings) {
            int buildingX = building.getX();
            int buildingY = building.getY();
            int buildingWidth = building.getWidth();
            int buildingHeight = building.getHeight();

            if (x >= buildingX - distanceThreshold && x <= buildingX + buildingWidth + distanceThreshold &&
                    y >= buildingY - distanceThreshold && y <= buildingY + buildingHeight + distanceThreshold) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Building> buildings = new ArrayList<>();

    private void generateBuildings() {
        buildings.clear();
        buildings.add(new Building(100, 200, 50, 150));
        buildings.add(new Building(300, 100, 80, 120));
        buildings.add(new Building(200, 400, 80, 120));


    }

    private boolean noMoreBandits() {
        return (banditCount == 0);
    }

    private boolean noMoreCivilandCops() {
        return (civilCount == 0 && copCount == 0);
    }

    private void stopSimulation() {
        Character.stopMovements();
        displaySimulationFinishedWindow();
        displaySummaryWindow();
    }

    private void displaySimulationFinishedWindow() {
        if (banditCount == 0) {
            JOptionPane.showMessageDialog(this, "Simulation terminée. Il n'y a plus de bandits en vie.");
        }

        if (civilCount == 0) {
            JOptionPane.showMessageDialog(this, "Simulation terminée. Il n'y a plus de civils ni de policiers en vie.");
        }

        if (banditCount == 0 && copCount == 0) {
            JOptionPane.showMessageDialog(this, "Simulation terminée. Il n'y a plus de policiers ni de bandits en vie.");
        }
    }

    private void displaySummaryWindow() {
        summaryWindow.displaySummary(banditCount, civilCount, copCount, banditDeaths, civilDeaths, copDeaths);
        summaryWindow.setVisible(true);
    }

    public Window() {
        setTitle("Tueurs dans la Foule !");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon("/Users/maxime/Desktop/Université/Projet Informatique/Tueur_dans_la_foule/src/fr/miroff/FouleProject/img/DALL·E 2023-11-07 09.14.38_crop.png").getImage();

        banditCounterLabel = new JLabel("🥷 Bandits en Vie : " + banditCount);
        civilCounterLabel = new JLabel("👤 Civils en vie : " + civilCount);
        copCounterLabel = new JLabel("🚓 Policiers en vie : " + copCount);

        deathsLabel = new JLabel("Morts - 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);

        drawingPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
                drawCircles(g);
            }
        };

        add(drawingPanel);

        JPanel controlPanel = new JPanel(new FlowLayout());

        JTextField redTextField = new JTextField(5);
        JTextField blackTextField = new JTextField(5);
        JTextField blueTextField = new JTextField(5);

        JLabel redLabel = new JLabel("🥷 Bandits : ");
        JLabel blackLabel = new JLabel("👤 Civils : ");
        JLabel blueLabel = new JLabel("🚓 Policiers : ");

        JButton generateButton = new JButton("START");
        generateButton.setBackground(Color.GREEN);
        generateButton.setForeground(Color.GREEN);
        generateButton.addActionListener(e -> {
            Character.resumeMovements();
            try {
                banditRemaining = Integer.parseInt(redTextField.getText());
                civilRemaining = Integer.parseInt(blackTextField.getText());
                copRemaining = Integer.parseInt(blueTextField.getText());

                generateCharacters();
                generateBuildings();
                repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠ Entrée incorrecte.");
            }
        });

        JButton stopButton = new JButton("STOP");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.RED);
        stopButton.addActionListener(e -> {
            Character.stopMovements();
            stopSimulation();
        });

        speedSlider = new JSlider(1, 20, movementSpeed);
        speedSlider.setMajorTickSpacing(5);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                movementSpeed = source.getValue();
                for (Character character : characters) {
                    character.setMovementSpeed(movementSpeed);
                }
            }
        });

        controlPanel.add(speedSlider);
        add(controlPanel, BorderLayout.NORTH);
        setVisible(true);

        controlPanel.add(redLabel);
        controlPanel.add(redTextField);
        controlPanel.add(blackLabel);
        controlPanel.add(blackTextField);
        controlPanel.add(blueLabel);
        controlPanel.add(blueTextField);
        controlPanel.add(generateButton);
        controlPanel.add(stopButton);
        add(controlPanel, BorderLayout.NORTH);
        setVisible(true);

        JPanel countersPanel = new JPanel();
        countersPanel.setLayout(new BoxLayout(countersPanel, BoxLayout.LINE_AXIS));

        countersPanel.add(Box.createHorizontalGlue());
        countersPanel.add(banditCounterLabel);
        countersPanel.add(Box.createHorizontalGlue());
        countersPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        countersPanel.add(civilCounterLabel);
        countersPanel.add(Box.createHorizontalGlue());
        countersPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        countersPanel.add(copCounterLabel);
        countersPanel.add(Box.createHorizontalGlue());

        add(countersPanel, BorderLayout.SOUTH);
        setVisible(true);

        summaryWindow = new SummaryWindow(this, banditDeaths, civilDeaths, copDeaths);
    }

    private void drawCircles(Graphics g) {
        ArrayList<Character> charactersCopy = new ArrayList<>(characters);

        for (Character character : charactersCopy) {
            if (character instanceof Bandit) {
                g.setColor(Color.RED);
            } else if (character instanceof Civil) {
                g.setColor(Color.BLACK);
            } else if (character instanceof Cop) {
                g.setColor(Color.BLUE);
            }
            drawCircle(g, character.getX(), character.getY(), 10);
        }
        for (Building building : buildings) {
            building.draw(g);
        }
    }

    public void display() {
        drawingPanel.repaint();
    }

    private void drawCircle(Graphics g, int x, int y, int size) {
        g.fillOval(x, y, size, size);
    }

    public void updateCounters() {
        banditCounterLabel.setText("🥷 Bandits en Vie : " + banditCount);
        civilCounterLabel.setText("👤 Civils en vie : " + civilCount);
        copCounterLabel.setText("🚓 Policiers en vie : " + copCount);
        deathsLabel.setText("Morts - 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);
    }

    public void updateCounters(Character character) {
        if (character instanceof Bandit) {
            banditCount--;
        } else if (character instanceof Civil) {
            civilCount--;
        } else if (character instanceof Cop) {
            copCount--;
        }
        if (character instanceof Bandit) {
            banditDeaths++;
        } else if (character instanceof Civil) {
            civilDeaths++;
        } else if (character instanceof Cop) {
            copDeaths++;
        }
        updateCounters();

        if (noMoreBandits()) {
            stopSimulation();
        }

        if (noMoreCivilandCops()) {
            stopSimulation();
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Window();
        }
    } 
}


class SummaryWindow extends JFrame {
    private JLabel banditLabel;
    private JLabel civilLabel;
    private JLabel copLabel;
    private JLabel deathsLabel;
    private JLabel testLabel;
    private int banditDeaths;

    public SummaryWindow(Window mainFrame, int banditDeaths, int copDeaths, int civilDeaths) {
        setTitle("Résumé de la Simulation");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(mainFrame);

        banditLabel = new JLabel("🥷 Bandits : ");
        civilLabel = new JLabel("👤 Civils : ");
        copLabel = new JLabel("🚓 Policiers : ");
        testLabel = new JLabel("Morts ------------------------");
        deathsLabel = new JLabel(" 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);

        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.PAGE_AXIS));
        summaryPanel.add(banditLabel);
        summaryPanel.add(civilLabel);
        summaryPanel.add(copLabel);
        summaryPanel.add(testLabel);
        summaryPanel.add(deathsLabel);

        add(summaryPanel, BorderLayout.CENTER);

        setVisible(false);
    }

    public void displaySummary(int banditCount, int civilCount, int copCount, int banditDeaths, int civilDeaths, int copDeaths) {
        banditLabel.setText("🥷 Bandits en vie : " + banditCount);
        civilLabel.setText("👤 Civils en vie  : " + civilCount);
        copLabel.setText("🚓 Policiers en vie : " + copCount);
        copLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        testLabel.setText("--------- Recap Agents Morts ---------");
        deathsLabel.setText(" 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);
    }
}
