package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 01.04.2016.
 */
public class MinMaxTensor extends Tensor {

    public MinMaxTensor(int player) {
        super(player);
    }


    public double intTakeTurn(Field field, int currentPlayer, int depth) {
        //System.out.println(depth+" - "+currentPlayer);
        if (depth > 6)
            return 0.5d;
        final int state = field.getState(this.mPlayerId);
        switch (state) {
            case Field.STATE_DRAW:
                //System.out.println("Draw");
                return 0.55d;
            case Field.STATE_LOSS:
                //System.out.println("Loss");
                return 0d;
            case Field.STATE_WIN:
                //System.out.println("Win");
                return 1d;
        }
        double sum = 0;
        double take = 0;
        for (int x = 0; x < Field.WIDTH; x++) {
            if (field.isColumnFull(x)) {
                continue;
            }
            take++;
            Field f = field.clone();
            f.put(x, currentPlayer);
            sum += intTakeTurn(f, currentPlayer == 1 ? 2 : 1, depth + 1);
        }
        return sum / take;
    }

    @Override
    public double evaluate(Field field, int take) {
        return intTakeTurn(field, this.mPlayerId == 1 ? 2 : 1, 0);
    }

    @Override
    public String getName() {
        return "MinMaxTensor";
    }
}
