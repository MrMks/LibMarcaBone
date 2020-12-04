package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.utils.YamlConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {
    private static CommandMap commandMap;
    private static boolean reflected = false;

    public static void register(Plugin plugin, CommandPackage pack) {
        reflectCommandMap();
        if (commandMap != null) {
            loadCommandConfig(plugin, pack);
            HashMap<CommandProperty, ICommandFunction> map = pack.getMap();
            List<Command> commands = new ArrayList<>();
            for (CommandProperty property : map.keySet()) {
                if (property.isValid()) commands.add(new CommandWrapper(plugin, property, map.get(property)));
            }
            commandMap.registerAll(plugin.getDescription().getName(), commands);
        }
    }

    public static void unregister(Plugin plugin) {
        reflectCommandMap();
    }

    private static void loadCommandConfig(Plugin plugin, CommandPackage commandPackage) {
        YamlConfigLoader loader = new YamlConfigLoader(plugin, "commands.yml");
        loader.saveDefaultConfig();
        ConfigurationSection section = loader.getConfig();

        for (Map.Entry<CommandProperty, ICommandFunction> entry : commandPackage.getMap().entrySet()) {
            if (section.isConfigurationSection(entry.getKey().getName())) {
                section = section.getConfigurationSection(entry.getKey().getName());
                entry.getKey().loadConfiguration(section);
                if (entry.getValue() instanceof IConfigurable && !entry.getKey().isShortcut()) {
                    ((IConfigurable) entry.getValue()).loadConfiguration(section);
                }
            }
        }

        for (Map.Entry<CommandProperty, ICommandFunction> entry : commandPackage.getMap().entrySet()) {
            entry.getKey().saveConfiguration(section);
            if (entry.getValue() instanceof IConfigurable && !entry.getKey().isShortcut()) {
                ((IConfigurable) entry.getValue()).saveConfiguration(section);
            }
        }

        loader.saveConfig();
    }

    private static void reflectCommandMap() {
        if (!reflected && commandMap == null) {
            try {
                Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                Object obj = field.get(Bukkit.getPluginManager());
                if (obj instanceof CommandMap) {
                    commandMap = (CommandMap) obj;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                reflected = true;
            }
        }
    }

}
