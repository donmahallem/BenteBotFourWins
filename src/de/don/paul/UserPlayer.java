package de.don.paul;

import java.io.IOException;

/**
 * Created by Don on 01.04.2016.
 */
public class UserPlayer extends Player {

    public UserPlayer(int player) {
        super(player);
    }


    @Override
    public int takeTurn(Field field) {
        field.print2D();
        System.out.println("Take Turn: ");
        try {
            while (true) {
                int a = System.in.read() - 48;
                if (a > 9 || a < 0)
                    continue;
                return a;
            }

        } catch (IOException pE) {
            pE.printStackTrace();
        }
        return -1;
    }
}
