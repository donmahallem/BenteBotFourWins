package de.don.paul.tensors;

import de.don.paul.Field;

import java.util.HashMap;

/**
 * Created by Don on 01.04.2016.
 */
public class MinMaxTensor2 extends Tensor {

    private HashMap<Integer, Double> mMemory = new HashMap<>();

    public MinMaxTensor2(int player) {
        super(player);
    }

    public double intTakeTurn(Field field, int currentPlayer, int depth, double alpha, double beta) {
        //System.out.println(depth+" - "+currentPlayer);
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
        if (depth > 6) {
            double num = 0, sum = 0;
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
                    if (x < Field.WIDTH - 3 && y >= 3) {
                        num++;
                        sum += getMemoryValue(field.get(x, y), field.get(x + 1, y - 1), field.get(x + 2, y - 2), field.get(x + 3, y - 3));
                    }
                }
            }
            return sum / num;
        }
        if (this.mPlayerId == currentPlayer) {
            //MAXIMIZING PLAYER
            double val = Double.MIN_VALUE;
            Field f = new Field();
            for (int x = 0; x < Field.WIDTH; x++) {
                if (field.isColumnFull(x)) {
                    continue;
                }
                f.copy(field);
                f.put(x, currentPlayer);
                val = Math.max(val, intTakeTurn(f, currentPlayer == 1 ? 2 : 1, depth + 1, alpha, beta));
                alpha = Math.max(alpha, val);
                if (beta <= alpha) {
                    //System.out.println("Cutoff");
                    break;
                }
            }
            return val;
        } else {
            //MINIMIZING PLAYER
            double val = Double.MAX_VALUE;
            Field f = new Field();
            for (int x = 0; x < Field.WIDTH; x++) {
                if (field.isColumnFull(x)) {
                    continue;
                }
                f.copy(field);
                f.put(x, currentPlayer);
                val = Math.min(val, intTakeTurn(f, currentPlayer == 1 ? 2 : 1, depth + 1, alpha, beta));
                beta = Math.min(beta, val);
                if (beta <= alpha) {
                    //System.out.println("Cutoff");
                    break;
                }
            }
            return val;
        }
    }

    private int createId(int... items) {
        int result = 0;
        for (int i = 0; i < items.length; i++) {
            result = result | ((items[i] & 3) << (2 * i));
        }
        return result;
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
        if (occ[0] == 4 || occ[3] == 4) {
            val = 0.5;
        } else if (occ[1] == 4) {
            val = 1;
        } else if (occ[2] == 4) {
            val = 0;
        } else if (occ[1] == 3 && (occ[0] == 1 || occ[3] == 1)) {
            val = 0.8;
        } else if (occ[2] == 3 && (occ[0] == 1 || occ[3] == 1)) {
            val = 0.2;
        } else if (occ[1] == 2 && occ[2] == 0) {
            val = 0.7;
        } else if (occ[2] == 2 && occ[1] == 0) {
            val = 0.3;
        } else if (occ[1] == 2 && occ[2] == 1) {
            val = 0.6;
        } else {
            val = 0.5;
        }
        this.mMemory.put(rowId, val);
        return val;
    }

    @Override
    public double evaluate(Field field, int take) {
        return intTakeTurn(field, this.mPlayerId == 1 ? 2 : 1, 0, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public String getName() {
        return "MinMaxTensor";
    }
}
