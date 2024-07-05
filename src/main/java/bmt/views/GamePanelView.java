package bmt.views;

import bmt.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class GamePanelView extends JPanel {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_SPEED = 120;

    private final SnakeModel snakeModel;
    private final FoodModel foodModel;
    private FoodBoostModel foodBoostModel;
    private FoodPoisonModel foodPoisonModel;
    private List<FoodDeadModel> foodDeadList;

    private JButton restartButton;
    private JButton quitButton;
    private JLabel titleLabel;
    private JLabel logoLabel;
    private JLabel keysLabel;
    private JLabel spaceLabel;
    private JCheckBox foodFilterCheckBox;
    private JCheckBox foodBoostFilterCheckBox;
    private JCheckBox foodDeadFilterCheckBox;
    private JCheckBox foodPoisonFilterCheckBox;
    private Timer gameTimer;
    private int highScore = 0;
    private Image gameOverImage;

    public GamePanelView(final SnakeModel snakeModel) {
        this.snakeModel = snakeModel;
        this.foodModel = new FoodModel();
        this.foodBoostModel = null;
        this.foodPoisonModel = null;
        this.foodDeadList = new ArrayList<>();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(127,255,0));
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        setLayout(null);
        repaint();

        // Load the game over image
        try {
            gameOverImage = ImageIO.read(new File("/Users/drbook/IdeaProjects/Jeu_Snake_Java/src/main/resources/bmt/gameover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Title label initialization
        titleLabel = new JLabel("Snake Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(WIDTH / 2 - 150, HEIGHT / 3 - 100, 300, 50);
        //add(titleLabel);

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/bmt/logo.png"));
        ImageIcon KeysIcon = new ImageIcon(getClass().getResource("/bmt/keys.png"));
        // Créez un JLabel pour afficher l'image
        logoLabel = new JLabel(logoIcon);
        logoLabel.setBounds(WIDTH / 2 - 300, HEIGHT / 3 - 280, 600, 600); // Ajustez les coordonnées et la taille selon vos besoins
        add(logoLabel);

        keysLabel = new JLabel(KeysIcon);
        keysLabel.setBounds(WIDTH / 2 - 130, HEIGHT / 3 + 20, 600, 600); // Ajustez les coordonnées et la taille selon vos besoins
        add(keysLabel);

        // Create a JLabel for the start image
        final ImageIcon startIcon = new ImageIcon(getClass().getResource("/bmt/start.png"));
        final JLabel startImageLabel = new JLabel(startIcon);
        startImageLabel.setBounds(WIDTH / 2 - 100, HEIGHT / 2 + 90, startIcon.getIconWidth(), startIcon.getIconHeight());
        startImageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startGame();
                startImageLabel.setVisible(false);
                titleLabel.setVisible(false);
                logoLabel.setVisible(false);
                keysLabel.setVisible(false);
                showFiltersAndQuit(false); // Hide filters and quit button
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Optionally, change image appearance on mouse hover
                startImageLabel.setIcon(new ImageIcon(getClass().getResource("/bmt/start.png")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Optionally, change image appearance on mouse exit
                startImageLabel.setIcon(startIcon);
            }
        });
        add(startImageLabel);

        // Quit button initialization
        final ImageIcon quitIcon = new ImageIcon(getClass().getResource("/bmt/exit.png"));
        quitButton = new JButton(quitIcon);
        int quitButtonX = WIDTH - 160; // 150 width + 10 padding
        int quitButtonY = HEIGHT - 80; // 40 height + 40 padding
        quitButton.setBounds(quitButtonX, quitButtonY, quitIcon.getIconWidth(), quitIcon.getIconHeight());
        quitButton.setVisible(false);
        quitButton.setFocusPainted(false);
        quitButton.setBorder(BorderFactory.createEmptyBorder());
        quitButton.setContentAreaFilled(false); // Make button transparent
        quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Optionally, change image appearance on mouse hover
                quitButton.setIcon(new ImageIcon(getClass().getResource("/bmt/exit.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Optionally, change image appearance on mouse exit
                quitButton.setIcon(quitIcon);
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
        final ImageIcon restartIcon = new ImageIcon(getClass().getResource("/bmt/restart.png"));
        restartButton = new JButton(restartIcon);
        restartButton.setBounds(WIDTH / 2, HEIGHT / 2, restartIcon.getIconWidth(), restartIcon.getIconHeight());
        restartButton.setVisible(false);
        restartButton.setFocusPainted(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder());
        restartButton.setContentAreaFilled(false); // Make button transparent
        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Optionally, change image appearance on mouse hover
                restartButton.setIcon(new ImageIcon(getClass().getResource("/bmt/restart.png")));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Optionally, change image appearance on mouse exit
                restartButton.setIcon(restartIcon);
            }
        });
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                snakeModel.restartGame();
                restartButton.setVisible(false);
                quitButton.setVisible(false);
                spaceLabel.setVisible(false);
                restartGame();
            }
        });
        add(restartButton);


        // Space label initialization
        spaceLabel = new JLabel("(Or use Space)", SwingConstants.CENTER);
        spaceLabel.setFont(new Font("Sans serif", Font.PLAIN, 14));
        spaceLabel.setForeground(Color.BLACK);
        spaceLabel.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 105, 150, 30); // Position it just below the restart button
        spaceLabel.setVisible(false);
        add(spaceLabel);

        // Checkbox initialization
        foodFilterCheckBox = new JCheckBox("Food : you gain 1 point !");
        foodFilterCheckBox.setBounds(10, 10, 1500, 30);
        foodFilterCheckBox.setSelected(true);
        foodFilterCheckBox.setForeground(Color.BLACK);
        foodFilterCheckBox.setBackground(new Color(127,255,0));
        foodFilterCheckBox.setVisible(false);
        foodFilterCheckBox.setEnabled(false); // Disable the checkbox
        add(foodFilterCheckBox);

        foodBoostFilterCheckBox = new JCheckBox("FoodBoost : your speed is doubled but you gain 2 points");
        foodBoostFilterCheckBox.setBounds(10, 40, 2000, 30);
        foodBoostFilterCheckBox.setSelected(false);
        foodBoostFilterCheckBox.setForeground(Color.BLACK);
        foodBoostFilterCheckBox.setBackground(new Color(127,255,0));
        foodBoostFilterCheckBox.setVisible(false);
        add(foodBoostFilterCheckBox);

        foodPoisonFilterCheckBox = new JCheckBox("FoodPoison : you lose 2 points :(");
        foodPoisonFilterCheckBox.setBounds(10, 100, 1500, 30);
        foodPoisonFilterCheckBox.setSelected(false);
        foodPoisonFilterCheckBox.setForeground(Color.BLACK);
        foodPoisonFilterCheckBox.setBackground(new Color(127,255,0));
        foodPoisonFilterCheckBox.setVisible(false);
        add(foodPoisonFilterCheckBox);

        foodDeadFilterCheckBox = new JCheckBox("FoodDead: if you touch this, the game ends ;)");
        foodDeadFilterCheckBox.setBounds(10, 70, 1500, 30);
        foodDeadFilterCheckBox.setSelected(false);
        foodDeadFilterCheckBox.setForeground(Color.BLACK);
        foodDeadFilterCheckBox.setBackground(new Color(127,255,0));
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

        if (foodBoostFilterCheckBox.isSelected()) {
            foodBoostModel = new FoodBoostModel();
        }

        if (foodPoisonFilterCheckBox.isSelected()) {
            foodPoisonModel = new FoodPoisonModel();
        }

        if (!foodDeadFilterCheckBox.isSelected()) {
            foodDeadList = new ArrayList<>();
        }

        gameTimer = new Timer(GAME_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snakeModel.isRunning()) {
                    snakeModel.move();
                    snakeModel.checkCollision();
                    if (foodBoostModel != null) {
                        snakeModel.checkFoodBoost(foodBoostModel.getFoodX(), foodBoostModel.getFoodY());
                        snakeModel.eatFoodBoost(foodBoostModel);
                    }
                    if (foodPoisonModel != null) {
                        snakeModel.checkFoodPoison(foodPoisonModel.getFoodX(), foodPoisonModel.getFoodY(),
                                foodPoisonModel);
                    }
                    snakeModel.checkFood(foodModel.getFoodX(), foodModel.getFoodY());
                    snakeModel.eatFood(foodModel);
                    if (foodDeadFilterCheckBox.isSelected()) {
                        for (FoodDeadModel foodDead : foodDeadList) {
                            if (snakeModel.getX()[0] == foodDead.getFoodX()
                                    && snakeModel.getY()[0] == foodDead.getFoodY()) {
                                snakeModel.setRunning(false);
                            }
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

        // Show restart button and space label
        restartButton.setVisible(true);
        spaceLabel.setVisible(true);
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
                    g.setColor(new Color(0, 150, 0));
                }
                if (snakeModel.isBoosted()) {
                    g.setColor(new Color(255, 255, 90));
                }
                g.fillRect(snakeModel.getX()[i], snakeModel.getY()[i], UNIT_SIZE, UNIT_SIZE);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String scoreText = "Score: " + snakeModel.getFoodEaten();
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
            // Draw the game over image instead of text
            if (gameOverImage != null) {
                int imgX = (WIDTH - gameOverImage.getWidth(this)) / 2;
                int imgY = HEIGHT / 3;
                g.drawImage(gameOverImage, imgX, imgY, this);
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Sans serif", Font.BOLD, 20));
            String finalScoreText = "Score: " + snakeModel.getFoodEaten();
            int xScore = (WIDTH - g.getFontMetrics().stringWidth(finalScoreText)) / 2;
            int yGameOver = HEIGHT / 3 + 20 + gameOverImage.getHeight(this) + 20;
            g.drawString(finalScoreText, xScore, yGameOver);
            String highScoreText = "High Score: " + highScore;
            int xHighScore = (WIDTH - g.getFontMetrics().stringWidth(highScoreText)) / 2;
            g.drawString(highScoreText, xHighScore, yGameOver + 30);
            restartButton.setBounds(WIDTH / 2 - 100, yGameOver + 70, 200, 60);
            restartButton.setVisible(true);
            spaceLabel.setBounds(WIDTH / 2 - 75, yGameOver + 120, 150, 30); // Update spaceLabel bounds
            spaceLabel.setVisible(true);
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
                        spaceLabel.setVisible(false);
                        restartGame();
                    }
                    break;
            }
        }
    }
}