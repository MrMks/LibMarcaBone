package com.github.mrmks.dev_tools_b.lang;

import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.UUID;

/**
 * DO NOT use this class directly
 */
public class PlayerLocaleManager implements Listener {

    /**
     * @see LanguageAPI#PM
     * @see LanguageAPI#helper(UUID)
     * @see LanguageHelper
     */
    @Deprecated
    public static String getLocale(UUID uuid) {
        return LanguageAPI.PM.get(uuid);
    }

    /**
     * @see LanguageAPI#PM
     * @see LanguageAPI#helper(UUID)
     * @see LanguageHelper
     */
    @Deprecated
    public static boolean hasLocale(UUID uuid) {
        return LanguageAPI.PM.has(uuid);
    }

    private final HashMap<UUID, String> map = new HashMap<>();
    public void put(UUID uuid, String locale){
        map.put(uuid, locale);
    }

    public void remove(UUID uuid) {
        map.remove(uuid);
    }

    public boolean has(UUID uuid) {
        return map.containsKey(uuid);
    }

    public String get(UUID uuid) {
        return map.getOrDefault(uuid, "");
    }

    public void clear(){
        map.clear();
    }
}
