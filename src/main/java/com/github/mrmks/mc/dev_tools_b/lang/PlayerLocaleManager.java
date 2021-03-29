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
public class PlayerLocaleManager {

    private static PlayerLocaleManager instance;
    public static PlayerLocaleManager getInstance() {
        if (instance == null) instance = new PlayerLocaleManager();
        return instance;
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

    public org.bukkit.event.Listener generateListener() {
        return new Listener();
    }

    public class Listener implements org.bukkit.event.Listener {
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent event){
            Player player = event.getPlayer();
            PlayerLocaleManager.this.put(player.getUniqueId(), player.getLocale().toLowerCase());
        }

        @EventHandler
        public void onPlayerLocaleChange(PlayerLocaleChangeEvent event){
            Player player = event.getPlayer();
            PlayerLocaleManager.this.put(player.getUniqueId(), event.getLocale().toLowerCase());
        }

        @EventHandler
        public void onPlayerQuit(PlayerQuitEvent event){
            PlayerLocaleManager.this.remove(event.getPlayer().getUniqueId());
        }
    }
}
