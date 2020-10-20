package com.github.MrMks.dev_tools_b;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.github.MrMks.dev_tools_b.lang.PlayerLocaleListener;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {
    @Override
    public void onEnable() {
        //PlayerLocaleManager.init(this);
        getServer().getPluginManager().registerEvents(new PlayerLocaleListener(LanguageAPI.PM), this);
        LanguageAPI.DEFAULT = new LanguageAPI(this);
        CommandManager.register(getCommand("dtb"));
    }

    @Override
    public void onDisable() {
        CommandManager.unregister(getCommand("dtb"));
        HandlerList.unregisterAll(this);
        LanguageAPI.PM.clear();
    }
}