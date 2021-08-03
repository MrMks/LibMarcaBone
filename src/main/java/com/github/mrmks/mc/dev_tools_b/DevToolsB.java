package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {

    private CommandManager cmdManager = null;

    @Override
    public void onLoad() {
        super.onLoad();
        cmdManager = new CommandManager(this);
    }

    @Override
    public void onEnable() {
        if (cmdManager == null) cmdManager = new CommandManager(this);
        LanguageAPI.init(this);
        LanguageAPI api = new LanguageAPI(this);
        cmdManager.register(api);
    }

    @Override
    public void onDisable() {
        if (cmdManager != null) cmdManager.unregister();
        HandlerList.unregisterAll(this);
        LanguageAPI.cleanup();
    }
}