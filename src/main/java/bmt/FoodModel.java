package bmt;

import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class FoodModel {
    private Image fruitImage;
    // Add variables to store images for different fruits
    private ImageIcon appleIcon = new ImageIcon("C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/apple.png");
    private int foodX;
    private int foodY;
    private boolean visible;

    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private Random random;

    public FoodModel() {
        random = new Random();
        spawn();

        fruitImage = appleIcon.getImage();
    }

    public void spawn() {
        // Add logic to randomly choose which fruit to spawn
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

        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
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
