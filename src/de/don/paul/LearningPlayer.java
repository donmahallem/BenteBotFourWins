package de.don.paul;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by windo on 22.03.2016.
 */
public class LearningPlayer extends Player {


    private final static double LEARNING_RATE = 0.1f;
    private final boolean mExploring;
    private final Random mRandom = new Random();
    private final int[][] TUPEL_MAP = {{33, 39, 26, 38, 27, 35, 20, 25},
            {16, 10, 22, 24, 30, 17, 2, 31},
            {14, 7, 1, 8, 6, 9, 0, 3},
            {15, 9, 3, 1, 4, 11, 18, 19},
            {30, 38, 29, 23, 36, 28, 31, 22},
            {20, 12, 21, 26, 22, 25, 19, 33},
            {15, 21, 27, 13, 28, 5, 23, 31},
            {29, 30, 36, 37, 21, 27, 35, 38},
            {39, 32, 25, 33, 38, 41, 24, 19},
            {13, 7, 5, 4, 11, 6, 12, 15},
            {27, 34, 21, 28, 13, 40, 15, 7},
            {20, 14, 12, 18, 17, 25, 8, 33},
            {38, 39, 30, 33, 27, 37, 36, 29},
            {7, 0, 1, 8, 15, 9, 22, 3},
            {3, 2, 11, 10, 16, 18, 22, 9},
            {23, 15, 30, 37, 16, 24, 31, 29},
            {36, 35, 29, 41, 30, 22, 40, 33},
            {41, 34, 28, 27, 26, 29, 25, 31},
            {30, 22, 21, 15, 24, 16, 32, 40},
            {33, 26, 25, 32, 24, 40, 17, 31},
            {10, 3, 2, 11, 18, 4, 1, 16},
            {39, 40, 38, 30, 34, 36, 23, 16},
            {15, 9, 7, 1, 8, 0, 6, 14},
            {26, 19, 34, 20, 32, 28, 36, 12},
            {31, 30, 23, 16, 9, 17, 38, 11},
            {34, 41, 40, 33, 28, 39, 32, 22},
            {12, 13, 18, 19, 24, 20, 10, 3},
            {28, 36, 20, 14, 7, 27, 15, 13},
            {13, 6, 5, 7, 12, 4, 15, 23},
            {0, 7, 6, 13, 20, 26, 15, 28}};
    private HashMap<Integer, HashMap<Integer, Double>> mMemory;
    private HashMap<Integer, HashMap<Integer, Double>> mTupelMemory = new HashMap<>();

    public LearningPlayer(int player, boolean exploring) {
        this(player, new HashMap<Integer, HashMap<Integer, Double>>(), exploring);
    }

    public LearningPlayer(int player, HashMap<Integer, HashMap<Integer, Double>> memory, boolean exploring) {
        super(player);
        this.mMemory = memory;
        this.mExploring = exploring;
    }

    public static void storeMemory(HashMap<Integer, HashMap<Integer, Double>> memory) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("test.csv");
        for (int tupel : memory.keySet()) {
            for (int item : memory.get(tupel).keySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(tupel)
                        .append(",")
                        .append(item)
                        .append(",")
                        .append(memory.get(tupel).get(item))
                        .append("\r\n");
                fileOutputStream.write(stringBuilder.toString().getBytes());
            }
        }
        fileOutputStream.close();
    }

    public static HashMap<Integer, HashMap<Integer, Double>> loadMemory(File file) throws IOException {
        HashMap<Integer, HashMap<Integer, Double>> memory = new HashMap<Integer, HashMap<Integer, Double>>();
        BufferedReader fileOutputStream = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        while ((line = fileOutputStream.readLine()) != null) {
            if (line == null || line.length() == 0)
                continue;
            String[] splits = line.split(",");
            if (splits.length != 3)
                continue;
            int tupel = Integer.parseInt(splits[0]);
            int id = Integer.parseInt(splits[1]);
            double value = Double.parseDouble(splits[2]);
            if (!memory.containsKey(tupel)) {
                memory.put(tupel, new HashMap<>());
            }
            memory.get(tupel).put(id, value);
        }

        fileOutputStream.close();
        return memory;
    }

    private static double recalculateValue(double current, double next) {
        return current + (LEARNING_RATE * (next - current));
    }

    private static int createTuple(Field field, int tuple, int length) {
        int ret = 0;
        int y = 0;
        for (int x = 0; x < Field.HEIGHT; x++) {
            y = (tuple + x % 2) % Field.HEIGHT;
            ret = ret | field.get((y * Field.WIDTH) + x) << 2;
        }
        return ret;
    }

    private static double getDefaultValueForState(int state) {
        switch (state) {
            case Field.STATE_DRAW:
                return 0.55d;
            case Field.STATE_WIN:
                return 1d;
            case Field.STATE_LOSS:
                return 0d;
            case Field.STATE_OPEN:
                return 0.5d;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    public HashMap<Integer, HashMap<Integer, Double>> getMemory() {
        return mMemory;
    }

    @Override
    public int takeTurn(Field field) {
        int take;
        final float rand = this.mRandom.nextFloat();
        if (this.mExploring && rand < 0.1f) {
            //System.out.println("Exploring");
            take = this.mRandom.nextInt(Field.WIDTH);
            while (field.isColumnFull(take))
                take = this.mRandom.nextInt(Field.WIDTH);
        } else {
            double tempValue = this.mPlayerId == 1 ? Double.MIN_VALUE : Double.MAX_VALUE;
            take = -1;
            Field fieldNormalized = field.clone();
            for (int x = 0; x < Field.WIDTH; x++) {
                if (field.isColumnFull(x))
                    continue;
                fieldNormalized.copy(field);
                fieldNormalized.put(x, this.mPlayerId);
                fieldNormalized.normalize();
                double value = getValueSumFor(fieldNormalized);
                //System.out.println(x+"|"+tempValue+"|"+value);
                if (this.mPlayerId == 1) {
                    if (tempValue < value || take == -1) {
                        tempValue = value;
                        take = x;
                    }
                } else {
                    if (tempValue > value || take == -1) {
                        tempValue = value;
                        take = x;
                    }
                }
            }
        }
        if (rand >= 0.1f && this.mExploring) {
            Field f2 = field.clone();
            f2.put(take, this.mPlayerId);
            f2.normalize();
            updateMemory(field, f2);
        }
        return take;
    }

    private void updateMemory(Field field, Field f2) {
        final double valSum = getValueSumFor(f2);
        final int state = f2.getState(1);
        for (int i = 0; i < TUPEL_MAP.length; i++) {
            int id = createTupleId(field.get(TUPEL_MAP[i]));
            double val = getValueForTupel(i, id, LearningPlayer.getDefaultValueForState(state));
            this.updateValueForTupel(i, id, recalculateValue(val, valSum));
        }
    }

    private void updateValueForTupel(int tupel, int tupelId, double value) {
        if (!this.mMemory.containsKey(tupel))
            this.mMemory.put(tupel, new HashMap<>());
        this.mMemory.get(tupel).put(tupelId, value);
    }

    private double getValueSumFor(Field field) {
        double sum = 0;
        for (int i = 0; i < TUPEL_MAP.length; i++) {
            int id = createTupleId(field.get(TUPEL_MAP[i]));
            sum += getValueForTupel(i,
                    id,
                    LearningPlayer.getDefaultValueForState(field.getState(1)));
        }
        return sum / (TUPEL_MAP.length * 1d);
    }

    private double getValuesForTuples(Field field) {
        double res = 0;
        for (int i = 0; i < 6; i++) {
            //res+=this.lookupTuple(this.createTuple(field,i,6));
        }
        return res;
    }

    private double lookupTuple(int tupleNum, int tuple) {
        final HashMap<Integer, Double> mem = this.mTupelMemory.get(tupleNum);
        if (mem.containsKey(tuple))
            return mem.get(tuple);
        return 0;
    }

    public int createTupleId(int... tupel) {
        int ret = 0;
        for (int i = 0; i < tupel.length; i++) {
            ret = ret | ((tupel[i] & 3) << (i * 2));
        }
        return ret;
    }

    public double getValueForTupel(int tupel, int[] tupelId, double defaultValue) {
        return this.getValueForTupel(tupel, createTupleId(tupelId), defaultValue);
    }

    public double getValueForTupel(int tupel, int tupelId, double defaultValue) {
        if (!this.mMemory.containsKey(tupel))
            this.mMemory.put(tupel, new HashMap<>());
        HashMap<Integer, Double> map = this.mMemory.get(tupel);
        if (!map.containsKey(tupelId)) {
            map.put(tupelId, defaultValue);
        }
        return map.get(tupelId);
    }


}
