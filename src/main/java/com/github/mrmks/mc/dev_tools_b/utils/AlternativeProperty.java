package com.github.mrmks.mc.dev_tools_b.utils;

public class AlternativeProperty<T> {
    private final T value_def;
    private T value_alter;

    public AlternativeProperty(T _def) {
        value_def = _def;
    }

    public T getValue() {
        if (value_alter != null) return value_alter;
        else return value_def;
    }

    public void setAlter(T _alter) {
        value_alter = _alter;
    }

    public void removeAlter() {
        value_alter = null;
    }
}
