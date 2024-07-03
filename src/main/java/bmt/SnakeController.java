package bmt;

import javax.swing.*;

public class SnakeController {
    private SnakeModel snakeModel;
    private GamePanelView gameView;
    private FoodModel foodModel;
    private FoodBoostModel foodBoostModel;
    private FoodController foodController;

    public SnakeController() {
        snakeModel = new SnakeModel();
        foodModel = new FoodModel();
        foodBoostModel = new FoodBoostModel();
        gameView = new GamePanelView(snakeModel, foodModel, foodBoostModel);
        foodController = new FoodController(foodModel, foodBoostModel, gameView, snakeModel); // Modifier ici

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gameView);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameView.requestFocusInWindow(); // Pour s'assurer que le panneau reçoit les événements de clavier
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SnakeController();
            }
        });
    }
}
