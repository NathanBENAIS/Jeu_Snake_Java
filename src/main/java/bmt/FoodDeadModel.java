package bmt;

import java.awt.*;

public class FoodDeadModel {
    private int foodX;
    private int foodY;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private static final Color FOOD_COLOR = Color.BLACK;

    public FoodDeadModel() {
        spawn();
    }

    public void spawn() {
        foodX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void draw(Graphics g) {
        g.setColor(FOOD_COLOR);
        g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }
}
