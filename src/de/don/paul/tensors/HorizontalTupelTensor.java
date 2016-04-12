package de.don.paul.tensors;

import de.don.paul.Field;

import java.util.HashMap;

/**
 * Created by Don on 07.04.2016.
 */
public class HorizontalTupelTensor extends Tensor {

    private HashMap<Integer, Double> mMemory = new HashMap<>();

    public HorizontalTupelTensor(int pPlayer) {
        super(pPlayer);
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        double result = takeTurnInt(field);
        return (this.mPlayerId == 1) ? result : (1 - result);
    }

    private double takeTurnInt(Field field) {
        double sum = 0, num = 0;
        for (int x = 0; x < Field.WIDTH; x++) {
            for (int y = 0; y < Field.HEIGHT; y++) {
                //Vertical
                if (y < Field.HEIGHT - 3) {
                    num++;
                    sum += getMemoryValue(field.get(x, y), field.get(x, y + 1), field.get(x, y + 2), field.get(x, y + 3));
                }
                //Horizontal
                if (x < Field.WIDTH - 3) {
                    num++;
                    sum += getMemoryValue(field.get(x, y), field.get(x + 1, y), field.get(x + 2, y), field.get(x + 3, y));
                }
                //Right Down
                if (x < Field.WIDTH - 3 && y < Field.HEIGHT - 3) {
                    num++;
                    sum += getMemoryValue(field.get(x, y), field.get(x + 1, y + 1), field.get(x + 2, y + 2), field.get(x + 3, y + 3));
                }
                //Right Up
                if (x >= 3 && y >= 3) {
                    num++;
                    sum += getMemoryValue(field.get(x, y), field.get(x - 1, y - 1), field.get(x - 2, y - 2), field.get(x - 3, y - 3));
                }
            }
        }
        return sum / num;
    }

    private double getMemoryValue(int... values) {
        final int rowId = createId(values);
        if (this.mMemory.containsKey(rowId))
            return this.mMemory.get(rowId);
        int[] occ = new int[4];
        for (int i = 0; i < values.length; i++) {
            occ[values[i]]++;
        }
        double val = 0;
        val += occ[0] * 0.5d;
        val += occ[1] * 1;
        val += occ[2] * 0;
        val += occ[3] * 0.6;

        val = val / (values.length * 1d);
        this.mMemory.put(rowId, val);
        return val;
    }

    private double getMemoryValue(int pRowId, Field pField) {
        if (this.mMemory.containsKey(pRowId))
            return this.mMemory.get(pRowId);
        final int state = pField.getState(1);
        switch (state) {
            case Field.STATE_DRAW:
                this.mMemory.put(pRowId, 0.55);
                return 0.55;
            case Field.STATE_LOSS:
                this.mMemory.put(pRowId, 0.0);
                return 0.0;
            case Field.STATE_WIN:
                this.mMemory.put(pRowId, 1d);
                return 1;
            case Field.STATE_OPEN:
                this.mMemory.put(pRowId, 0.5);
                return 0.5;
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    private int createId(int... items) {
        int result = 0;
        for (int i = 0; i < items.length; i++) {
            result = result | ((items[i] & 3) << (2 * i));
        }
        return result;
    }

    @Override
    public String getName() {
        return "HorizontalTupelTensor";
    }
}
