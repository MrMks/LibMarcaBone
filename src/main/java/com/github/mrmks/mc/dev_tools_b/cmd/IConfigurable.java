package com.github.mrmks.mc.dev_tools_b.cmd;

import org.bukkit.configuration.ConfigurationSection;

/**
 * This interface will handle all config read and write in commands.
 * If you are going to make your own SubCommand, I recommend you to implement this Interface so that user can edit properties without edit the source code.
 *
 * @see ICommandPackage ICommandPackage
 */
public interface IConfigurable {

    void loadDefaultConfiguration();

    void loadConfiguration(ConfigurationSection section);

    void saveConfiguration(ConfigurationSection section);

}
