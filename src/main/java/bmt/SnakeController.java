package bmt;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SnakeController {
    private SnakeModel snakeModel;
    private GamePanelView gameView;
    private FoodModel foodModel;
    private FoodBoostModel foodBoostModel;
    private FoodPoisonModel foodPoisonModel;
    private Clip clip;

    public SnakeController() {
        snakeModel = new SnakeModel(this); // Pass reference to this SnakeController
        foodModel = new FoodModel();
        foodBoostModel = new FoodBoostModel();
        foodPoisonModel = new FoodPoisonModel();
        gameView = new GamePanelView(snakeModel, foodModel, foodBoostModel, foodPoisonModel);

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(gameView);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameView.requestFocusInWindow();

        // Initialize Clip for playing sound
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Method to play a sound effect from a file
    private void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip.stop();  // Stop any currently playing sound
            clip.close(); // Close the clip if it's open
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playGameOverSound() {
        String soundFilePath = "C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/dead.wav"; // Adjust path to your sound file
        playSound(soundFilePath);
    }

    public void playBoostSound() {
        String soundFilePath = "C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/boost.wav"; // Adjust path to your sound file
        playSound(soundFilePath);
    }
    
    // Method to play sound when the snake eats an apple
    public void playEatAppleSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = "C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/food.wav";
        playSound(soundFilePath);
    }

    public void playEatPoisonSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = "C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/fart.wav";
        playSound(soundFilePath);
    }

    public void playEatDeadSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = "C:/Users/RiadK/OneDrive/Bureau/Snake/Jeu_Snake_Java/src/main/resources/bmt/dead2.wav";
        playSound(soundFilePath);
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
