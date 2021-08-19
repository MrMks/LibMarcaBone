package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

import com.github.mrmks.mc.dev_tools_b.nbt.NBTUtils;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();
        NBTUtils.init();
    }

    @Override
    public void onEnable() {
        LanguageAPI.init(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        LanguageAPI.cleanup();
    }
}