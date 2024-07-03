package bmt;

import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodDeadModel {
    private int foodX;
    private int foodY;
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private static final Color FOOD_COLOR = Color.BLACK;
    private Timer foodTimer;

    public FoodDeadModel() {
        spawn();
        startTimer();
    }

    public void spawn() {
        foodX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void startTimer() {
        foodTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foodX = -UNIT_SIZE; // Move off screen to hide
                foodY = -UNIT_SIZE;
            }
        });
        foodTimer.setRepeats(false);
        foodTimer.start();
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

    public void stopTimer() {
        if (foodTimer != null) {
            foodTimer.stop();
        }
    }
}
