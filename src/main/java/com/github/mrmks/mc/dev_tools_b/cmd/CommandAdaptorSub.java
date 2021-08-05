package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public class CommandAdaptorSub extends SubCommand implements TabExecutor {
    public CommandAdaptorSub(PluginCommand cmd, String name, String[] aliases) {
        super(name, aliases, cmd.getDescription(), cmd.getUsage(), cmd.getPermission(), cmd.getPermissionMessage());
    }

    public CommandAdaptorSub(LanguageAPI api, PluginCommand cmd, String name, String[] aliases) {
        super(api, name, aliases, cmd.getDescription(), cmd.getUsage(), cmd.getPermission(), cmd.getPermissionMessage());
    }
}
