package bmt.models;

public class FoodModel extends FoodModelBase {
    public FoodModel() {
        super("/bmt/apple.png");
    }

    @Override
    public void spawn() {
        super.spawn();
        // Vous pouvez ajouter d'autres logiques spécifiques ici
    }
}
