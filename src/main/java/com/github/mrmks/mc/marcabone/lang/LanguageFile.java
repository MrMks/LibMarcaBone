package com.github.mrmks.mc.marcabone.lang;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class LanguageFile {
    static final LanguageFile EMPTY = new LanguageFile("", Collections.emptyMap());

    private final Map<String, String> map = new HashMap<>();
    private final String locale;
    private LanguageFile(String locale, Map<String, String> nMap) {
        this.locale = locale;
        merge(nMap);
    }

    LanguageFile(ConfigurationSection section, String localeKey, String transKey) {
        if (section != null) {
            locale = section.getString(localeKey, "").toLowerCase();
            ConfigurationSection trans = section.getConfigurationSection(transKey);
            if (trans != null) {
                trans.getKeys(true).forEach(key -> map.put(key, trans.getString(key)));
            }
        } else {
            locale = "";
        }
    }

    String getLocale(){
        return locale;
    }

    boolean has(String key){
        return key != null && map.containsKey(key);
    }

    String get(String key){
        return key == null ? "" : map.getOrDefault(key,key);
    }

    String getOrDefault(String key, String value){
        return key == null ? "" : map.getOrDefault(key, value);
    }

    private void merge(Map<String, String> map){
        if (map != null && !map.isEmpty()) {
            map.forEach(this.map::putIfAbsent);
        }
    }

    void merge(LanguageFile nf) {
        if (nf != null && this.locale.equals(nf.locale)) merge(nf.map);
    }
}
