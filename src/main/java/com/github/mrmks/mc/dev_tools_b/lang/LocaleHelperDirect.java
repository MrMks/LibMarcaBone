package com.github.mrmks.mc.dev_tools_b.lang;

import java.util.Map;

public class LocaleHelperDirect implements LanguageHelper {

    private final LanguageFile file;
    LocaleHelperDirect(LanguageFile file) {
        this.file = file;
    }

    @Override
    public boolean has(String code) {
        return file.has(code);
    }

    @Override
    public String trans(String code) {
        return trans(code, code);
    }

    @Override
    public String trans(String code, String def) {
        if (code == null) return "";
        String str = file.get(code);
        return str == null ? (def == null ? "" : def) : str;
    }

    @Override
    public String trans(String code, Map<String, String> map) {
        return LanguageUtils.transWithTag(trans(code), map);
    }

    @Override
    public String trans(String code, String def, Map<String, String> map) {
        return LanguageUtils.transWithTag(trans(code, def), map);
    }
}
