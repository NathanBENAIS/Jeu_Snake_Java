package bmt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanelView extends JPanel {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_SPEED = 120; // Vitesse du jeu en millisecondes

    private final SnakeModel snakeModel;
    private final FoodModel foodModel;
    private final FoodBoostModel foodBoostModel;
    private final FoodPoisonModel foodPoisonModel;

    private final java.util.List<FoodDeadModel> foodDeadList; // Liste pour plusieurs instances de nourriture morte
    private JButton restartButton;
    private JButton startButton; // Bouton Start
    private JButton quitButton; // Bouton Quitter
    private JLabel titleLabel; // Titre du jeu
    private Timer gameTimer; // Timer pour le jeu
    private int highScore = 0; // Variable pour stocker le high score

    public GamePanelView(final SnakeModel snakeModel, final FoodModel foodModel, final FoodBoostModel foodBoostModel,
            final FoodPoisonModel foodPoisonModel) {
        this.snakeModel = snakeModel;
        this.foodModel = foodModel;
        this.foodBoostModel = foodBoostModel;
        this.foodDeadList = new java.util.ArrayList<>(); // Initialisation de la liste
        this.foodPoisonModel = foodPoisonModel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        setLayout(null); // Utiliser null layout pour positionner les composants précisément

        // Création du titre du jeu
        titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(WIDTH / 2 - 150, HEIGHT / 3 - 100, 300, 50); // Dimensions et position

        add(titleLabel);

        // Création du bouton Start avec style personnalisé
        startButton = new JButton("Start");
        startButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 - 20, 150, 50); // Dimensions et position
        startButton.setFont(new Font("Sans serif", Font.BOLD, 14));
        startButton.setBackground(new Color(59, 89, 182));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(89, 119, 222));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(59, 89, 182));
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // Démarrer le jeu
                startButton.setVisible(false); // Cacher le bouton Start après avoir démarré
                titleLabel.setVisible(false); // Cacher le titre après avoir démarré
            }
        });

        add(startButton);

        /****/
        // Création du bouton Quitter
        quitButton = new JButton("Quitter");
        quitButton.setBounds(WIDTH / 2 - 75, HEIGHT - 80, 150, 40); // Position en bas de la fenêtre
        quitButton.setFont(new Font("Sans serif", Font.BOLD, 14));
        quitButton.setBackground(new Color(59, 89, 182));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        quitButton.setVisible(false); // Rendre le bouton invisible initialement

        quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quitButton.setBackground(new Color(89, 119, 222));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                quitButton.setBackground(new Color(59, 89, 182));
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Fermer l'application
            }
        });

        add(quitButton);
        /****/

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

        // Configurer un timer pour ajouter des objets FoodDead toutes les 5 secondes
        Timer addFoodDeadTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFoodDead();
            }
        });
        addFoodDeadTimer.start();
    }

    private void startGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snakeModel.isRunning()) {
                    snakeModel.move();
                    snakeModel.checkCollision();
                    snakeModel.checkFood(foodModel.getFoodX(), foodModel.getFoodY());
                    snakeModel.checkFoodBoost(foodBoostModel.getFoodX(), foodBoostModel.getFoodY());
                    snakeModel.checkFoodPoison(foodPoisonModel.getFoodX(), foodPoisonModel.getFoodY(), foodPoisonModel);
                    snakeModel.eatFood(foodModel);
                    snakeModel.eatFoodBoost(foodBoostModel);

                    // Vérifier la collision avec toutes les nourritures mortes
                    for (FoodDeadModel foodDead : foodDeadList) {
                        if (snakeModel.getX()[0] == foodDead.getFoodX()
                                && snakeModel.getY()[0] == foodDead.getFoodY()) {
                            snakeModel.setRunning(false); // Arrêter le jeu en cas de collision
                        }
                    }

                    repaint();
                } else {
                    endGame(); // Afficher les boutons lorsque le jeu est terminé
                }
            }
        });
        gameTimer.start();
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop(); // Arrêter le timer existant s'il est en cours
        }
        snakeModel.restartGame(); // Réinitialiser le modèle de serpent
        foodDeadList.clear(); // Vider la liste de nourriture morte pour un nouveau départ
        startGame();
        quitButton.setVisible(false);
    }

    private void endGame() {
        restartButton.setVisible(true);
        quitButton.setVisible(true);
    }

    private void addFoodDead() {
        FoodDeadModel newFoodDead = new FoodDeadModel();
        foodDeadList.add(newFoodDead);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (snakeModel.isRunning()) {
            // Dessiner la nourriture normale
            foodModel.draw(g);

            // Dessiner la nourriture boost
            foodBoostModel.draw(g);
            foodPoisonModel.draw(g); // Dessiner la nourriture empoisonnée
            // Dessiner toutes les nourritures mortes
            for (FoodDeadModel foodDead : foodDeadList) {
                foodDead.draw(g);
            }

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
            // Mettre à jour le high score si le score actuel est supérieur
            if (snakeModel.getFoodEaten() > highScore) {
                highScore = snakeModel.getFoodEaten();
            }

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

            // Afficher le high score
            String highScoreText = "High Score: " + highScore;
            int xHighScore = (WIDTH - g.getFontMetrics().stringWidth(highScoreText)) / 2;
            g.drawString(highScoreText, xHighScore, yGameOver + 70); // Positionner sous le score final

            // Positionner le bouton Restart sous le high score
            restartButton.setBounds(WIDTH / 2 - 75, yGameOver + 100, 150, 40);
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
                case KeyEvent.VK_SPACE:
                    if (!snakeModel.isRunning() && restartButton.isVisible()) {
                        snakeModel.restartGame();
                        restartButton.setVisible(false);
                        quitButton.setVisible(false); // Cacher le bouton Quitter après avoir redémarré
                        restartGame();
                    }
                    break;
            }
        }
    }
}
