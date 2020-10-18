package com.github.MrMks.dev_tools_b.utils;

import com.google.common.base.Charsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class YamlConfigLoader {
    private final Plugin plugin;
    private final String strFile;
    private final File configFile;

    private FileConfiguration newConfig;

    public YamlConfigLoader(Plugin plugin, String file){
        this.plugin = plugin;
        file = file.replace("\\", "/");
        this.strFile = file;
        this.configFile = new File(plugin.getDataFolder(), file);
    }

    public FileConfiguration getConfig(){
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    public void saveConfig(){
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) saveDefaultConfig(false);
    }

    public void saveDefaultConfig(boolean replace) {
        plugin.saveResource(strFile, replace);
    }

    public void reloadConfig(){
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(strFile);
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @Deprecated
    public boolean exist(){
        return configFile.exists();
    }

}
