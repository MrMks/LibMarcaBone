package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CommandWrapper extends Command implements PluginIdentifiableCommand {
    private final ICommand command;
    private final Plugin plugin;
    protected CommandWrapper(Plugin plugin, ICommand subCommand) {
        super(subCommand.getName(), subCommand.getDescription(), subCommand.getUsage(), subCommand.getAliases());
        setPermission(subCommand.getPermission());
        setPermissionMessage(subCommand.getPermissionMessage());
        this.command = subCommand;
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return command.execute(sender, commandLabel, new ArrayList<>(Arrays.asList(args)));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return command.tabComplete(sender, alias, new ArrayList<>(Arrays.asList(args)));
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
