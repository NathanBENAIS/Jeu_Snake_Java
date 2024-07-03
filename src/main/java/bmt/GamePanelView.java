package bmt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanelView extends JPanel {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_SPEED = 80; // Vitesse du jeu en millisecondes

    private final SnakeModel snakeModel;
    private final FoodModel foodModel;
    private final FoodBoostModel foodBoostModel;
    private JButton restartButton;
    private Timer gameTimer; // Timer comme champ de classe

    public GamePanelView(final SnakeModel snakeModel, final FoodModel foodModel, final FoodBoostModel foodBoostModel) {
        this.snakeModel = snakeModel;
        this.foodModel = foodModel;
        this.foodBoostModel = foodBoostModel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        // Création du bouton Restart avec style personnalisé
        restartButton = new JButton("Restart");
        restartButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 60, 150, 40); // Dimensions agrandies
        restartButton.setVisible(false); // Initialement caché tant que le jeu n'est pas terminé
        restartButton.setFont(new Font("Sans serif", Font.BOLD, 14));
        restartButton.setBackground(new Color(59, 89, 182));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Effet hover
        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(89, 119, 222));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartButton.setBackground(new Color(59, 89, 182));
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeModel.restartGame();
                restartButton.setVisible(false); // Cacher le bouton après avoir redémarré
                restartGame(); // Redémarrer le timer et le jeu
            }
        });

        add(restartButton);

        startGame(); // Démarrer le jeu initialement
    }

    private void startGame() {
        gameTimer = new Timer(GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snakeModel.isRunning()) {
                    snakeModel.move();
                    snakeModel.checkCollision();
                    snakeModel.checkFood(foodModel.getFoodX(), foodModel.getFoodY());
                    snakeModel.checkFoodBoost(foodBoostModel.getFoodX(), foodBoostModel.getFoodY());
                    snakeModel.eatFood(foodModel);
                    snakeModel.eatFoodBoost(foodBoostModel);
                    repaint();
                }
            }
        });
        gameTimer.start();
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop(); // Arrêter le timer existant s'il est en cours
            gameTimer = null; // Détruire l'instance du timer
        }
        startGame(); // Redémarrer le jeu avec un nouveau timer
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (snakeModel.isRunning()) {
            // Dessiner la nourriture normale
            foodModel.draw(g);

            // Dessiner la nourriture boost
            foodBoostModel.draw(g);

            // Dessiner le serpent
            for (int i = 0; i < snakeModel.getLength(); i++) {
                if (i == 0) {
                    g.setColor(Color.WHITE); // Couleur de la tête
                } else {
                    g.setColor(Color.GREEN); // Couleur du corps (par défaut)
                }
                if (snakeModel.isBoosted()) {
                    g.setColor(new Color(148, 0, 211)); // Couleur violette lorsque boosté
                }
                g.fillRect(snakeModel.getX()[i], snakeModel.getY()[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Afficher le score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String scoreText = "Score: " + snakeModel.getFoodEaten();
            FontMetrics fm = g.getFontMetrics();
            int x = (WIDTH - fm.stringWidth(scoreText)) / 2; // Centrer horizontalement
            int y = 20; // Position fixe verticalement
            g.drawString(scoreText, x, y);
        } else {
            // Afficher l'écran de fin de jeu
            g.setColor(Color.RED);
            g.setFont(new Font("Sans serif", Font.BOLD, 40));
            String gameOverText = "Game Over";
            int xGameOver = (WIDTH - g.getFontMetrics().stringWidth(gameOverText)) / 2;
            int yGameOver = HEIGHT / 3; // Positionner au tiers de l'écran verticalement
            g.drawString(gameOverText, xGameOver, yGameOver);

            // Afficher le score final
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String finalScoreText = "Score: " + snakeModel.getFoodEaten();
            int xScore = (WIDTH - g.getFontMetrics().stringWidth(finalScoreText)) / 2;
            g.drawString(finalScoreText, xScore, yGameOver + 40); // Positionner sous le texte Game Over

            // Positionner le bouton Restart sous le score final
            restartButton.setBounds(WIDTH / 2 - 75, yGameOver + 80, 150, 40);
            restartButton.setVisible(true); // Afficher le bouton Restart lors du game over
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (snakeModel.getDirection() != 'R') {
                        snakeModel.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (snakeModel.getDirection() != 'L') {
                        snakeModel.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (snakeModel.getDirection() != 'D') {
                        snakeModel.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (snakeModel.getDirection() != 'U') {
                        snakeModel.setDirection('D');
                    }
                    break;
            }
        }
    }
}
