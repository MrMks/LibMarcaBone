package com.github.MrMks.dev_tools_b.lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class PlayerLocaleManager implements Listener {
    public static String getLocale(UUID uuid) {
        return ins == null ? "" : ins.get(uuid);
    }

    public static boolean hasLocale(UUID uuid) {
        return ins != null && ins.has(uuid);
    }

    private static PlayerLocaleManager ins = null;
    public static void init(JavaPlugin plugin) {
        if (ins != null) return;
        ins = new PlayerLocaleManager();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            ins.put(player.getUniqueId(), player.getLocale().toLowerCase());
        }
        plugin.getServer().getPluginManager().registerEvents(ins, plugin);
    }

    public static void unload(){
        HandlerList.unregisterAll(ins);
        ins.clear();
        ins = null;
    }

    private final HashMap<UUID, String> map = new HashMap<>();
    private void put(UUID uuid, String locale){
        map.put(uuid, locale);
    }

    private boolean has(UUID uuid) {
        return map.containsKey(uuid);
    }

    private String get(UUID uuid) {
        return map.getOrDefault(uuid, "");
    }

    private void clear(){
        map.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        map.putIfAbsent(player.getUniqueId(), player.getLocale().toLowerCase());
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event){
        Player player = event.getPlayer();
        map.put(player.getUniqueId(), event.getLocale().toLowerCase());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        map.remove(event.getPlayer().getUniqueId());
    }
}
