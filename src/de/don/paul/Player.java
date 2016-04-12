package de.don.paul;

/**
 * Created by windo on 22.03.2016.
 */
public abstract class Player {

    protected final int mPlayerId;

    public Player(int player) {
        this.mPlayerId = player;
    }

    public abstract int takeTurn(Field field, int turn);

    public int getPlayerId() {
        return mPlayerId;
    }
}
