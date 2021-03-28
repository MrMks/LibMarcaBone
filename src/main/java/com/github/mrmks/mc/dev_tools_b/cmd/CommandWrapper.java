package com.github.mrmks.mc.dev_tools_b.cmd;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class CommandWrapper extends Command implements PluginIdentifiableCommand {
    private final ICommandFunction function;
    private final CommandProperty property;
    private final Plugin plugin;
    protected CommandWrapper(Plugin plugin, CommandProperty property, ICommandFunction function) {
        super(property.getName(), property.getDescription(), property.getUsage(), property.getAlias());

        setPermission(property.getPermission());
        setPermissionMessage(property.getPermissionMessage());

        property.setRegistered(true);

        this.plugin = plugin;
        this.property = property;
        this.function = function;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) throws IllegalArgumentException, CommandException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(commandLabel, "Label cannot be null");

        if (!plugin.isEnabled()) {
            throw new CommandException("Cannot execute command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName() + " - plugin is disabled.");
        }

        if (!testPermission(sender)) {
            return true;
        }

        List<String> list = Arrays.asList(args);
        try {
            return function.onCommand(sender, property, new ArrayList<>(Collections.singletonList(commandLabel)), new ArrayList<>(list));
        } catch (Throwable ex) {
            throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + plugin.getDescription().getFullName(), ex);
        }

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException, CommandException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        List<String> list;
        try {
            list = function.onTabComplete(sender, property, new ArrayList<>(Collections.singletonList(alias)), new ArrayList<>(Arrays.asList(args)));
        } catch (Throwable ex) {
            StringBuilder builder = new StringBuilder("Unhandled exception during completion for command '/")
                    .append(alias);
            for (String arg : args) builder.append(' ').append(arg);
            builder.append("' in plugin ").append(plugin.getDescription().getFullName());
            throw new CommandException(builder.toString(), ex);
        }

        return list == null ? super.tabComplete(sender, alias, args) : list;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
