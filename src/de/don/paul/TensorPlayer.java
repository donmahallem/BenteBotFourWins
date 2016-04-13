package de.don.paul;

import de.don.paul.tensors.Tensor;

/**
 * Created by Don on 12.04.2016.
 */
public class TensorPlayer extends Player {

    private final Tensor[] mTensors;

    public TensorPlayer(int player, Tensor... pTensors) {
        super(player);
        this.mTensors = pTensors;
    }

    @Override
    public int takeTurn(Field field, int turn) {
        double hightest = 0;
        int take = -1;
        Field lField = Field.obtain();
        for (int i = 0; i < 7; i++) {
            if (field.isColumnFull(i))
                continue;
            lField.copy(field);
            lField.put(i, this.mPlayerId);
            double val = runTensors(lField, i, turn + 1);
            //System.out.println("T: "+i+ " = "+val);
            if (val > hightest || take == -1) {
                hightest = val;
                take = i;
            }
        }
        return take;
    }

    private final double runTensors(Field pField, int pTake, int pTurn) {
        if (this.mTensors.length == 1)
            return this.mTensors[0].evaluate(pField, pTake, pTurn);
        double sum = 0;
        for (Tensor lTensor : this.mTensors) {
            sum += lTensor.evaluate(pField, pTake, pTurn);
        }
        return sum / (1d * this.mTensors.length);
    }
}
