package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 05.04.2016.
 */
public class BlockingTensor extends Tensor {

    public BlockingTensor(int pPlayer) {
        super(pPlayer);
    }

    @Override
    public double evaluate(Field field, int take) {
        final int enemyId = this.mPlayerId == 1 ? 2 : 1;
        int y = field.getHighestCoin(take);
        if (y == Field.HEIGHT - 1)
            return this.checkSourounding(field, take, y, enemyId) ? 10 : 0.5;
        else {
            if (this.checkSourounding(field, take, y, enemyId))
                return 10;
            else if (this.checkSourounding(field, take, y + 1, enemyId))
                return -10;
            else return 0.5;
        }
    }

    private boolean checkSourounding(Field field, int x, int y, int enemyId) {
        for (int i = -3; i <= 0; i++) {
            if (checkDir(field, x + i, y + i, 1, 1, enemyId)) {
                return true;
            } else if (checkDir(field, x + i, y, 1, 0, enemyId)) {
                return true;
            } else if (checkDir(field, x + i, y - i, 1, -1, enemyId)) {
                return true;
            }
        }
        return checkDir(field, x, y - 3, 0, 1, enemyId);
    }

    @Override
    public String getName() {
        return "BlockingTensor";
    }


    private boolean checkDir(Field field,
                             int cX,
                             int cY,
                             int xDir,
                             int yDir,
                             int playerId) {
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
            if (val == playerId)
                enemies++;
        }
        return enemies == 3;
    }
}
