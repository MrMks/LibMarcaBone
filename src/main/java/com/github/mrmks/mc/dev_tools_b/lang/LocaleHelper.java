package com.github.mrmks.mc.dev_tools_b.lang;

import java.util.Map;

public class LocaleHelper implements LanguageHelper {

    public static final LocaleHelper EMPTY = new LocaleHelper();

    private final LanguageFile file, defFile;
    LocaleHelper(LanguageFile file, LanguageFile def) {
        this.file = file;
        this.defFile = def;
    }

    LocaleHelper(LanguageFile file) {
        this.file = file;
        this.defFile = null;
    }

    private LocaleHelper() {
        this.file = null;
        this.defFile = null;
    }

    @Override
    public boolean has(String code) {
        return (defFile != null && defFile.has(code)) || (file != null && file.has(code));
    }

    @Override
    public String trans(String code) {
        return trans(code, code);
    }

    @Override
    public String trans(String code, String def) {
        def = def == null ? "" : def;
        if (code == null) return def;
        String str = null;
        if (file != null) str = file.get(code);
        if (defFile != null && str == null) str = defFile.get(code);
        if (str == null) str = def;
        return str;
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
