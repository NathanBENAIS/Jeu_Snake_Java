package bmt;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodPoisonModel {
    private int foodX;
    private int foodY;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final Color FOOD_COLOR = Color.GREEN;

    private boolean isVisible = false; // Pour suivre la visibilité du poison
    private Timer spawnTimer; // Timer pour faire apparaître le FoodPoison
    private Timer disappearTimer; // Timer pour faire disparaître le FoodPoison

    public FoodPoisonModel() {
        spawn();
        setupTimers(); // Initialisation des timers
    }

    public void spawn() {
        foodX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        isVisible = true;
    }

    public void despawn() {
        isVisible = false;
    }

    public void draw(Graphics g) {
        if (isVisible) {
            g.setColor(FOOD_COLOR);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void setupTimers() {
        // Timer pour faire apparaître un nouveau FoodPoison toutes les 10 secondes
        spawnTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawn();
                // Démarrer le timer pour faire disparaître le FoodPoison après 5 secondes
                disappearTimer.start();
            }
        });

        // Timer pour faire disparaître le FoodPoison après 5 secondes
        disappearTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                despawn(); // Utilise despawn() pour cacher le FoodPoison
                // Arrêter le timer de disparition après son exécution
                disappearTimer.stop();
            }
        });

        // Démarrer le timer de spawn dès la création de l'instance
        spawnTimer.start();
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }
}
