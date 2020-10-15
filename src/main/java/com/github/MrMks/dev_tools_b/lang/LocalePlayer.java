package com.github.MrMks.dev_tools_b.lang;

import java.util.Map;
import java.util.UUID;

public class LocalePlayer {
    private final UUID uuid;
    private final LanguageAPI api;
    public LocalePlayer(UUID uuid, LanguageAPI api){
        this.uuid = uuid;
        this.api = api;
    }

    public boolean hasKey(String key) {
        return api.hasKey(uuid, key);
    }

    public String trans(String key) {
        return api.getTranslation(uuid, key);
    }

    public String trans(String key, Map<String, String> map) {
        return api.getTranslationWithTag(uuid, key, map);
    }
}
