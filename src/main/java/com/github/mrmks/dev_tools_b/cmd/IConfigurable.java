package com.github.mrmks.dev_tools_b.cmd;

import org.bukkit.configuration.ConfigurationSection;

public interface IConfigurable {

    void loadDefaultConfiguration();

    void loadConfiguration(ConfigurationSection section);

    void saveConfiguration(ConfigurationSection section);

}
