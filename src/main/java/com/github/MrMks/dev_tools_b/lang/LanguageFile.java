package com.github.MrMks.dev_tools_b.lang;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class LanguageFile {
    public static final LanguageFile EMPTY = new LanguageFile("", new HashMap<>());

    private final HashMap<String, String> map = new HashMap<>();
    private final String locale;
    private LanguageFile(String locale, HashMap<String, String> nMap) {
        this.locale = locale;
        merge(nMap);
    }

    public LanguageFile(ConfigurationSection section, String localeKey, String transKey) {
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

    public String getLocale(){
        return locale;
    }

    public boolean has(String key){
        return map.containsKey(key);
    }

    public String get(String key){
        return map.getOrDefault(key,key);
    }

    public String getOrDefault(String key, String value){
        return map.getOrDefault(key, value);
    }

    public void merge(HashMap<String, String> map){
        map.forEach(this.map::putIfAbsent);
    }

    public void merge(LanguageFile nf) {
        if (this.locale.equals(nf.locale)) merge(nf.map);
    }
}
