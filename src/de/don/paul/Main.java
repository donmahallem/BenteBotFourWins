package de.don.paul;

import de.don.paul.tensors.MinMaxTensor;

import java.io.FileOutputStream;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("createTuple")) {
            Random rand = new Random();
            List<Integer> list = new ArrayList<>();
            int current = 0, last;
            int[][] arr = new int[30][8];
            for (int i = 0; i < 30; i++) {
                int runs = 0;
                while (list.size() < 8) {
                    int r = rand.nextInt(Field.WIDTH * Field.HEIGHT);
                    if (list.contains(r))
                        continue;
                    if (list.size() == 0) {
                        list.add(r);
                    } else {
                        if (list.contains(r + 7)
                                || list.contains(r - 7)
                                || list.contains(r + 1)
                                || list.contains(r - 1)
                                || list.contains(r + 8)
                                || list.contains(r - 8)
                                || list.contains(r + 6)
                                || list.contains(r - 6))
                            list.add(r);
                    }
                    runs++;
                    if (runs > 200)
                        break;
                }
                if (list.size() == 8) {
                    for (int a = 0; a < list.size(); a++) {
                        arr[i][a] = list.get(a);
                    }
                    System.out.println(Arrays.toString(arr[i]));
                }
                list.clear();
            }
            for (int i = 0; i < Field.WIDTH * Field.HEIGHT; i++) {
                int occ = 0;
                for (int j = 0; j < arr.length; j++) {
                    if (contains(arr[j], i))
                        occ++;
                    if (occ >= 3)
                        break;
                }
                if (occ < 3)
                    throw new IllegalStateException(i + " not often enough");
            }
            return;
        } else if (args.length > 0 && args[0].equals("createMapfile")) {
            FileOutputStream fileOutputStream = new FileOutputStream("Memory.java");
            HashMap<Integer, HashMap<Integer, Double>> mem = null;
            for (int i : mem.keySet()) {
                for (int j : mem.get(i).keySet()) {
                    fileOutputStream.write(("MEMORY.get(" + i + ").put(" + j + "," + mem.get(i).get(j) + ");\r\n").getBytes());
                }
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            return;
        } else if (args.length > 0 && args[0].equals("learn")) {
            Field field = new Field();
            Player[] players = new Player[2];
            HashMap<Integer, HashMap<Integer, Double>> memory = new HashMap<>();
            players[0] = new LearningPlayer(1, memory, true);
            players[1] = new LearningPlayer(2, memory, false);//RandomPlayer(2,System.currentTimeMillis());
            for (int i = 0; i < 10000; i++) {
                int move = 0;
                int[] map = new int[4];
                while (move < 10000) {
                    map[playMatch(players[0], players[1])]++;
                    move++;
                }
                System.out.println(i);
                System.out.println(Arrays.toString(map));
                System.out.println(((LearningPlayer) players[0]).getMemory().size());
                LearningPlayer.storeMemory(((LearningPlayer) players[0]).getMemory());
            }
            return;
        } else if (args.length > 0 && args[0].equals("playit")) {
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
            int take = (move % 2 == 0 ? p1 : p2).takeTurn(field);
            //System.out.println((move%2==0?p1:p2).getPlayerId()+" - "+take);
            field.put(take, (move % 2 == 0 ? p1.mPlayerId : p2.mPlayerId));
            if (field.getState(move % 2 + 1) != Field.STATE_OPEN)
                return field.getState(p1.getPlayerId());
            move++;
            if (move > Field.WIDTH * Field.HEIGHT + 2)
                throw new Exception("TOO MANY MOVES FOR A NORMAL FIELD");
        }
    }
}
