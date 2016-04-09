package de.don.paul;

import java.util.Random;
import java.util.Scanner;

/**
 * Created by windo on 26.03.2016.
 */
public class TheBot {

    private Scanner mScanner;

    private Random mRandom = new Random();

    private StrategyPlayer mPlayer;

    public void start() {
        this.mScanner = new Scanner(System.in);
        while (true) {
            String line = this.mScanner.nextLine();
            if (line.length() == 0) {
                continue;
            }
            final String[] parts = line.split(" ");
            switch (parts[0]) {
                case "settings":
                case "update":
                    BotStatus.parse(parts);
                    BotStatus.parse(parts);
                    break;
                case "action":
                    Field f = Field.obtain();
                    f.set(BotStatus.getGameField());
                    double[] weights = new double[5];
                    weights[0] = 1;
                    weights[1] = weights[0] / 3d * 2d;
                    weights[2] = weights[1] / 3d * 2d;
                    weights[3] = weights[2] / 3d * 2d;
                    weights[4] = weights[3] / 3d * 2d;
                    for (int i = 0; i < weights.length; i++) {
                        weights[i] = 0.5 + (weights[i] / 2);
                    }
                    mPlayer = new StrategyPlayer(BotStatus.getYourBotId(), weights);
                    System.out.println("place_disc " + mPlayer.takeTurn(f));
                    Field.free(f);
                    break;
                default:
                    break;
            }
        }
    }
}
