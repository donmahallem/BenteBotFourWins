package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 01.04.2016.
 */
public abstract class Tensor {

    protected final int mPlayerId;

    public Tensor(int pPlayer) {
        this.mPlayerId = pPlayer;
    }


    public abstract double evaluate(Field field, int take);

    public abstract String getName();
}
