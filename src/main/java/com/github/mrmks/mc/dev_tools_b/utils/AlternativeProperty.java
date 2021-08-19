package com.github.mrmks.mc.dev_tools_b.utils;

public class AlternativeProperty<T> {
    private final T value_def;
    private final T value_null;
    private T value_alter;
    private boolean use_alter;

    public AlternativeProperty(T _def) {
        value_def = _def;
        value_null = null;
    }

    public AlternativeProperty(T _def, T _null) {
        value_def = _def;
        value_null = _null;
        if (_null == null) throw new IllegalArgumentException();
    }

    public T getValue() {
        T v = use_alter ? value_alter : value_def;
        return v == null ? value_null : v;
    }

    public void setAlter(T _alter) {
        if (_alter == null) {
            value_alter = null;
            use_alter = value_def != null;
        } else {
            if (_alter == value_def || _alter.equals(value_alter)) removeAlter();
            else {
                value_alter = _alter;
                use_alter = true;
            }
        }
    }

    public void removeAlter() {
        value_alter = null;
        use_alter = false;
    }
}
