package de.don.paul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Field implements Comparable<Field>, Cloneable {
    public final static int WIDTH = 7, HEIGHT = 6;
    public final static int STATE_LOSS = 0, STATE_DRAW = 1, STATE_WIN = 2, STATE_OPEN = 3;
    public final static int VALUE_P1 = 1,
            VALUE_P2 = 2,
            VALUE_UNREACHABLE = 3,
            VALUE_OPEN = 0;
    private final int[] mField;
    private boolean mNormalized = false;

    public Field() {
        this(new int[WIDTH * HEIGHT]);
    }

    public Field(int[] ints) {
        this.mField = ints;
    }

    public static List<String> getChildIds(Field field, int playerId) {
        if (!field.isNormalized())
            throw new IllegalArgumentException("field must be normalized");
        List<String> ids = new ArrayList<>();
        Field f = new Field();
        for (int i = 0; i < WIDTH; i++) {
            if (field.isColumnFull(i))
                continue;
            f.copy(field);
            f.put(i, playerId);
            f.normalize();
            final String id = f.getId();
            if (!ids.contains(id))
                ids.add(id);
        }
        return ids;
    }

    public static Field fromId(String max) {
        Field field = new Field();
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            int val = max.charAt(i / 2) - 65;
            field.mField[i] = (val & (i % 2 == 0 ? 12 : 3)) >> (i % 2 == 0 ? 2 : 0);
        }
        return field;
    }

    public int[] getField() {
        return this.mField;
    }

    public int[] get() {
        return this.mField;
    }

    public int size() {
        return (int) Math.sqrt(this.mField.length);
    }

    public boolean isPossibleTurn(int x) {
        return (this.mField[this.mField.length - 1 - WIDTH + x] != 0);
    }

    public void mirror() {
        int left, right;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                left = (y * WIDTH) + x;
                right = (y * WIDTH) + (WIDTH - x - 1);
                int buf = this.mField[left];
                this.mField[left] = this.mField[right];
                this.mField[right] = buf;
            }
        }
        this.mNormalized = false;
    }

    @Override
    public Field clone() {
        Field field = new Field();
        for (int i = 0; i < this.mField.length; i++)
            field.mField[i] = this.mField[i];
        field.mNormalized = this.mNormalized;
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field that = (Field) o;

        return Arrays.equals(mField, that.mField);

    }

    public void copy(Field field) {
        for (int i = 0; i < field.mField.length; i++)
            this.mField[i] = field.mField[i];
        this.mNormalized = field.mNormalized;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mField);
    }

    /**
     * Normalizes the field and returns the transformation applied
     *
     * @return
     */
    public boolean normalize() {
        Field field = this.clone();
        field.mirror();
        if (field.hashCode() < this.hashCode()) {
            this.copy(field);
            return true;
        }
        this.mNormalized = true;
        return false;
    }

    public void print() {
        for (int i = 0; i < this.mField.length; i++)
            System.out.print(this.mField[i] + " ");
        System.out.println();
    }

    public void print2D() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                System.out.print(this.mField[(y * WIDTH) + x]);
            }
            System.out.println();
        }
    }

    public int compareTo(Field grid) {
        return Long.compare(this.hashCode(), grid.hashCode());
    }

    public String getId() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.mField.length; i += 2) {
            builder.append((char) (65 + (this.mField[i] << 2 | this.mField[i + 1])));
        }
        return builder.toString();
    }

    public int getState(int player) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                //CHECK HORIZONTAL
                if (x < WIDTH - 4) {
                    if (this.mField[(y * WIDTH) + x] != 0
                            && this.mField[(y * WIDTH) + x] == this.mField[(y * WIDTH) + x + 1]
                            && this.mField[(y * WIDTH) + x] == this.mField[(y * WIDTH) + x + 2]
                            && this.mField[(y * WIDTH) + x] == this.mField[(y * WIDTH) + x + 3])
                        return this.mField[(y * WIDTH) + x] == player ? Field.STATE_WIN : Field.STATE_LOSS;
                }
                if (y < HEIGHT - 4) {
                    if (this.mField[(y * WIDTH) + x] != 0
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 1) * WIDTH) + x]
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 2) * WIDTH) + x]
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 3) * WIDTH) + x])
                        return this.mField[(y * WIDTH) + x] == player ? Field.STATE_WIN : Field.STATE_LOSS;
                }
                //LEFT UP RIGHT DOWN
                if (x < WIDTH - 4 && y < HEIGHT - 4) {
                    if (this.mField[(y * WIDTH) + x] != 0
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 1) * WIDTH) + x + 1]
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 2) * WIDTH) + x + 2]
                            && this.mField[(y * WIDTH) + x] == this.mField[((y + 3) * WIDTH) + x + 3])
                        return this.mField[(y * WIDTH) + x] == player ? Field.STATE_WIN : Field.STATE_LOSS;
                    else if (this.mField[(y * WIDTH) + x + 3] != 0
                            && this.mField[(y * WIDTH) + x + 3] == this.mField[((y + 1) * WIDTH) + x + 2]
                            && this.mField[(y * WIDTH) + x + 3] == this.mField[((y + 2) * WIDTH) + x + 1]
                            && this.mField[(y * WIDTH) + x + 3] == this.mField[((y + 3) * WIDTH) + x])
                        return this.mField[(y * WIDTH) + x + 3] == player ? Field.STATE_WIN : Field.STATE_LOSS;
                }
            }
        }
        for (int i = 0; i < WIDTH; i++)
            if (this.mField[this.mField.length - 1 - WIDTH + i] == 0)
                return STATE_OPEN;
        return STATE_DRAW;
    }

    public int get(int i) {
        if (i < Field.WIDTH) {
            return this.mField[i];
        }
        if (this.mField[i - Field.WIDTH] == 0)
            return VALUE_UNREACHABLE;
        return this.mField[i];
    }

    public void clear() {
        for (int i = 0; i < this.mField.length; i++)
            this.mField[i] = 0;
    }

    public int get(int x, int y) {
        return this.get(x + (y * Field.WIDTH));
    }

    public int[] get(int... ids) {
        int[] ret = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            if (this.mField[ids[i]] == 0
                    && ids[i] >= Field.WIDTH
                    && this.mField[ids[i] - Field.WIDTH] == 0) {
                ret[i] = VALUE_UNREACHABLE;
            } else
                ret[i] = this.mField[ids[i]];
        }
        return ret;
    }

    public void put(int x, int player) {
        for (int y = Field.HEIGHT - 1; y > 0; y--) {
            if (this.mField[(y * WIDTH) + x] == 0 && this.mField[((y - 1) * WIDTH) + x] != 0) {
                this.mField[(y * WIDTH) + x] = player;
                return;
            }
        }
        if (this.mField[x] == 0)
            this.mField[x] = player;
        this.mNormalized = false;
    }

    public boolean isNormalized() {
        return mNormalized;
    }

    public boolean isColumnFull(int x) {
        return this.mField[this.mField.length - WIDTH + x] != 0;
    }

    public int getHighestCoin(int x) {
        for (int y = Field.HEIGHT - 1; y >= 0; y--) {
            if (this.get(x, y) == VALUE_P1 || this.get(x, y) == VALUE_P2)
                return y;
        }
        return 0;
    }
}