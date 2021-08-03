package com.github.mrmks.mc.dev_tools_b.pcmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import java.util.List;

public abstract class CommandAdaptorCfgDirect extends FunctionCfgCommand implements TabExecutor {

    public CommandAdaptorCfgDirect(PluginCommand cmd, String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(cmd, configKey, name, aliases, desc, usg, perm, permMsg);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
