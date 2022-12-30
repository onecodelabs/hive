package com.onecodelabs.flags;

public class Flag<T> {

    private T value;

    public static <E> Flag<E> of(E value) {
        return new Flag(value);
    }

    public Flag(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
    public void set(T value) {
        this.value = value;
    }
}
