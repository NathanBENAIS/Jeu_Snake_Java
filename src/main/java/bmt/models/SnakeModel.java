package bmt.models;

import bmt.controllers.SnakeController;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeModel {
    private SnakeController snakeController;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int BOOST_DURATION = 7000;

    private int[] x = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];
    private int[] y = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];

    private int length = 5;
    private int foodEaten;
    private char direction = 'R';

    private boolean running = true;
    private boolean boosted = false;
    private Timer boostTimer;

    public SnakeModel(SnakeController snakeController) {
        this.snakeController = snakeController;
        x[0] = 0;
        y[0] = 0;
    }

    public void move() {
        int speedMultiplier = boosted ? 2 : 1;
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'L':
                x[0] -= UNIT_SIZE * speedMultiplier;
                break;
            case 'R':
                x[0] += UNIT_SIZE * speedMultiplier;
                break;
            case 'U':
                y[0] -= UNIT_SIZE * speedMultiplier;
                break;
            case 'D':
                y[0] += UNIT_SIZE * speedMultiplier;
                break;
        }
    }

    public void checkCollision() {
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
            snakeController.playGameOverSound();
        }
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                snakeController.playEatDeadSound();
                break;
            }
        }
        if (!running) {
            // Actions à effectuer lorsque le jeu s'arrête
        }
    }

    public void checkFood(int foodX, int foodY) {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            snakeController.playEatAppleSound();
        }
    }

    public void checkFoodBoost(int foodX, int foodY) {
        if (x[0] == foodX && y[0] == foodY) {
            startBoostTimer();
            boosted = true;
            length++;
            foodEaten++;
            snakeController.playBoostSound();
        }
    }

    public void checkFoodDead(int foodX, int foodY, FoodDeadModel foodDeadModel) {
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodDeadModel.spawn();
            snakeController.playEatDeadSound();
            running = false; // Game over when snake eats FoodDead
            
        }
    }

    public void checkFoodPoison(int foodX, int foodY, FoodPoisonModel foodPoisonModel) {
        if (x[0] == foodX && y[0] == foodY) {
            eatFoodPoison(foodPoisonModel);
            foodPoisonModel.spawn();
            snakeController.playEatPoisonSound();
        }
    }

    public void eatFoodPoison(FoodPoisonModel foodPoisonModel) {
        // Décrémentez le score de 2

        if (foodEaten > 1) {
            foodEaten -= 2;
        } else {
            foodEaten = 0;
        }

        // Réduire la longueur du serpent de 2
        if (length > 2) {
            length -= 2;
        } else {
            length = 1; // Assurez-vous que la longueur ne devient pas nulle ou négative
        }
    }

    private void startBoostTimer() {
        boosted = true;
        boostTimer = new Timer(BOOST_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boosted = false;
                boostTimer.stop();
            }
        });
        boostTimer.setRepeats(false);
        boostTimer.start();
    }

    public void eatFood(FoodModelBase foodModel) {
        if (x[0] == foodModel.getFoodX() && y[0] == foodModel.getFoodY()) {
            length += 0.5;
            foodEaten += 0.5;
            foodModel.spawn();
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
        }
    }

    public void eatFoodBoost(FoodBoostModel foodBoostModel) {
        if (x[0] == foodBoostModel.getFoodX() && y[0] == foodBoostModel.getFoodY()) {
            startBoostTimer();
            boosted = true;
            length++;
            foodEaten++;
            foodBoostModel.spawn();
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
        }
    }

    public void eatFoodDead(FoodDeadModel foodDeadModel) {
        if (x[0] == foodDeadModel.getFoodX() && y[0] == foodDeadModel.getFoodY()) {
            running = false;
        }
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isBoosted() {
        return boosted;
    }

    public void restartGame() {
        length = 5;
        foodEaten = 0;
        direction = 'R';
        running = true;
        boosted = false;
        x[0] = 0;
        y[0] = 0;
        for (int i = 1; i < length; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        if (boostTimer != null) {
            boostTimer.stop();
            boostTimer = null;
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
