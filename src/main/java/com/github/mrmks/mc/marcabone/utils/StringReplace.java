package com.github.mrmks.mc.marcabone.utils;

@Deprecated
public class StringReplace {
    private final StringUtils.StringReplace ins;
    public StringReplace(String str) {
        ins = StringUtils.replacer(str);
    }

    public StringReplace replace(String key, String value) {
        ins.replaceTag(key, value);
        return this;
    }

    @Override
    public String toString() {
        return ins.toString();
    }
}
