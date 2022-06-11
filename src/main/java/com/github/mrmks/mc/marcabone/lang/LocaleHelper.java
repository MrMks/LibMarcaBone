package com.github.mrmks.mc.marcabone.lang;

import com.github.mrmks.mc.marcabone.utils.StringUtils;

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
        if (file != null && file.has(code)) str = file.get(code);
        if (str == null && defFile != null && defFile.has(code)) str = defFile.get(code);
        if (str == null) str = def;
        return str;
    }

    @Override
    public String trans(String code, String key, String value) {
        return StringUtils.replaceTag(trans(code, code), key, value);
    }

    @Override
    public String trans(String code, String def, String key, String value) {
        return StringUtils.replaceTag(trans(code, def), key, value);
    }

    @Override
    public String trans(String code, String[] kv) {
        return StringUtils.replaceTag(trans(code), kv);
    }

    @Override
    public String trans(String code, String def, String[] kv) {
        return StringUtils.replaceTag(trans(code, def), kv);
    }

    @Override
    public String trans(String code, Map<String, String> map) {
        return StringUtils.replaceTag(trans(code), map);
    }

    @Override
    public String trans(String code, String def, Map<String, String> map) {
        return StringUtils.replaceTag(trans(code, def), map);
    }
}
