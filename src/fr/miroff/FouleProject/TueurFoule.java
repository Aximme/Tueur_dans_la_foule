package fr.miroff.FouleProject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class TueurFoule extends JFrame {
    private final int WINDOW_WIDTH = 600;
    private final int WINDOW_HEIGHT = 400;
    private int NbrBandit = 1;
    private int NbrCivil = 1;
    private int NbrPolicier = 1;
    private Point[] circlePositions;
    private Timer movementTimer;
    private JSlider speedSlider;

    public TueurFoule() {
        setTitle("Tueurs dans la Foule !");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        circlePositions = new Point[NbrBandit + NbrCivil + NbrPolicier];
        initializeCirclePositions();

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCircles(g);
            }
        };
        add(panel);

        JPanel controlPanel = new JPanel(new FlowLayout());

        JTextField redTextField = new JTextField(5);
        JTextField blackTextField = new JTextField(5);
        JTextField blueTextField = new JTextField(5);

        JLabel redLabel = new JLabel("Bandits: ");
        JLabel blackLabel = new JLabel("Civils: ");
        JLabel blueLabel = new JLabel("Policiers: ");

        JButton generateButton = new JButton("START");
        generateButton.setBackground(Color.GREEN);
        generateButton.setForeground(Color.GREEN);
        generateButton.addActionListener(e -> {
            try {
                NbrBandit = Integer.parseInt(redTextField.getText());
                NbrCivil = Integer.parseInt(blackTextField.getText());
                NbrPolicier = Integer.parseInt(blueTextField.getText());
                initializeCirclePositions();
                startMovement();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Entrez un nombre correct svp");
            }
        });

        JButton stopButton = new JButton("STOP");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.RED);
        stopButton.addActionListener(e -> {
            stopMovement();
        });

        //Slide de vitesse
        speedSlider = new JSlider(1, 50, 1); //Base à 1 --> va de 1 à 100
        controlPanel.add(speedSlider);
// Ajoutez un écouteur de changement pour le curseur de vitesse global
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateCircleSpeed(speedSlider.getValue());
            }
        });


        controlPanel.add(redLabel);
        controlPanel.add(redTextField);
        controlPanel.add(blackLabel);
        controlPanel.add(blackTextField);
        controlPanel.add(blueLabel);
        controlPanel.add(blueTextField);
        controlPanel.add(generateButton);
        controlPanel.add(stopButton);

        add(controlPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());

        add(buttonPanel, BorderLayout.SOUTH);

        movementTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCirclePositions();
                repaint();
            }
        });

        setVisible(true);
    }

    private final int MAX_SPEED = 5; // Vmax cercles
    private Point[] circleVelocities; // Stockages vitesse des cercles conseillé par internet


    private void updateCircleSpeed(int speed) {
        for (int i = 0; i < NbrBandit + NbrCivil + NbrPolicier; i++) {
            Point velocity = circleVelocities[i];
            // Mettez à jour la vitesse en fonction de la valeur du curseur global
            velocity.x = speed;
            velocity.y = speed;
            circleVelocities[i] = velocity;
        }
    }

    private void initializeCirclePositions() {
        int totalCircles = NbrBandit + NbrCivil + NbrPolicier;
        circlePositions = new Point[totalCircles];
        circleVelocities = new Point[totalCircles]; // Tableau pour les vitesses
        Random rand = new Random();

        for (int i = 0; i < totalCircles; i++) {
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            circlePositions[i] = new Point(x, y);

            // Attribuez une vitesse aléatoire
            int vx = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
            int vy = rand.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED;
            circleVelocities[i] = new Point(vx, vy);
        }
    }
    private void updateCirclePositions() {
        for (int i = 0; i < NbrBandit + NbrCivil + NbrPolicier; i++) {
            Point position = circlePositions[i];
            Point velocity = circleVelocities[i];

            int newX = position.x + velocity.x;
            int newY = position.y + velocity.y;

            // Assurez-vous que les cercles rebondissent sur les bords de la fenêtre
            if (newX < 0 || newX > WINDOW_WIDTH - 10) {
                velocity.x = -velocity.x; // Inverse la composante horizontale de la vitesse
                newX = Math.max(0, Math.min(newX, WINDOW_WIDTH - 10));
            }
            if (newY < 0 || newY > WINDOW_HEIGHT - 10) {
                velocity.y = -velocity.y; // Inverse la composante verticale de la vitesse
                newY = Math.max(0, Math.min(newY, WINDOW_HEIGHT - 10));
            }

            circlePositions[i] = new Point(newX, newY);
            circleVelocities[i] = velocity;
        }
    }

    private void drawCircles(Graphics g) {
        g.setColor(Color.RED);
        for (int i = 0; i < NbrBandit; i++) {
            Point position = circlePositions[i];
            drawCircle(g, position.x, position.y, 10);
        }

        g.setColor(Color.BLACK);
        for (int i = NbrBandit; i < NbrBandit + NbrCivil; i++) {
            Point position = circlePositions[i];
            drawCircle(g, position.x, position.y, 10);
        }

        g.setColor(Color.BLUE);
        for (int i = NbrBandit + NbrCivil; i < NbrBandit + NbrCivil + NbrPolicier; i++) {
            Point position = circlePositions[i];
            drawCircle(g, position.x, position.y, 10);
        }
    }

    private void drawCircle(Graphics g, int x, int y, int size) {
        g.fillOval(x, y, size, size);
    }

    private void startMovement() {
        if (!movementTimer.isRunning()) {
            movementTimer.start();
        }
    }

    private void stopMovement() {
        if (movementTimer.isRunning()) {
            movementTimer.stop();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TueurFoule();
        });
    }
}
