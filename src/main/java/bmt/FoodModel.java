package bmt;

import java.awt.*;
import java.util.Random;

public class FoodModel {
    private int foodX;
    private int foodY;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private Random random;

    public FoodModel() {
        random = new Random();
        spawn();
    }

    public void spawn() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }
}
