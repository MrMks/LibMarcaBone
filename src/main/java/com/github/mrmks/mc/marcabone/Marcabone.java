package com.github.mrmks.mc.marcabone;

import com.github.mrmks.mc.marcabone.lang.LanguageAPI;

import com.github.mrmks.mc.marcabone.nbt.NBTUtils;
import com.github.mrmks.mc.marcabone.utils.YamlConfigurationLoader;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Marcabone extends JavaPlugin {

    public static final int VER_BUILD = 23;
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