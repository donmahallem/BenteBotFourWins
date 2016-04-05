package de.don.paul;

/**
 * Created by windo on 26.03.2016.
 */
public class BotStatus {
    private final static String TIMEBANK = "timebank_t",
            TIME_PER_MOVE = "time_per_move_t",
            PLAYER_NAMES = "player_names",
            YOUR_BOT = "your_bot",
            YOUR_BOT_ID = "your_botid",
            FIELD_COLUMNS = "field_columns",
            FIELD_ROWS = "field_rows";
    private static int mYourBotId;
    private static int mFieldColumns;
    private static int mFieldRows;
    private static int mGameRound;
    private static int[] mGameField;

    public static int getYourBotId() {
        return mYourBotId;
    }

    public static int getFieldColumns() {
        return mFieldColumns;
    }

    public static int getFieldRows() {
        return mFieldRows;
    }

    public static int getGameRound() {
        return mGameRound;
    }

    public static int[] getGameField() {
        return mGameField;
    }

    public static void parse(String... args) {
        if (args[0].equals("settings")) {
            switch (args[1]) {
                case YOUR_BOT:
                case PLAYER_NAMES:
                case TIME_PER_MOVE:
                    break;
                case FIELD_COLUMNS:
                    mFieldColumns = Integer.parseInt(args[2]);
                    if (mFieldColumns != 7)
                        throw new RuntimeException("Unsupported column number: " + mFieldColumns);
                    break;
                case FIELD_ROWS:
                    mFieldRows = Integer.parseInt(args[2]);
                    if (mFieldRows != 6)
                        throw new RuntimeException("Unsupported row number: " + mFieldRows);
                    break;
                case YOUR_BOT_ID:
                    mYourBotId = Integer.parseInt(args[2]);
                    break;
            }
        } else if (args[0].equalsIgnoreCase("update")) {
            if (args[1].equalsIgnoreCase("game")) {
                if (args[2].equalsIgnoreCase("round")) {
                    mGameRound = Integer.parseInt(args[3]);
                } else if (args[2].equalsIgnoreCase("field")) {
                    mGameField = parseField(args[3]);
                }
            }
        }
    }

    private static int[] parseField(String field) {
        int[] ret = new int[mFieldColumns * mFieldRows];
        int y = BotStatus.getFieldRows() - 1;
        for (String row : field.split(";")) {
            String[] elements = row.split(",");
            for (int x = 0; x < elements.length; x++) {
                ret[(y * BotStatus.getFieldColumns()) + x] = Integer.parseInt(elements[x]);
            }
            y--;
        }
        return ret;
    }
}
