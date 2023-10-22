package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.Bandit;
import fr.miroff.FouleProject.character.Civil;
import fr.miroff.FouleProject.character.Cop;
import fr.miroff.FouleProject.character.Character;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Window extends JFrame {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 650;
    private int banditRemaining = 1;
    private int civilRemaining = 1;
    private int copRemaining = 1;
    public static final ArrayList<Character> characters = new ArrayList<>();
    private JPanel drawingPanel;
    private JSlider speedSlider;
    private int banditCount = 0;
    private int civilCount = 0;
    private int copCount = 0;
    private int movementSpeed = 1;
    private JLabel banditCounterLabel;
    private JLabel civilCounterLabel;
    private JLabel copCounterLabel;

    private void generateCharacters() {
        Random rand = new Random();
        characters.clear();

        banditCount = 0;
        civilCount = 0;
        copCount = 0;

        for (int i = 0; i < banditRemaining; i++) {
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            characters.add(new Bandit(x, y, movementSpeed, this));
            banditCount++;
        }

        for (int i = 0; i < civilRemaining; i++) {
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            characters.add(new Civil(x, y, movementSpeed, this));
            civilCount++;
        }

        for (int i = 0; i < copRemaining; i++) {
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            characters.add(new Cop(x, y, movementSpeed, this));
            copCount++;
        }

        for (Character character : characters) {
            character.setMovementSpeed(movementSpeed);
        }

        updateCounters();
    }

    public Window() {
        setTitle("Tueurs dans la Foule !");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        banditCounterLabel = new JLabel("🥷 Bandits en Vie : " + banditCount);
        civilCounterLabel = new JLabel("👤 Civils en vie : " + civilCount);
        copCounterLabel = new JLabel("🚓 Policiers en vie : " + copCount);

        drawingPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCircles(g);
            }
        };
        add(drawingPanel);

        JPanel controlPanel = new JPanel(new FlowLayout());

        JTextField redTextField = new JTextField(5);
        JTextField blackTextField = new JTextField(5);
        JTextField blueTextField = new JTextField(5);

        JLabel redLabel = new JLabel("Bandits : ");
        JLabel blackLabel = new JLabel("Civils : ");
        JLabel blueLabel = new JLabel("Policiers : ");

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
    }

    public void updateCounters(Character character) {
        if (character instanceof Bandit) {
            banditCount--;
        } else if (character instanceof Civil) {
            civilCount--;
        } else if (character instanceof Cop) {
            copCount--;
        }
        updateCounters();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Window();
        });
    }
}
