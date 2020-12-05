package com.github.mrmks.dev_tools_b.lang.helper;

import com.github.mrmks.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.dev_tools_b.lang.LanguageHelper;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class LocalePlayer implements LanguageHelper {
    private final UUID uuid;
    private final LanguageAPI api;
    public LocalePlayer(UUID uuid, LanguageAPI api){
        this.uuid = uuid;
        this.api = api;
    }

    public LocalePlayer(Player player, LanguageAPI api) {
        this(player.getUniqueId(), api);
    }

    @Deprecated
    public boolean hasKey(String key) {
        return api.hasKey(uuid, key);
    }

    @Override
    public boolean has(String code) {
        return api.hasKey(uuid, code);
    }

    public String trans(String key) {
        return api.getTranslation(uuid, key);
    }

    @Override
    public String trans(String code, String def) {
        return api.hasKey(uuid, code) ? api.getTranslation(uuid, code) : def;
    }

    public String trans(String key, Map<String, String> map) {
        return api.getTranslation(uuid, key, map);
    }

    @Override
    public String trans(String code, String def, Map<String, String> map) {
        return api.getTranslation(uuid, has(code) ? code : def, map);
    }
}
