package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {

    private CommandManager cmdManager = null;
    private LanguageAPI api = null;

    @Override
    public void onLoad() {
        super.onLoad();
        cmdManager = new CommandManager(this);
    }

    @Override
    public void onEnable() {
        if (cmdManager == null) cmdManager = new CommandManager(this);
        LanguageAPI.init(this);
        if (api == null) api = new LanguageAPI(this);
        cmdManager.register(api, getCommand("dtb"));
    }

    @Override
    public void onDisable() {
        if (cmdManager != null) cmdManager.unregister(getCommand("dtb"));
        HandlerList.unregisterAll(this);
        LanguageAPI.cleanup();
    }
}