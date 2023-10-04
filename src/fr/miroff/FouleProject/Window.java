package fr.miroff.FouleProject;

import fr.miroff.FouleProject.character.ListePerso;

import javax.swing.*;
import java.awt.*;
import java.util.Random;


//Bandits = 1 | Civils = 2 | Policier = 3


public class Window extends JFrame {
    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;
    private int NbrBandit = 1;
    private int NbrCivil = 1;
    private int NbrPolicier = 1;
    private int[][] pointMatrix;
    private ListePerso pointManager;




    public Window() {
        setTitle("Tueurs dans la Foule !");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pointMatrix = new int[WINDOW_WIDTH][WINDOW_HEIGHT];
        pointManager = new ListePerso(WINDOW_WIDTH, WINDOW_HEIGHT);


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
                repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠ Entrée incorrecte.");
            }
        });

        JButton stopButton = new JButton("STOP");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.RED);
        stopButton.addActionListener(e -> {
            //stopMovement();
            JOptionPane.showMessageDialog(this, "⚠ Les points ne sont pas en mouvement.");
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

        setVisible(true);
    }

    private void drawCircles(Graphics g) {
        Random rand = new Random();

        for (int i = 0; i < NbrBandit; i++) {
            g.setColor(Color.RED);
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            drawCircle(g, x, y, 10);
            pointManager.addPoint(new Point(x, y), 1);        }

        for (int i = 0; i < NbrCivil; i++) {
            g.setColor(Color.BLACK);
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            drawCircle(g, x, y, 10);
            pointManager.addPoint(new Point(x, y), 2);            }

        for (int i = 0; i < NbrPolicier; i++) {
            g.setColor(Color.BLUE);
            int x = rand.nextInt(WINDOW_WIDTH);
            int y = rand.nextInt(WINDOW_HEIGHT);
            drawCircle(g, x, y, 10);
            pointManager.addPoint(new Point(x, y), 3);

        }
        pointManager.afficherMatrice();


    }


    private void drawCircle(Graphics g, int x, int y, int size) {
        g.fillOval(x, y, size, size);
    }

}
