package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 05.04.2016.
 */
public class FirstMoveTensor extends Tensor {
    public FirstMoveTensor(int pPlayer) {
        super(pPlayer);
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        int nums = 0;
        for (int i = 0; i < Field.WIDTH; i++) {
            if (field.get(i) != 0 && (field.get(i + 7) == 0))
                nums++;
        }
        if (take == 3 && nums == 1) {
            return 1;
        } else
            return 0.5;
    }

    @Override
    public String getName() {
        return "FirstMoveTensor";
    }
}
