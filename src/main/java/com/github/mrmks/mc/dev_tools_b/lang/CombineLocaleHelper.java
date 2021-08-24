package com.github.mrmks.mc.dev_tools_b.lang;

import com.github.mrmks.mc.dev_tools_b.utils.StringUtils;

import java.util.Map;

public class CombineLocaleHelper implements LanguageHelper {

    private final LanguageHelper fir, sec;
    public CombineLocaleHelper(LanguageHelper fir, LanguageHelper sec) {
        this.fir = fir;
        this.sec = sec;
    }

    @Override
    public boolean has(String code) {
        return fir.has(code) || sec.has(code);
    }

    @Override
    public String trans(String code) {
        return trans(code, code);
    }

    @Override
    public String trans(String code, String def) {
        if (fir.has(code)) return fir.trans(code, def);
        else if (sec.has(code)) return sec.trans(code, def);
        else return def == null ? "" : def;
    }

    @Override
    public String trans(String code, String key, String value) {
        return StringUtils.replace(trans(code), key, value);
    }

    @Override
    public String trans(String code, String def, String key, String value) {
        return StringUtils.replace(trans(code, def), key, value);
    }

    @Override
    public String trans(String code, Map<String, String> map) {
        return StringUtils.replace(trans(code), map);
    }

    @Override
    public String trans(String code, String def, Map<String, String> map) {
        return StringUtils.replace(trans(code, def), map);
    }
}
