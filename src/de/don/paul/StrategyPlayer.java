package de.don.paul;

import de.don.paul.tensors.BlockingTensor;
import de.don.paul.tensors.FirstMoveTensor;
import de.don.paul.tensors.MinMaxTensor;
import de.don.paul.tensors.Tensor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don on 05.04.2016.
 */
public class StrategyPlayer extends Player {

    private List<Tensor> mTensorList = new ArrayList();

    public StrategyPlayer(int player) {
        super(player);
        this.mTensorList.add(new MinMaxTensor(player));
        this.mTensorList.add(new FirstMoveTensor(player));
        this.mTensorList.add(new BlockingTensor(player));
        //this.mTensorList.add(new RandomTensor(player));
    }

    @Override
    public int takeTurn(Field field) {
        Field f = field.clone();
        double val = 0;
        int take = -1;
        for (int i = 0; i < Field.WIDTH; i++) {
            if (field.isColumnFull(i))
                continue;
            f.copy(field);
            f.put(i, this.mPlayerId);
            double val2 = calcColumn(f, i);
            //System.out.println("Wert: "+i+" : "+val2);
            if (take == -1 || val < val2) {
                take = i;
                val = val2;
            }
        }
        return take;
    }

    private double calcColumn(Field pF, int take) {
        double res = 0;
        for (Tensor lTensor : this.mTensorList) {
            double v = lTensor.evaluate(pF, take);
            //System.out.println(lTensor.getName()+" - "+v);
            res += v;
        }
        return res / (1d * this.mTensorList.size());
    }
}
