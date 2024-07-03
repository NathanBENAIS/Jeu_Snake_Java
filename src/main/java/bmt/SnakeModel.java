package bmt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeModel {
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int BOOST_DURATION = 5000; // Durée du boost en millisecondes (5 secondes)

    private int[] x = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];
    private int[] y = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];

    private int length = 5;
    private int foodEaten;
    private char direction = 'R'; // Direction initiale

    private boolean running = true;
    private boolean boosted = false; // Indique si le serpent est boosté
    private Timer boostTimer;

    public SnakeModel() {
        // Initialisation du serpent
        x[0] = 0;
        y[0] = 0;
    }

    public void move() {
        // Déplacer le serpent en fonction de la direction
        int speedMultiplier = boosted ? (2) : 1; // Boost de vitesse si boosted est true
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
        // Vérifier la collision avec les bords de l'écran
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        // Vérifier la collision avec le corps du serpent
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        if (!running) {
            // Arrêter le jeu ou effectuer d'autres actions
        }
    }

    public void checkFood(int foodX, int foodY) {
        // Vérifier si la tête du serpent a mangé de la nourriture normale
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
        }
    }

    public void checkFoodBoost(int foodX, int foodY) {
        // Vérifier si la tête du serpent a mangé de la nourriture boost
        if (x[0] == foodX && y[0] == foodY) {
            startBoostTimer(); // Démarrer le timer de boost
            boosted = true;
            length++;
            foodEaten++;
        }
    }

    private void startBoostTimer() {
        boosted = true; // Activer le boost immédiatement
        boostTimer = new Timer(BOOST_DURATION, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boosted = false; // Désactiver le boost lorsque le timer expire
                boostTimer.stop(); // Arrêter le timer
            }
        });
        boostTimer.setRepeats(false); // Ne pas répéter le timer
        boostTimer.start();
    }

    public void eatFood(FoodModel foodModel) {
        if (x[0] == foodModel.getFoodX() && y[0] == foodModel.getFoodY()) {
            length++;
            foodEaten++;
            foodModel.spawn(); // Réinitialiser la position de la nourriture

            // Ajouter un nouveau segment de serpent à la position actuelle de la tête
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
        }
    }

    public void eatFoodBoost(FoodBoostModel foodBoostModel) {
        if (x[0] == foodBoostModel.getFoodX() && y[0] == foodBoostModel.getFoodY()) {
            startBoostTimer(); // Démarrer le timer de boost
            boosted = true;
            length++;
            foodEaten++;
            foodBoostModel.spawn(); // Réinitialiser la position de la nourriture boost

            // Ajouter un nouveau segment de serpent à la position actuelle de la tête
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
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

        // Réinitialisation de la position du serpent
        x[0] = 0;
        y[0] = 0;
        for (int i = 1; i < length; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        // Arrêter le timer de boost s'il est en cours
        if (boostTimer != null) {
            boostTimer.stop();
            boostTimer = null;
        }
    }
}
