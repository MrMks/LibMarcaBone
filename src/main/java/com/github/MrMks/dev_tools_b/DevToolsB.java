package com.github.MrMks.dev_tools_b;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.github.MrMks.dev_tools_b.lang.PlayerLocaleListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {

    private CommandManager cmdManager;

    @Override
    public void onLoad() {
        super.onLoad();
        cmdManager = new CommandManager(this);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerLocaleListener(LanguageAPI.PM), this);
        LanguageAPI lapi = new LanguageAPI(this);
        LanguageAPI.DEFAULT  = lapi;
        cmdManager.register(lapi);
    }

    @Override
    public void onDisable() {
        cmdManager.unregister();
        HandlerList.unregisterAll(this);
        LanguageAPI.PM.clear();
    }
}