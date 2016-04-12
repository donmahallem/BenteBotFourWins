package de.don.paul;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("playit")) {
            System.out.println("Play It");
            Field field = Field.obtain();
            Player[] players = new Player[2];
            //HashMap<Integer,HashMap<Integer,Double>> memory=LearningPlayer.loadMemory(new File("test.csv"));
            //players[1]=new LearningPlayer(1,memory,false);
            double[] weights = new double[5];
            weights[0] = 1;
            weights[1] = weights[0] / 3d * 2d;
            weights[2] = weights[1] / 3d * 2d;
            weights[3] = weights[2] / 3d * 2d;
            weights[4] = weights[3] / 3d * 2d;
            for (int i = 0; i < weights.length; i++) {
                weights[i] = 0.5 + (weights[i] / 2);
            }
            players[1] = new StrategyPlayer(2, weights);
            players[0] = new UserPlayer(1);//RandomPlayer(2,System.currentTimeMillis());
            System.out.println("match : " + playMatch(players[0], players[1]));
        } else if (args.length > 0 && args[0].equals("selfPlay")) {
            Field field = Field.obtain();
            Player[] players = new Player[2];
            //HashMap<Integer,HashMap<Integer,Double>> memory=LearningPlayer.loadMemory(new File("test.csv"));
            //players[1]=new LearningPlayer(1,memory,false);
            double[] weights = new double[5];
            weights[0] = 1;
            weights[1] = weights[0] / 3d * 2d;
            weights[2] = weights[1] / 3d * 2d;
            weights[3] = weights[2] / 3d * 2d;
            weights[4] = weights[3] / 3d * 2d;
            players[0] = new StrategyPlayer(1, weights);
            players[1] = new StrategyPlayer(2, weights);//RandomPlayer(2,System.currentTimeMillis());
            final long start = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                System.out.println("match " + i + ": " + playMatch(players[0], players[1]));
            }
            System.out.println("" + ((System.currentTimeMillis() - start) / 1000));
        } else if (args.length > 0 && args[0].equals("tryWeights")) {
            Field field = Field.obtain();
            Player[] players = new Player[2];
            double[] weights = new double[5];
            weights[0] = 1;
            weights[1] = weights[0] / 3d * 2d;
            weights[2] = weights[1] / 3d * 2d;
            weights[3] = weights[2] / 3d * 2d;
            weights[4] = weights[3] / 3d * 2d;
            for (int i = 0; i < weights.length; i++) {
                weights[i] = 0.5 + (weights[i] / 2);
            }
            System.out.println(Arrays.toString(weights));
            for (int i = 0; i < 100; i++) {
                players[0] = new StrategyPlayer(1, weights);
                players[1] = new StrategyPlayer(2, weights);
                int result = playMatch(players[i % 2], players[(i + 1) % 2]);
                System.out.println(i + " - center: " + result);
            }
            System.out.println(Arrays.toString(weights));
        } else {
            new TheBot().start();
        }
    }


    private static boolean contains(int[] arr, int value) {
        for (int a : arr)
            if (value == a)
                return true;
        return false;
    }

    public static int playMatch(Player p1, Player p2) throws Exception {
        Field field = Field.obtain();
        int move = 0;
        while (true) {
            final long startTime = System.currentTimeMillis();
            int take = (move % 2 == 0 ? p1 : p2).takeTurn(field, move);
            //System.out.println((move%2==0?p1:p2).getPlayerId()+" - "+take);
            field.put(take, (move % 2 == 0 ? p1.mPlayerId : p2.mPlayerId));
            final int state = field.getState(move % 2 + 1);
            if (state != Field.STATE_OPEN) {
                Field.free(field);
                return state;
            }
            move++;
            //System.out.println("Move: " + move + " took: " + ((System.currentTimeMillis() - startTime) / 1000f) + "s");
            if (move > Field.WIDTH * Field.HEIGHT + 2)
                throw new Exception("TOO MANY MOVES FOR A NORMAL FIELD");
        }
    }
}
