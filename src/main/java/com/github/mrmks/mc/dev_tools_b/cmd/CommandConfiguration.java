package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.YamlConfigurationLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class CommandConfiguration {
    private final YamlConfigurationLoader loader;
    public CommandConfiguration(Plugin plugin) {
        this.loader = new YamlConfigurationLoader(plugin, "commands.yml");
        loader.saveDefaultConfig();
    }

    public <T extends IConfigurable> T loadCommand(T cmd) {
        if (cmd != null && cmd.hasConfigKey()) {
            String path = cmd.getConfigKey();
            ConfigurationSection cfg = loader.getConfig();
            cfg = cfg.isConfigurationSection(path) ? cfg.getConfigurationSection(path) : cfg.createSection(path);
            cmd.loadConfig(cfg);
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
