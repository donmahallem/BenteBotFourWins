package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 07.04.2016.
 */
public class WinningTensor extends Tensor {
    public WinningTensor(int pPlayer) {
        super(pPlayer);
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        int y = field.getHighestCoin(take);
        if (checkSourounding(field, take, y))
            return 100;
        else
            return 0.5;
    }

    private boolean checkSourounding(Field field, int x, int y) {
        for (int i = -3; i <= 0; i++) {
            if (checkDir(field, x + i, y + i, 1, 1)) {
                return true;
            } else if (checkDir(field, x + i, y, 1, 0)) {
                return true;
            } else if (checkDir(field, x + i, y - i, 1, -1)) {
                return true;
            }
        }
        return checkDir(field, x, y - 3, 0, 1);
    }

    @Override
    public String getName() {
        return "WinningTensor";
    }


    private boolean checkDir(Field field,
                             int cX,
                             int cY,
                             int xDir,
                             int yDir) {
        if (cX < 0 ||
                cX >= Field.WIDTH ||
                cY < 0 ||
                cY >= Field.HEIGHT)
            return false;
        if ((cX - 3 < 0 && xDir < 0) ||
                (cX + 3 >= Field.WIDTH && xDir > 0)) {
            return false;
        }
        if ((cY - 3 < 0 && yDir < 0) ||
                (cY + 3 >= Field.HEIGHT && yDir > 0)) {
            return false;
        }
        int enemies = 0;
        for (int i = 0; i < 4; i++) {
            final int val = field.get(cX + (i * xDir), cY + (i * yDir));
            if (val == this.mPlayerId)
                enemies++;
        }
        return enemies == 4;
    }
}
