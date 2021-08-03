package com.github.mrmks.mc.dev_tools_b.utils;

public class ArraySlice<T> {
    private final T[] src;
    private final int bi, ei;

    public ArraySlice(T[] src) {
        this(src, 0, src.length);
    }

    public ArraySlice(T[] src, int begin) {
        this(src, begin, src.length);
    }

    public ArraySlice(T[] src, int begin, int end) {
        chkBd(begin, end, src.length);
        this.src = src;
        this.bi = begin;
        this.ei = end;
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public int size() {
        return ei - bi;
    }

    public T at(int index) {
        chkBd(index);
        return src[index + bi];
    }

    public T first() {
        return at(0);
    }

    public T last() {
        return at(size() - 1);
    }

    public ArraySlice<T> slice(int begin) {
        return slice(begin, size());
    }

    public ArraySlice<T> slice(int begin, int end) {
        chkBd(begin, end, size());
        return new ArraySlice<>(src, bi + begin, bi + end);
    }

    private void chkBd(int index) {
        if (index < 0 || index >= size())
            throw new IndexOutOfBoundsException();
    }

    private void chkBd(int b, int e, int s) {
        if (b > e || b < 0 || e > s)
            throw new IndexOutOfBoundsException();
    }
}
