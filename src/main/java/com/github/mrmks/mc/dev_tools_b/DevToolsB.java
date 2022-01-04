package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

import com.github.mrmks.mc.dev_tools_b.nbt.NBTUtils;
import com.github.mrmks.mc.dev_tools_b.utils.YamlConfigurationLoader;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class DevToolsB extends JavaPlugin {

    public static final int VER_BUILD = 22;
    public static final int VER_NBT = 1;
    public static final int VER_CMD = 1;
    public static final int VER_LANG = 1;

    @Override
    public void onLoad() {
        super.onLoad();
        YamlConfigurationLoader loader = new YamlConfigurationLoader(this, "nbtmap.yml");
        loader.saveDefaultConfig();
        NBTUtils.init(loader.getConfig());
    }

    @Override
    public void onEnable() {
        LanguageAPI.init(this);
        getCommand("dtbr").setExecutor(new ExecutorReload());
    }

    @Override
    public void onDisable() {
        getCommand("dtbr").setExecutor(null);
        HandlerList.unregisterAll(this);
        LanguageAPI.cleanup();
    }
}