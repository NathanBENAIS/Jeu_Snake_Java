package bmt.controllers;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SnakeController {
    private Clip clip;

    public SnakeController() {
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
        String soundFilePath = getClass().getResource("/bmt/dead.wav").toString(); // Adjust path to your sound file
        playSound(soundFilePath);
    }

    public void playBoostSound() {
        String soundFilePath = getClass().getResource("/bmt/boost.wav").toString(); // Adjust path to your sound file
        playSound(soundFilePath);
    }
    
    // Method to play sound when the snake eats an apple
    public void playEatAppleSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = getClass().getResource("/bmt/food.wav").toString();
        playSound(soundFilePath);
    }

    public void playEatPoisonSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = getClass().getResource("/bmt/fart.wav").toString();
        playSound(soundFilePath);
    }

    public void playEatDeadSound() {
        // Adjust this path according to where your sound file is located
        String soundFilePath = getClass().getResource("/bmt/dead2.wav").toString();
        playSound(soundFilePath);
    }
}
