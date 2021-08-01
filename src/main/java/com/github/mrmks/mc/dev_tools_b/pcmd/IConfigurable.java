package com.github.mrmks.mc.dev_tools_b.pcmd;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurable {
    String getConfigKey();
    void loadConfig(ConfigurationSection section);
}
