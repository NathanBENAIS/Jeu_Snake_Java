package bmt;

import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Random;

public class FoodPoisonModel {
    private Image fruitImage;
    // Add variables to store images for different fruits
    private ImageIcon appleIcon = new ImageIcon("C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/vert.png");
    private int foodX;
    private int foodY;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private boolean isVisible = false; // Pour suivre la visibilité du poison
    private Timer spawnTimer; // Timer pour faire apparaître le FoodPoison
    private Timer disappearTimer; // Timer pour faire disparaître le FoodPoison
    private Random random = new Random();

    public FoodPoisonModel() {
        spawn();
        setupTimers(); // Initialisation des timers
        fruitImage = appleIcon.getImage();
    }

    public void spawn() {
        int randomFruit = random.nextInt(4); // Assuming 4 types of fruits

        switch (randomFruit) {
            case 0:
                fruitImage = appleIcon.getImage();
                break;
            // Add cases for other fruits
            default:
                fruitImage = appleIcon.getImage(); // Default to apple
                break;
        }
        foodX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        isVisible = true;
    }

    public void despawn() {
        isVisible = false;
    }

    public void draw(Graphics g) {
        g.drawImage(fruitImage, foodX, foodY, UNIT_SIZE, UNIT_SIZE, null);
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
