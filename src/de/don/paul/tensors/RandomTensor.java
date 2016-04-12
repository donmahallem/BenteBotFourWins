package de.don.paul.tensors;

import de.don.paul.Field;

import java.util.Random;

/**
 * Created by Don on 05.04.2016.
 */
public class RandomTensor extends Tensor {
    private final Random mRandom = new Random();

    public RandomTensor(int pPlayer) {
        super(pPlayer);
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        return this.mRandom.nextDouble();
    }

    @Override
    public String getName() {
        return "RandomTensor";
    }
}
