package com.github.MrMks.dev_tools_b;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.github.MrMks.dev_tools_b.lang.PlayerLocaleManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {
    @Override
    public void onEnable() {
        PlayerLocaleManager.init(this);
        LanguageAPI api = LanguageAPI.load(this);
        CommandManager.register(getCommand("dtb"));
    }

    @Override
    public void onDisable() {
        CommandManager.unregister(getCommand("dtb"));
        PlayerLocaleManager.unload();
    }
}