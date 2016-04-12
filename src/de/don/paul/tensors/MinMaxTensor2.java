package de.don.paul.tensors;

import de.don.paul.Field;

/**
 * Created by Don on 01.04.2016.
 */
public class MinMaxTensor2 extends Tensor {

    private final double[] mWeights;
    private int mBreakDepth = 6;

    public MinMaxTensor2(int player) {
        this(player, 0.9, 0.8, 0.7, 0.6);
    }

    public MinMaxTensor2(int pPlayer, double... weights) {
        super(pPlayer);
        this.mWeights = weights;
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
        if (depth > this.mBreakDepth) {
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
            Field f = Field.obtain();
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
            Field.free(f);
            return val;
        } else {
            //MINIMIZING PLAYER
            double val = Double.MAX_VALUE;
            Field f = Field.obtain();
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
            Field.free(f);
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

    private boolean patternMatch(int[] block, int... pattern) {
        return (pattern[0] == block[0]
                && block[1] == pattern[1]
                && block[2] == pattern[2]
                && block[3] == pattern[3]);
    }

    private double getMemoryValue(int... values) {
        int[] occ = new int[4];
        for (int i = 0; i < values.length; i++) {
            occ[values[i]]++;
        }
        final int enemyId = this.mPlayerId == 1 ? 2 : 1;
        if (occ[this.mPlayerId] == 4) {
            return this.mWeights[0];
        } else if (occ[enemyId] == 4) {
            return 1 - this.mWeights[0];
        } else if (occ[0] == 4) {
            return 0.5;
        } else if (occ[3] == 4) {
            return 0.5;
        } else if (occ[this.mPlayerId] == 3
                && occ[enemyId] == 0) {
            return this.mWeights[1];
        } else if (occ[enemyId] == 3
                && occ[this.mPlayerId] == 0) {
            return 1 - this.mWeights[1];
        } else if (occ[this.mPlayerId] == 3
                && occ[enemyId] == 1) {
            return this.mWeights[2];
        } else if (occ[enemyId] == 3
                && occ[this.mPlayerId] == 1) {
            return 1 - this.mWeights[2];
        } else if (occ[this.mPlayerId] == 2
                && occ[enemyId] == 0) {
            return this.mWeights[3];
        } else if (occ[enemyId] == 2
                && occ[this.mPlayerId] == 0) {
            return 1 - this.mWeights[3];
        } else if (occ[this.mPlayerId] == 2
                && occ[enemyId] == 1) {
            return this.mWeights[4];
        } else if (occ[enemyId] == 2
                && occ[this.mPlayerId] == 1) {
            return 1 - this.mWeights[4];
        } else {
            return 0.5;
        }
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        if (turn > 10) {
            this.mBreakDepth = 7;
        } else if (turn > 15) {
            this.mBreakDepth = 8;
        }
        return intTakeTurn(field, this.mPlayerId == 1 ? 2 : 1, 0, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Override
    public String getName() {
        return "MinMaxTensor";
    }
}
