package com.github.mrmks.mc.dev_tools_b.utils;

public class StringReplace {
    private final String str;
    private StringBuilder bd;
    public StringReplace(String str) {
        if (str == null) throw new IllegalArgumentException("Source string can't be null");
        this.str = str;
    }

    public StringReplace replace(String key, String value) {
        if (key != null) {
            if (value == null) value = "";
            if (bd == null) bd = new StringBuilder(str);
            StringUtils.replace(bd, key, value);
        }
        return this;
    }

    @Override
    public String toString() {
        return bd == null ? str : bd.toString();
    }
}
