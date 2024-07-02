package bmt;

public class SnakeModel {
    private static final int UNIT_SIZE = 20;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    private int[] x = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];
    private int[] y = new int[WIDTH * HEIGHT / UNIT_SIZE / UNIT_SIZE];

    private int length = 5;
    private int foodEaten;
    private char direction = 'R'; // Direction initiale

    private boolean running = true;

    public SnakeModel() {
        // Initialisation du serpent
        x[0] = 0;
        y[0] = 0;
    }

    public void move() {
        // Déplacer le serpent en fonction de la direction
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'L':
                x[0] -= UNIT_SIZE;
                break;
            case 'R':
                x[0] += UNIT_SIZE;
                break;
            case 'U':
                y[0] -= UNIT_SIZE;
                break;
            case 'D':
                y[0] += UNIT_SIZE;
                break;
        }
    }

    public void checkCollision() {
        // Vérifier la collision avec les bords de l'écran
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        // Vérifier la collision avec le corps du serpent
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        if (!running) {
            // Arrêter le jeu ou effectuer d'autres actions
        }
    }

    public void checkFood(int foodX, int foodY) {
        // Vérifier si la tête du serpent a mangé de la nourriture
        if (x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
        }
    }

    public void eatFood(FoodModel foodModel) {
        if (x[0] == foodModel.getFoodX() && y[0] == foodModel.getFoodY()) {
            length++;
            foodEaten++;
            foodModel.spawn(); // Réinitialiser la position de la nourriture

            // Ajouter un nouveau segment de serpent à la position actuelle de la tête
            x[length - 1] = x[length - 2];
            y[length - 1] = y[length - 2];
        }
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public int getLength() {
        return length;
    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public boolean isRunning() {
        return running;
    }

    public void restartGame() {
        length = 5;
        foodEaten = 0;
        direction = 'R';
        running = true;

        // Réinitialisation de la position du serpent
        x[0] = 0;
        y[0] = 0;
        for (int i = 1; i < length; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    }
}
