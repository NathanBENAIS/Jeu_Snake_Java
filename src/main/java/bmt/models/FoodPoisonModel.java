package bmt.models;

import bmt.models.interfaces.IFoodPoisonModel;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FoodPoisonModel extends FoodModelBase implements IFoodPoisonModel {
    private Timer spawnTimer;
    private Timer disappearTimer;

    public FoodPoisonModel() {
        super("/bmt/vert.png");
        setupTimers();
    }

    @Override
    public void spawn() {
        super.spawn();
    }

    public void despawn() {
        visible = false;
    }

    private void setupTimers() {
        spawnTimer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spawn();
                disappearTimer.start();
            }
        });

        disappearTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                despawn();
                disappearTimer.stop();
            }
        });

        spawnTimer.start();
    }
}
