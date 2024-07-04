package bmt;

import javax.swing.*;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class FoodController {
    private final FoodModel foodModel;
    private final FoodBoostModel foodBoostModel;
    private final List<FoodDeadModel> foodDeadList;
    private final GamePanelView gameView;
    private final SnakeModel snakeModel;

    private FoodPoisonModel foodPoisonModel;

    private Timer spawnFoodTimer;
    private Timer addFoodDeadTimer;
    private Timer spawnFoodPoisonTimer;
    private Timer despawnFoodPoisonTimer;

    public FoodController(FoodModel foodModel, FoodBoostModel foodBoostModel, GamePanelView gameView,
            SnakeModel snakeModel) {
        this.foodModel = foodModel;
        this.foodBoostModel = foodBoostModel;
        this.foodDeadList = new ArrayList<>();
        this.gameView = gameView;
        this.snakeModel = snakeModel;
        this.foodPoisonModel = new FoodPoisonModel();

        setupFoodSpawning();
        setupFoodDeadAdding();
        setupFoodPoisonSpawning();
    }

    private void setupFoodSpawning() {
        spawnFoodTimer = new Timer(15000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnFoodBoost();
            }
        });
        spawnFoodTimer.setInitialDelay(10000);
        spawnFoodTimer.start();
    }

    private void setupFoodDeadAdding() {
        addFoodDeadTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFoodDead();
            }
        });
        addFoodDeadTimer.start();
    }

    private void setupFoodPoisonSpawning() {
        spawnFoodPoisonTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawnFoodPoison();
            }
        });
        spawnFoodPoisonTimer.start();

        despawnFoodPoisonTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                foodPoisonModel.despawn();
                gameView.repaint();
            }
        });
    }

    public void handleFoodConsumption() {
        if (snakeModel.getX()[0] == foodModel.getFoodX() && snakeModel.getY()[0] == foodModel.getFoodY()) {
            snakeModel.eatFood(foodModel);
            foodModel.spawn();
        }

        if (snakeModel.getX()[0] == foodBoostModel.getFoodX() && snakeModel.getY()[0] == foodBoostModel.getFoodY()) {
            snakeModel.eatFoodBoost(foodBoostModel);
            foodBoostModel.spawn();
        }

        if (foodPoisonModel.isVisible() && snakeModel.getX()[0] == foodPoisonModel.getFoodX()
                && snakeModel.getY()[0] == foodPoisonModel.getFoodY()) {
            snakeModel.eatFoodPoison(foodPoisonModel);
            foodPoisonModel.despawn();
        }

        for (FoodDeadModel foodDead : foodDeadList) {
            if (snakeModel.getX()[0] == foodDead.getFoodX() && snakeModel.getY()[0] == foodDead.getFoodY()) {
                snakeModel.eatFoodDead(foodDead);
            }
        }

        gameView.repaint();
    }

    public void spawnFood() {
        foodModel.spawn();
        while (checkCollisionWithSnake(foodModel.getFoodX(), foodModel.getFoodY())) {
            foodModel.spawn();
        }
        gameView.repaint();
    }

    public void spawnFoodBoost() {
        foodBoostModel.spawn();
        while (checkCollisionWithSnake(foodBoostModel.getFoodX(), foodBoostModel.getFoodY())) {
            foodBoostModel.spawn();
        }
        gameView.repaint();
    }

    private void addFoodDead() {
        FoodDeadModel newFoodDead = new FoodDeadModel();
        while (checkCollisionWithSnake(newFoodDead.getFoodX(), newFoodDead.getFoodY())) {
            newFoodDead.spawn();
        }
        foodDeadList.add(newFoodDead);
        gameView.repaint();
    }

    private void spawnFoodPoison() {
        foodPoisonModel.spawn();
        while (checkCollisionWithSnake(foodPoisonModel.getFoodX(), foodPoisonModel.getFoodY())) {
            foodPoisonModel.spawn();
        }
        despawnFoodPoisonTimer.setRepeats(false);
        despawnFoodPoisonTimer.start();
        gameView.repaint();
    }

    private boolean checkCollisionWithSnake(int foodX, int foodY) {
        for (int i = 0; i < snakeModel.getLength(); i++) {
            if (snakeModel.getX()[i] == foodX && snakeModel.getY()[i] == foodY) {
                return true;
            }
        }
        return false;
    }

    public void stopFoodSpawning() {
        if (spawnFoodTimer != null) {
            spawnFoodTimer.stop();
        }
        if (addFoodDeadTimer != null) {
            addFoodDeadTimer.stop();
        }
        if (spawnFoodPoisonTimer != null) {
            spawnFoodPoisonTimer.stop();
        }
        if (despawnFoodPoisonTimer != null) {
            despawnFoodPoisonTimer.stop();
        }
    }
}
