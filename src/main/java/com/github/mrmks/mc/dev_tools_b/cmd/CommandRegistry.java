package com.github.mrmks.mc.dev_tools_b.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class CommandRegistry {
    private static CommandMap commandMap;
    private static boolean reflected = false;

    private static final HashSet<String> registeredPlugins = new HashSet<>();

    public static void register(Plugin plugin, CommandPackage pack) {
        reflectCommandMap();
        if (commandMap != null && !registeredPlugins.contains(plugin.getName())) {
            loadCommandConfig(plugin, pack);
            HashMap<CommandProperty, ICommandFunction> map = pack.getMap();
            List<Command> commands = new ArrayList<>();
            for (CommandProperty property : map.keySet()) {
                if (property.isValid() && property.isEnable()) commands.add(new CommandWrapper(plugin, property, map.get(property)));
            }
            commandMap.registerAll(plugin.getDescription().getName(), commands);
            registeredPlugins.add(plugin.getName());
        }
    }

    @Deprecated
    public static void unregister(Plugin plugin) {
        //reflectCommandMap();
    }

    private static void loadCommandConfig(Plugin plugin, CommandPackage commandPackage) {
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "commands.yml"));

        for (Map.Entry<CommandProperty, ICommandFunction> entry : commandPackage.getMap().entrySet()) {
            if (yml.isConfigurationSection(entry.getKey().getName())) {
                ConfigurationSection cSection = yml.getConfigurationSection(entry.getKey().getName());
                entry.getKey().loadConfiguration(cSection);
                if (entry.getValue() instanceof IConfigurable && !entry.getKey().isShortcut()) {
                    ((IConfigurable) entry.getValue()).loadConfiguration(cSection);
                }
            }
        }

        for (Map.Entry<CommandProperty, ICommandFunction> entry : commandPackage.getMap().entrySet()) {
            ConfigurationSection cSection = yml.createSection(entry.getKey().getName());
            entry.getKey().saveConfiguration(cSection);
            if (entry.getValue() instanceof IConfigurable && !entry.getKey().isShortcut()) {
                ((IConfigurable) entry.getValue()).saveConfiguration(cSection);
            }
        }

        try {
            yml.save(new File(plugin.getDataFolder(), "commands.yml"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Can't save command configs", e);
        }
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
