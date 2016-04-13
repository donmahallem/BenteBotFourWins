package de.don.paul.tensors;

import de.don.paul.Field;

import java.util.Random;

/**
 * Created by Don on 12.04.2016.
 */
public class MonteCarloTensor extends Tensor {

    private static final long DEFAULT_EXECUTION_TIME = 500 / 7;
    private final long mExecutionTime;
    private Random mRandom = new Random();

    public MonteCarloTensor(int pPlayer) {
        this(pPlayer, DEFAULT_EXECUTION_TIME);
    }

    /**
     * @param pPlayer
     * @param pExecutionTime
     */
    public MonteCarloTensor(int pPlayer, long pExecutionTime) {
        super(pPlayer);
        this.mExecutionTime = pExecutionTime;
    }

    @Override
    public double evaluate(Field field, int take, int turn) {
        double num = 0;
        double sum = 0;
        final long lStartTime = System.currentTimeMillis();
        Field f = Field.obtain();
        while (true) {
            f.copy(field);
            num++;
            sum += this.takeTurn(f, this.mPlayerId == 1 ? 2 : 1, turn);
            if ((System.currentTimeMillis() - lStartTime) > this.mExecutionTime) {
                //System.out.println("took: "+(System.currentTimeMillis()-lStartTime));
                break;
            }
        }
        //System.out.println(num+" with "+sum);
        return sum / num;
    }

    private double takeTurn(Field pField, int pCurrentPlayer, int pTurn) {
        final int state = pField.getState(this.mPlayerId);
        switch (state) {
            case Field.STATE_DRAW:
                //System.out.println("DRAW");
                return 0.55d;
            case Field.STATE_LOSS:
                //System.out.println("LOSS");
                return 0d;
            case Field.STATE_WIN:
                //System.out.println("WIN");
                return 1d;
        }
        int take = 0;
        while (true) {
            take = this.mRandom.nextInt(7);
            if (pField.isColumnFull(take))
                continue;
            //System.out.println("Take: "+take+" in Turn: "+pTurn);
            pField.put(take, pCurrentPlayer);
            return this.takeTurn(pField, pCurrentPlayer == 1 ? 2 : 1, pTurn + 1);
        }
    }

    @Override
    public String getName() {
        return "MonteCarloTensor";
    }
}
