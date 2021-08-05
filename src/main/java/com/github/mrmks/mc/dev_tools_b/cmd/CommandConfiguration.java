package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.YamlConfigurationLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class CommandConfiguration {
    private YamlConfigurationLoader loader;
    public CommandConfiguration(Plugin plugin) {
        this.loader = new YamlConfigurationLoader(plugin, "commands.yml");
        loader.saveDefaultConfig();
    }

    public ISubCommand loadCommand(ISubCommand cmd) {
        if (cmd instanceof IConfigurable) {
            IConfigurable ccmd = (IConfigurable) cmd;
            String path = ccmd.getConfigKey();
            ConfigurationSection cfg = loader.getConfig();
            cfg = cfg.isConfigurationSection(path) ? cfg.getConfigurationSection(path) : cfg.createSection(path);
            ccmd.loadConfig(cfg);
        }
        return cmd;
    }

    public void save() {
        loader.saveConfig();
    }

    public void reload() {
        loader.reloadConfig();
    }
}
