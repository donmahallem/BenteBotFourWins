package de.don.paul;

/**
 * Created by Don on 01.04.2016.
 */
public class MinMaxPlayer extends Player {

    final int MAX_DEPTH = 8;
    public MinMaxPlayer(int player) {
        super(player);
    }

    @Override
    public int takeTurn(Field field) {
        final long startTime = System.currentTimeMillis();
        int take = -1;
        double highest = -Double.MIN_VALUE;
        for (int x = 0; x < Field.WIDTH; x++) {
            if (field.isColumnFull(x))
                continue;
            double result = intTakeTurn(field, this.mPlayerId, 0);
            System.out.println(x + " - " + result);
            if (result > highest || take == -1) {
                take = x;
                highest = result;
            }
        }
        System.out.println("depth: " + MAX_DEPTH + " took: " + ((System.currentTimeMillis() - startTime) / 1000f) + "s");
        return take;
    }

    public double intTakeTurn(Field field, int currentPlayer, int depth) {
        if (depth == MAX_DEPTH / 2)
            System.out.println(depth + " - " + currentPlayer);
        if (depth > MAX_DEPTH)
            return 0.5;
        final int state = field.getState(this.mPlayerId);
        switch (state) {
            case Field.STATE_DRAW:
                //System.out.println("Draw");
                return 0.55;
            case Field.STATE_LOSS:
                //System.out.println("Loss");
                return 0;
            case Field.STATE_WIN:
                //System.out.println("Win");
                return 1;
        }

        double sum = 0;
        int takes = 0;
        for (int x = 0; x < Field.WIDTH; x++) {
            if (field.isColumnFull(x)) {
                continue;
            }
            takes++;
            Field f = field.clone();
            f.put(x, currentPlayer);
            sum += intTakeTurn(f, currentPlayer == 1 ? 2 : 1, depth + 1);
        }
        return sum / takes;
    }

}
