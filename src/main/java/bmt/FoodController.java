package bmt;

public class FoodController {
    private final FoodModel foodModel;
    private final GamePanelView gameView;

    public FoodController(final FoodModel foodModel, final GamePanelView gameView) {
        this.foodModel = foodModel;
        this.gameView = gameView;

    }
}
