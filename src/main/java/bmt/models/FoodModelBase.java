package bmt.models;

import bmt.models.interfaces.IFoodModelBase;

import java.awt.*;
import javax.swing.ImageIcon;
import java.util.Random;

public abstract class FoodModelBase implements IFoodModelBase {
    protected Image fruitImage;
    protected int foodX;
    protected int foodY;
    protected boolean visible;

    protected static final int UNIT_SIZE = 20;
    protected static final int WIDTH = 500;
    protected static final int HEIGHT = 500;

    protected Random random = new Random();

    public FoodModelBase(String imagePath) {
        loadImage(imagePath);
        spawn();
    }

    protected void loadImage(String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        fruitImage = icon.getImage();
    }

    public void spawn() {
        foodX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        foodY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        visible = true;
    }

    public void draw(Graphics g) {
        if (visible) {
            g.drawImage(fruitImage, foodX, foodY, UNIT_SIZE, UNIT_SIZE, null);
        }
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
}
