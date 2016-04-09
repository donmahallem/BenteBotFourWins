package de.don.paul.util;

import java.util.Stack;

/**
 * Created by Don on 08.04.2016.
 */
public abstract class Pool<T> {
    private Stack<T> mStack = new Stack();

    protected abstract T create();

    public synchronized T obtain() {
        if (this.mStack.size() == 0) {
            return this.create();
        }
        return this.mStack.pop();
    }

    public synchronized void free(T t) {
        if (t instanceof Poolable)
            ((Poolable) t).reset();
        this.mStack.push(t);
    }

    public interface Poolable {
        void reset();
    }
}
