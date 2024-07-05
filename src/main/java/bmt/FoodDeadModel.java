package bmt;

import java.awt.*;
import javax.swing.ImageIcon;
import java.util.Random;

public class FoodDeadModel {
    private Image fruitImage;
    // Add variables to store images for different fruits
    private ImageIcon appleIcon = new ImageIcon("C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/dead.png");
    private int foodX;
    private int foodY;
    private boolean visible;

    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private Random random = new Random();

    public FoodDeadModel() {
        spawn();
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
    }

    public void draw(Graphics g) {
        g.drawImage(fruitImage, foodX, foodY, UNIT_SIZE, UNIT_SIZE, null);
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
