package bmt;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    static final int WIDTH = 500;
    static final int HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    final int x[] = new int[NUMBER_OF_UNITS]; // coordonnées x du serpent
    final int y[] = new int[NUMBER_OF_UNITS]; // coordonnées y du serpent

    int length = 5; // longueur initiale du serpent
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'D'; // direction initiale: Droite
    boolean running = false;
    Random random;
    Timer timer;
    JButton restartButton;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play(); // démarrer le jeu
    }

    public void play() {
        addFood(); // ajouter de la nourriture
        running = true;
        timer = new Timer(150, this); // Augmenter cette valeur pour ralentir le serpent
        timer.start();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics); // dessiner le jeu
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1]; // déplacer le corps
            y[i] = y[i - 1];
        }

        switch (direction) { // déplacer la tête
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            length++; // augmenter la longueur du serpent
            foodEaten++; // incrémenter le score
            addFood(); // ajouter une nouvelle nourriture
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            // dessiner la nourriture
            graphics.setColor(new Color(210, 115, 90));
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // dessiner la tête du serpent
            graphics.setColor(Color.white);
            graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

            // dessiner le corps du serpent
            for (int i = 1; i < length; i++) {
                graphics.setColor(new Color(40, 200, 150));
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // afficher le score
            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
                    graphics.getFont().getSize());
        } else {
            gameOver(graphics); // afficher Game Over
        }
    }

    public void addFood() {
        // positionner la nourriture aléatoirement
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void checkHit() {
        // vérifier la collision avec le corps
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        // vérifier la collision avec les murs
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop(); // arrêter le jeu
        }
    }

    public void gameOver(Graphics graphics) {
        // afficher Game Over
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        // afficher le score final
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
                graphics.getFont().getSize());

        // ajouter le bouton "Restart" s'il n'existe pas déjà
        if (restartButton == null) {
            restartButton = new JButton("Restart");
            restartButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 50, 100, 30);
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    resetGame(); // Réinitialiser le jeu lorsqu'on clique sur "Restart"
                }
            });
            this.setLayout(null); // utiliser un layout null pour positionner le bouton
            this.add(restartButton);
        }

        restartButton.setVisible(true); // afficher le bouton "Restart"
    }

    public void resetGame() {
        // réinitialiser les variables de jeu
        length = 5;
        foodEaten = 0;
        direction = 'D';
        running = true;
        for (int i = 0; i < NUMBER_OF_UNITS; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        addFood(); // ajouter de la nourriture
        timer.start(); // redémarrer le timer

        // cacher le bouton "Restart"
        if (restartButton != null) {
            restartButton.setVisible(false);
        }

        repaint(); // redessiner l'écran
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move(); // déplacer le serpent
            checkFood(); // vérifier si la nourriture est mangée
            checkHit(); // vérifier les collisions
        }
        repaint(); // redessiner l'écran
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') { // empêcher de tourner à 180°
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
