package de.don.paul;

import de.don.paul.tensors.MinMaxTensor;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("playit")) {
            Field field = new Field();
            MinMaxTensor lMinMaxTensor = new MinMaxTensor(1);
            System.out.println(lMinMaxTensor.evaluate(field, 1));
            Player[] players = new Player[2];
            //HashMap<Integer,HashMap<Integer,Double>> memory=LearningPlayer.loadMemory(new File("test.csv"));
            //players[1]=new LearningPlayer(1,memory,false);
            players[0] = new StrategyPlayer(1);
            players[1] = new UserPlayer(2);//RandomPlayer(2,System.currentTimeMillis());
            Field lField = new Field();
            System.out.println(lField.getState(1));
            System.out.println(playMatch(players[0], players[1]));
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
