package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.lang.PlayerLocaleManager;
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
        getServer().getPluginManager().registerEvents(PlayerLocaleManager.getInstance().generateListener(), this);
        LanguageAPI api = new LanguageAPI(this);
        LanguageAPI.initDefault(api);
        cmdManager.register(api);
    }

    @Override
    public void onDisable() {
        //cmdManager.unregister();
        HandlerList.unregisterAll(this);
        PlayerLocaleManager.getInstance().clear();
    }
}