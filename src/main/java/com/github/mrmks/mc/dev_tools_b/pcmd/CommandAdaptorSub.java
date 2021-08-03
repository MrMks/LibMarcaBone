package com.github.mrmks.mc.dev_tools_b.pcmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandAdaptorSub extends SubCommand implements TabExecutor {
    public CommandAdaptorSub(PluginCommand cmd, String name, String[] aliases) {
        super(name, aliases, cmd.getDescription(), cmd.getUsage(), cmd.getPermission(), cmd.getPermissionMessage());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return execute(commandSender, s, s, new ArgSlice(strings));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return complete(commandSender, s, s, new ArgSlice(strings));
    }
}
