package de.don.paul;

import de.don.paul.tensors.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Don on 05.04.2016.
 */
public class StrategyPlayer extends Player {

    private List<Tensor> mTensorList = new ArrayList();

    public StrategyPlayer(int player, double... weights) {
        super(player);
        this.mTensorList.add(new MinMaxTensor2(player, weights));
        this.mTensorList.add(new FirstMoveTensor(player));
        this.mTensorList.add(new BlockingTensor(player));
        this.mTensorList.add(new WinningTensor(player));
        //this.mTensorList.add(new HorizontalTupelTensor(player));
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
            //System.err.println("Column - "+i);
            double val2 = calcColumn(f, i);
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
            //System.err.println(lTensor.getName()+" - "+v);
            res += v;
        }
        return res / (1d * this.mTensorList.size());
    }
}
