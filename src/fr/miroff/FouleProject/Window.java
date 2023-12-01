package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Bandit;
import fr.miroff.FouleProject.character.Character;
import fr.miroff.FouleProject.character.Civil;
import fr.miroff.FouleProject.character.Cop;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;



public class Window extends JFrame {
    public static final int WINDOW_WIDTH = 1450;
    public static final int WINDOW_HEIGHT = 860;
    private int banditRemaining = 1;
    private int civilRemaining = 1;
    private int copRemaining = 1;
    public static final CopyOnWriteArrayList<Character> characters = new CopyOnWriteArrayList<>();
    private final JPanel drawingPanel;
    private final JSlider speedSlider;
    private int banditCount = 0;
    private int escapedCount = 0;
    private int civilCount = 0;
    private int copCount = 0;
    private int banditDeaths = 0;
    private int civilDeaths = 0;
    private int copDeaths = 0;
    private int movementSpeed = 1;
    private final JLabel banditCounterLabel;
    private final JLabel civilCounterLabel;
    private final JLabel copCounterLabel;
    private final JLabel deathsLabel;
    private final SummaryWindow summaryWindow;
    private final Image backgroundImage;
    private Building circularBuilding;
    private final JLabel escapedCounterLabel;

    private void generateCharacters() {
        Random rand = new Random();
        characters.clear();
        escapedCount = 0;
        banditCount = 0;
        civilCount = 0;
        copCount = 0;
        banditDeaths = 0;
        civilDeaths = 0;
        copDeaths = 0;

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

        for (Character character : characters) {
            character.setMovementSpeed(movementSpeed);

        }

        updateCounters();

        //TODO : Add timer for bandit spawn.
    }

    private boolean isNearBuilding(int x, int y) {
        int distanceThreshold = 20; // Possible d'ajuster la distance

        for (Building building : buildings) {
            if (building.isCircular()) {
                // Pour les bâtiments circulaires
                int buildingX = building.getX();
                int buildingY = building.getY();
                int buildingRadius = building.getRadius();

                double distance = Math.sqrt(Math.pow(x - buildingX, 2) + Math.pow(y - buildingY, 2));

                if (distance <= buildingRadius + distanceThreshold) {
                    return true;
                }
            } else {
                // Pour les bâtiments rectangulaires
                int buildingX = building.getX();
                int buildingY = building.getY();
                int buildingWidth = building.getWidth();
                int buildingHeight = building.getHeight();

                if (x >= buildingX - distanceThreshold && x <= buildingX + buildingWidth + distanceThreshold &&
                        y >= buildingY - distanceThreshold && y <= buildingY + buildingHeight + distanceThreshold) {
                    return true;
                }
            }
        }

        return false;
    }



    public final ArrayList<Building> buildings = new ArrayList<>();

    protected void generateBuildings() {
        buildings.clear();
        buildings.add(new Building(0, 0, 435, 60));//rectangle en haut a gauche avant evac
        buildings.add(new Building(480, 0, 1030, 107));//Barre batiments haut de evac a droite extreme
        buildings.add(new Building(980, 47, 470, 140));//rectangle en haut a droite
        buildings.add(new Building(1385, 0, 70, 780));//barre droite extreme
        buildings.add(new Building(980, 595, 500, 350));//eau en bas a droite
        buildings.add(new Building(525, 620, 385, 150));//eau milieu
        buildings.add(new Building(0, 630, 494, 150));//eau bas a gauche
        buildings.add(new Building(187, 312, 40));//big fontaine gauche
        buildings.add(new Building(323, 210, 21));//gauche en haut
        buildings.add(new Building(319, 424, 21));//gauche en bas
        buildings.add(new Building(903, 298, 49));//rond point centre
        buildings.add(new Building(1079,214,140,130));//pyramide louvre
        buildings.add(new Building(1085, 290, 140, 45));//pyramide louvre (2)
        buildings.add(new Building(480, 530, 100, 45));//bats ligne du bas
        buildings.add(new Building(580, 520, 150, 40));//bats ligne du bas (2)
        buildings.add(new Building(710, 485, 125, 60));//bats ligne du bas (gros bat) (3)
        buildings.add(new Building(830, 502, 60, 30));//bats ligne du bas (ligne fine) (1)
        buildings.add(new Building(965, 490, 60, 30));//bats ligne du bas (ligne fine) (2)
        buildings.add(new Building(1005, 390, 350, 120));//gros carre droite (1)
        buildings.add(new Building(1290, 375, 100, 120));//gros carre droite (2)
    }

    private boolean noMoreBandits() {
        return (banditCount == 0);
    }

    private boolean noMoreCivilandCops() {
        return (civilCount == 0 && copCount == 0);
    }

    private boolean noMoreCivils() {
        return (civilCount == 0);
    }

    private void stopSimulation() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) { //TODO: Find another fix to delete point after ending simulation.
            e.printStackTrace();
        }
        Character.stopMovements();
        displaySummaryWindow();
    }


    private void displaySummaryWindow() {
        summaryWindow.displaySummary(banditCount, civilCount, copCount, banditDeaths, civilDeaths, copDeaths, escapedCount);
        summaryWindow.setVisible(true);
    }

    private String getEndingMessage() {
        if (banditCount == 0) {
            JOptionPane.showMessageDialog(this, "✅ Simulation terminée. Il n'y a plus de bandits en vie.");
        }

        if (civilCount == 0) {
            JOptionPane.showMessageDialog(this, "✅ Simulation terminée. Il n'y a plus de civils en vie.");
        }

        if (banditCount == 0 && copCount == 0) {
            JOptionPane.showMessageDialog(this, "✅ Simulation terminée. Il n'y a plus de policiers ni de bandits en vie.");
        } else {
            JOptionPane.showMessageDialog(this,"⚠️ Simulation stoppée avant la fin");
        }
        return null;
    }
    protected void handleCollisions(Character character) {
        final double MIN_DISTANCE_ATTACK = 10.0;
        final double MIN_DISTANCE = 4;

        for (Character otherCharacter : characters) {
            if (otherCharacter != character && character.distanceBetween(otherCharacter) < MIN_DISTANCE) {
                if (!(character instanceof Bandit && otherCharacter instanceof Civil) &&
                        !(character instanceof Cop && otherCharacter instanceof Bandit)) {
                    character.adjustPositions(otherCharacter);
                } else if (character.distanceBetween(otherCharacter) < MIN_DISTANCE_ATTACK) {
                    character.interact(otherCharacter);
                }
            }
        }
    }

    public Window() {
        setTitle("Tueurs dans la Foule !");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //TODO : remove after positioning buildings !!
        //generateBuildings();

        backgroundImage = new ImageIcon("src/fr/miroff/FouleProject/img/louvre.png").getImage();

        banditCounterLabel = new JLabel("🥷 Bandits en Vie : " + banditCount);
        civilCounterLabel = new JLabel("👤 Civils en vie : " + civilCount);
        copCounterLabel = new JLabel("🚓 Policiers en vie : " + copCount);
        escapedCounterLabel = new JLabel(" 🫥 Nombre d'échappés : " + escapedCount);

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


                generateBuildings();
                //generateCircularBuildings();
                generateCharacters();
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
        countersPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        countersPanel.add(Box.createHorizontalGlue());
        countersPanel.add(escapedCounterLabel);

        countersPanel.add(Box.createRigidArea(new Dimension(10, 0)));


        add(countersPanel, BorderLayout.SOUTH);
        setVisible(true);

        summaryWindow = new SummaryWindow(this, banditDeaths, civilDeaths, copDeaths, escapedCount);
    }

    private void drawCircles(Graphics g) {
        ArrayList<Character> charactersCopy = new ArrayList<>(characters);

        for (Character character : charactersCopy) {
            if (character instanceof Bandit) {
                g.setColor(Color.RED);
            } else if (character instanceof Civil) {
                g.setColor(Color.WHITE);
            } else if (character instanceof Cop) {
                g.setColor(Color.BLUE);
            }
            drawCircle(g, character.getX(), character.getY(), 7);
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
        escapedCounterLabel.setText("Nombre d'échappés : " + escapedCount);
        deathsLabel.setText("Morts - 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths + "🫥 Civils échappés" + escapedCount);
    }

    public void updateCounters(Character character) {
        if (character instanceof Bandit) {
            banditCount--;
            banditDeaths++;
        } else if (character instanceof Civil) {
            civilCount--;
            civilDeaths++;
        } else if (character instanceof Cop) {
            copCount--;
            copDeaths++;
        }


        updateCounters();

        if (noMoreBandits()) {
            stopSimulation();
        }

        if (noMoreCivilandCops()) {
            stopSimulation();
        }
        if (noMoreCivils()) {
            stopSimulation();
        }
    }
    public void removeCivil(Civil civil) {     //TODO: Mettre à jour le compteur en live sur l"interface graphique.
        characters.remove(civil);
        escapedCount++;
        updateCounters();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Window();
        });
    }
}


    class SummaryWindow extends JFrame {
        private final JLabel banditLabel;
        private final JLabel civilLabel;
        private final JLabel copLabel;
        private final JLabel deathsLabel;

        private final JLabel escapedCounterLabel;

        private final JLabel testLabel;


        public SummaryWindow(Window mainFrame, int banditDeaths, int copDeaths, int civilDeaths, int escapedCount) {
            setTitle("Résumé de la Simulation");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            setLocationRelativeTo(mainFrame);

            banditLabel = new JLabel("🥷 Bandits : ");
            civilLabel = new JLabel("👤 Civils : ");
            copLabel = new JLabel("🚓 Policiers : ");
            escapedCounterLabel = new JLabel(" 🫥 nombre échappées :");
            testLabel = new JLabel("Morts ------------------------");
            deathsLabel = new JLabel(" 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);

            JPanel summaryPanel = new JPanel();
            summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.PAGE_AXIS));
            summaryPanel.add(banditLabel);
            summaryPanel.add(civilLabel);
            summaryPanel.add(copLabel);
            summaryPanel.add(escapedCounterLabel);
            summaryPanel.add(testLabel);
            summaryPanel.add(deathsLabel);

            add(summaryPanel, BorderLayout.CENTER);

            setVisible(false);
        }

        public void displaySummary(int banditCount, int civilCount, int copCount, int banditDeaths, int civilDeaths, int copDeaths, int escapedCount) {


            banditLabel.setText("🥷 Bandits en vie : " + banditCount);
            civilLabel.setText("👤 Civils en vie  : " + civilCount);
            copLabel.setText("🚓 Policiers en vie : " + copCount);
            escapedCounterLabel.setText("🫥 Civils échapés : " + escapedCount);
            copLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
            testLabel.setText("--------- Recap Agents Morts ---------");
            deathsLabel.setText(" 🥷 Bandits: " + banditDeaths + " 👤 Civils: " + civilDeaths + " 🚓 Policiers: " + copDeaths);
        }
    }

