package bmt;

import bmt.controllers.SnakeController;
import bmt.models.*;
import bmt.views.GamePanelView;

import javax.swing.*;

public class Main {

    private static void init() {
        SnakeModel snakeModel = new SnakeModel(new SnakeController()); // Pass reference to this SnakeController
        GamePanelView gameView = new GamePanelView(snakeModel);

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gameView);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameView.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }
}
