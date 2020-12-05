package com.github.mrmks.dev_tools_b.lang.helper;

import com.github.mrmks.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.dev_tools_b.lang.LanguageHelper;

import java.util.Map;

public class LocaleHelper implements LanguageHelper {
    private final String locale;
    private final LanguageAPI api;
    public LocaleHelper(String locale, LanguageAPI api) {
        this.locale = locale;
        this.api = api;
    }

    @Override
    public boolean has(String code) {
        return api.hasKey(locale, code);
    }

    @Override
    public String trans(String code) {
        return api.getTranslation(locale, code);
    }

    @Override
    public String trans(String code, String def) {
        return api.hasKey(locale, code) ? api.getTranslation(locale, code) : def;
    }

    @Override
    public String trans(String code, Map<String, String> map) {
        return api.getTranslation(locale, code, map);
    }

    @Override
    public String trans(String code, String def, Map<String, String> map) {
        return api.hasKey(locale, code) ? api.getTranslation(locale, code, map) : api.getTranslation(locale, def, map);
    }
}
