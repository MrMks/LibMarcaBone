package com.github.mrmks.mc.dev_tools_b.cmd;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurable {
    default boolean hasConfigKey() {
        return true;
    }
    String getConfigKey();
    void loadConfig(ConfigurationSection section);
}
