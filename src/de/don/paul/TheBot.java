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
                    Field f = new Field(BotStatus.getGameField());
                    mPlayer = new StrategyPlayer(BotStatus.getYourBotId());
                    System.out.println("place_disc " + mPlayer.takeTurn(f));
                    break;
                default:
                    break;
            }
        }
    }
}
