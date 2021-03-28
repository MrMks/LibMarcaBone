package com.github.mrmks.mc.dev_tools_b.lang;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLocaleListener implements Listener {
    private final PlayerLocaleManager manager;
    public PlayerLocaleListener(PlayerLocaleManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        manager.put(player.getUniqueId(), player.getLocale().toLowerCase());
    }

    @EventHandler
    public void onPlayerLocaleChange(PlayerLocaleChangeEvent event){
        Player player = event.getPlayer();
        manager.put(player.getUniqueId(), event.getLocale().toLowerCase());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        manager.remove(event.getPlayer().getUniqueId());
    }

}
