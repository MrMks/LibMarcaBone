package com.github.mrmks.dev_tools_b.cmd;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommandFunction {

    List<String> onTabComplete(CommandSender sender, CommandProperty property, List<String> label, List<String> args);

    boolean onCommand(CommandSender sender, CommandProperty property, List<String> alias, List<String> args);
}
