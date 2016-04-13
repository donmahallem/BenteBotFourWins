package de.don.paul;

import de.don.paul.tensors.MonteCarloTensor;

/**
 * Created by Don on 12.04.2016.
 */
public class MonteCarloPlayer extends TensorPlayer {
    public MonteCarloPlayer(int player) {
        super(player, new MonteCarloTensor(player));
    }
}
