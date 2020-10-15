package com.github.MrMks.dev_tools_b.lang;

import java.util.HashMap;

public class LanguageFile {
    private final HashMap<String, String> map;
    private final String locale;
    public LanguageFile(String locale, HashMap<String, String> nMap) {
        this.locale = locale;
        this.map = new HashMap<>();
        merge(nMap);
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
}
