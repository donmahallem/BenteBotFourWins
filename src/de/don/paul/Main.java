package de.don.paul;

import de.don.paul.tensors.MinMaxTensor;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("playit")) {
            Field field = new Field();
            MinMaxTensor lMinMaxTensor = new MinMaxTensor(1);
            System.out.println(lMinMaxTensor.evaluate(field, 1));
            Player[] players = new Player[2];
            //HashMap<Integer,HashMap<Integer,Double>> memory=LearningPlayer.loadMemory(new File("test.csv"));
            //players[1]=new LearningPlayer(1,memory,false);
            double[] weights = new double[4];
            weights[0] = 0.9;
            weights[1] = 0.8;
            weights[2] = 0.7;
            weights[3] = 0.6;
            players[0] = new StrategyPlayer(1, weights);
            players[1] = new UserPlayer(2);//RandomPlayer(2,System.currentTimeMillis());
            Field lField = new Field();
            System.out.println(lField.getState(1));
            System.out.println(playMatch(players[0], players[1]));
        } else if (args.length > 0 && args[0].equals("tryWeights")) {
            Field field = new Field();
            Player[] players = new Player[2];
            double[] weights = new double[4];
            weights[0] = 0.75;
            weights[1] = 0.75;
            weights[2] = 0.75;
            weights[3] = 0.75;
            Random lRandom = new Random();
            for (int i = 0; i < 1000; i++) {
                double[] testWeights = new double[4];
                testWeights[0] = lRandom.nextDouble();
                testWeights[1] = lRandom.nextDouble() * testWeights[0];
                testWeights[2] = lRandom.nextDouble() * testWeights[1];
                testWeights[3] = lRandom.nextDouble() * testWeights[2];
                for (int a = 0; a < testWeights.length; a++) {
                    testWeights[a] = testWeights[a] * 0.5d + 0.5d;
                }
                players[0] = new StrategyPlayer(1, weights);
                players[1] = new StrategyPlayer(2, testWeights);
                if (playMatch(players[i % 2], players[(i + 1) % 2]) == 2) {
                    weights = testWeights;
                    System.out.println(i + " rand better: " + Arrays.toString(weights));
                }
                System.out.println("old better");
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
        Field field = new Field();
        int move = 0;
        while (true) {
            final long startTime = System.currentTimeMillis();
            int take = (move % 2 == 0 ? p1 : p2).takeTurn(field);
            //System.out.println((move%2==0?p1:p2).getPlayerId()+" - "+take);
            field.put(take, (move % 2 == 0 ? p1.mPlayerId : p2.mPlayerId));
            if (field.getState(move % 2 + 1) != Field.STATE_OPEN)
                return field.getState(p1.getPlayerId());
            move++;
            System.out.println("Move: " + move + " took: " + ((System.currentTimeMillis() - startTime) / 1000f) + "s");
            if (move > Field.WIDTH * Field.HEIGHT + 2)
                throw new Exception("TOO MANY MOVES FOR A NORMAL FIELD");
        }
    }
}
