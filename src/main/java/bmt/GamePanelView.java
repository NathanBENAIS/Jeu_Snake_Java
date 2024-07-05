package bmt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GamePanelView extends JPanel {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_SPEED = 120;

    private final SnakeModel snakeModel;
    private final FoodModel foodModel;
    private final FoodBoostModel foodBoostModel;
    private final FoodPoisonModel foodPoisonModel;
    private final java.util.List<FoodDeadModel> foodDeadList;

    private JButton restartButton;
    private JButton startButton;
    private JButton quitButton;
    private JLabel titleLabel;
    private JLabel logoLabel;
    private JCheckBox foodFilterCheckBox;
    private JCheckBox foodBoostFilterCheckBox;
    private JCheckBox foodDeadFilterCheckBox;
    private JCheckBox foodPoisonFilterCheckBox;
    private Timer gameTimer;
    private int highScore = 0;

    public GamePanelView(final SnakeModel snakeModel, final FoodModel foodModel, final FoodBoostModel foodBoostModel,
            final FoodPoisonModel foodPoisonModel) {
        this.snakeModel = snakeModel;
        this.foodModel = foodModel;
        this.foodBoostModel = foodBoostModel;
        this.foodPoisonModel = foodPoisonModel;
        this.foodDeadList = new java.util.ArrayList<>();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        setLayout(null);
        repaint();

        // Title label initialization
        
        titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(WIDTH / 2 - 150, HEIGHT / 3 - 100, 300, 50);
        
    
        ImageIcon logoIcon = new ImageIcon("C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/snakee.png");
        // Créez un JLabel pour afficher l'image
        logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(WIDTH / 2 - 307, HEIGHT / 3 - 320, 600, 600); // Ajustez les coordonnées et la taille selon vos besoins
        add(logoLabel);

        // Start button initialization
        startButton = new JButton("Start");
        startButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 - 20, 150, 50);
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
                startGame();
                startButton.setVisible(false);
                titleLabel.setVisible(false);
                logoLabel.setVisible(false);
                showFiltersAndQuit(false); // Hide filters and quit button
            }
        });
        add(startButton);
        // Quit button initialization
        quitButton = new JButton("Quitter");
        // Calculate position for bottom right
        int quitButtonX = WIDTH - 160; // 150 width + 10 padding
        int quitButtonY = HEIGHT - 50; // 40 height + 40 padding
        quitButton.setBounds(quitButtonX, quitButtonY, 150, 40);
        quitButton.setFont(new Font("Sans serif", Font.BOLD, 14));
        quitButton.setBackground(new Color(59, 89, 182));
        quitButton.setForeground(Color.WHITE);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        quitButton.setVisible(false);
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
                System.exit(0);
            }
        });
        add(quitButton);

        // Restart button initialization
        restartButton = new JButton("Restart");
        restartButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 60, 150, 40);
        restartButton.setVisible(false);
        restartButton.setFont(new Font("Sans serif", Font.BOLD, 14));
        restartButton.setBackground(new Color(59, 89, 182));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
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
                restartButton.setVisible(false);
                quitButton.setVisible(false);
                restartGame();
            }
        });
        add(restartButton);

        // Checkbox initialization
        foodFilterCheckBox = new JCheckBox("Food : you gain 1 point !");
        foodFilterCheckBox.setBounds(10, 10, 1500, 30);
        foodFilterCheckBox.setSelected(true);
        foodFilterCheckBox.setForeground(Color.WHITE);
        foodFilterCheckBox.setBackground(Color.DARK_GRAY);
        foodFilterCheckBox.setVisible(false);
        foodFilterCheckBox.setEnabled(false); // Disable the checkbox
        add(foodFilterCheckBox);

        foodBoostFilterCheckBox = new JCheckBox("FoodBoost : your speed is doubled but you gain 2 points");
        foodBoostFilterCheckBox.setBounds(10, 40, 2000, 30);
        foodBoostFilterCheckBox.setSelected(false);
        foodBoostFilterCheckBox.setForeground(Color.WHITE);
        foodBoostFilterCheckBox.setBackground(Color.DARK_GRAY);
        foodBoostFilterCheckBox.setVisible(false);
        add(foodBoostFilterCheckBox);

        foodPoisonFilterCheckBox = new JCheckBox("FoodPoison : you lose 2 points :(");
        foodPoisonFilterCheckBox.setBounds(10, 100, 1500, 30);
        foodPoisonFilterCheckBox.setSelected(false);
        foodPoisonFilterCheckBox.setForeground(Color.WHITE);
        foodPoisonFilterCheckBox.setBackground(Color.DARK_GRAY);
        foodPoisonFilterCheckBox.setVisible(false);
        add(foodPoisonFilterCheckBox);

        foodDeadFilterCheckBox = new JCheckBox("FoodDead: if you touch this, the game ends ;)");
        foodDeadFilterCheckBox.setBounds(10, 70, 1500, 30);
        foodDeadFilterCheckBox.setSelected(false);
        foodDeadFilterCheckBox.setForeground(Color.WHITE);
        foodDeadFilterCheckBox.setBackground(Color.DARK_GRAY);
        foodDeadFilterCheckBox.setVisible(false);
        add(foodDeadFilterCheckBox);

        // Timer for adding FoodDead instances
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
                    snakeModel.checkFoodPoison(foodPoisonModel.getFoodX(), foodPoisonModel.getFoodY(),
                            foodPoisonModel);
                    snakeModel.eatFood(foodModel);
                    snakeModel.eatFoodBoost(foodBoostModel);
                    for (FoodDeadModel foodDead : foodDeadList) {
                        if (snakeModel.getX()[0] == foodDead.getFoodX()
                                && snakeModel.getY()[0] == foodDead.getFoodY()) {
                            snakeModel.setRunning(false);
                        }
                    }
                    repaint();
                } else {
                    endGame();
                }
            }
        });
        gameTimer.start();
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        snakeModel.restartGame();
        foodDeadList.clear();
        startGame();

        // Hide filters and quit button after restart
        showFiltersAndQuit(false);
    }

    private void endGame() {
        if (snakeModel.getFoodEaten() > highScore) {
            highScore = snakeModel.getFoodEaten();
        }
        
        // Show filters and quit button when game ends
        showFiltersAndQuit(true);

        // Show restart button
        restartButton.setVisible(true);
        repaint(); 
    }

    private void showFiltersAndQuit(boolean show) {
        foodFilterCheckBox.setVisible(show);
        foodBoostFilterCheckBox.setVisible(show);
        foodDeadFilterCheckBox.setVisible(show);
        foodPoisonFilterCheckBox.setVisible(show);
        quitButton.setVisible(show);
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
            if (foodFilterCheckBox.isSelected()) {
                foodModel.draw(g);
            }
            if (foodFilterCheckBox.isSelected()) {
                foodModel.draw(g); // Update to use the new draw() method
            }
            if (foodBoostFilterCheckBox.isSelected()) {
                foodBoostModel.draw(g);
            }
            if (foodPoisonFilterCheckBox.isSelected()) {
                foodPoisonModel.draw(g);
            }
            if (foodDeadFilterCheckBox.isSelected()) {
                for (FoodDeadModel foodDead : foodDeadList) {
                    foodDead.draw(g);
                }
            }
            for (int i = 0; i < snakeModel.getLength(); i++) {
                if (i == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GREEN);
                }
                if (snakeModel.isBoosted()) {
                    g.setColor(new Color(255, 255, 90));
                }
                g.fillRect(snakeModel.getX()[i], snakeModel.getY()[i], UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String scoreText = "Score: " + snakeModel.getFoodEaten();
            //FontMetrics fm = g.getFontMetrics();
            int x = 10;
            int y = 20;
            g.drawString(scoreText, x, y);

            // Draw high score in top right
            String highScoreText = "High Score: " + highScore;
            int xHighScore = WIDTH - 10 - g.getFontMetrics().stringWidth(highScoreText);
            g.drawString(highScoreText, xHighScore, 20);
        } else {
            if (snakeModel.getFoodEaten() > highScore) {
                highScore = snakeModel.getFoodEaten();
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Sans serif", Font.BOLD, 40));
            String gameOverText = "Game Over";
            int xGameOver = (WIDTH - g.getFontMetrics().stringWidth(gameOverText)) / 2;
            int yGameOver = HEIGHT / 3 + 15;
            g.drawString(gameOverText, xGameOver, yGameOver);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String finalScoreText = "Score: " + snakeModel.getFoodEaten();
            int xScore = (WIDTH - g.getFontMetrics().stringWidth(finalScoreText)) / 2;
            g.drawString(finalScoreText, xScore, yGameOver + 40);
            String highScoreText = "High Score: " + highScore;
            int xHighScore = (WIDTH - g.getFontMetrics().stringWidth(highScoreText)) / 2;
            g.drawString(highScoreText, xHighScore, yGameOver + 70);
            restartButton.setBounds(WIDTH / 2 - 75, yGameOver + 100, 150, 40);
            restartButton.setVisible(true);
        }
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_Q:
                    if (snakeModel.getDirection() != 'R') {
                        snakeModel.setDirection('L');
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (snakeModel.getDirection() != 'L') {
                        snakeModel.setDirection('R');
                    }
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_Z:
                    if (snakeModel.getDirection() != 'D') {
                        snakeModel.setDirection('U');
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (snakeModel.getDirection() != 'U') {
                        snakeModel.setDirection('D');
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!snakeModel.isRunning() && restartButton.isVisible()) {
                        snakeModel.restartGame();
                        restartButton.setVisible(false);
                        quitButton.setVisible(false);
                        restartGame();
                    }
                    break;
            }
        }
    }
}
