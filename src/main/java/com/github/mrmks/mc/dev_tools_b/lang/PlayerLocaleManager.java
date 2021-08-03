package com.github.mrmks.mc.dev_tools_b.lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

/**
 * DO NOT use this class directly
 */
class PlayerLocaleManager {

    private static PlayerLocaleManager instance;
    static PlayerLocaleManager getInstance() {
        if (instance == null) instance = new PlayerLocaleManager();
        return instance;
    }

    private final HashMap<UUID, String> map = new HashMap<>();

    void put(UUID uuid, String locale){
        map.put(uuid, locale);
    }

    void remove(UUID uuid) {
        map.remove(uuid);
    }

    boolean has(UUID uuid) {
        return map.containsKey(uuid);
    }

    String get(UUID uuid) {
        return map.getOrDefault(uuid, "");
    }

    void clear(){
        map.clear();
        cache = null;
    }

    private Listener cache = null;
    org.bukkit.event.Listener generateListener() {
        if (cache == null) cache = new Listener();
        return cache;
    }

    public class Listener implements org.bukkit.event.Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event){
            if (this != cache) return;
            Player player = event.getPlayer();
            PlayerLocaleManager.this.put(player.getUniqueId(), player.getLocale().toLowerCase());
        }

        @EventHandler
        public void onPlayerLocaleChange(PlayerLocaleChangeEvent event){
            if (this != cache) return;
            Player player = event.getPlayer();
            PlayerLocaleManager.this.put(player.getUniqueId(), event.getLocale().toLowerCase());
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event){
            if (this != cache) return;
            PlayerLocaleManager.this.remove(event.getPlayer().getUniqueId());
        }
    }
}
