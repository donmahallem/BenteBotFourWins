package de.don.paul;

import java.util.Random;

/**
 * Created by windo on 22.03.2016.
 */
public class RandomPlayer extends Player {
    private Random mRandom;

    public RandomPlayer(int player, long seed) {
        super(player);
        this.mRandom = new Random(seed);
    }

    public RandomPlayer(int player) {
        this(player, System.currentTimeMillis());
    }

    @Override
    public int takeTurn(Field field, int turn) {
        int take = this.mRandom.nextInt(Field.WIDTH);
        while (field.isColumnFull(take)) {
            take = this.mRandom.nextInt(Field.WIDTH);
        }
        return take;
    }
}
