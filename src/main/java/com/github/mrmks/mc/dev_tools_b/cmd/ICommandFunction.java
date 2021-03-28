package com.github.mrmks.mc.dev_tools_b.cmd;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This interface response all command function, it is just like {@link org.bukkit.command.TabExecutor}
 * You can complete your command function by implement this command
 *
 * @see ICommandProperty ICommandProperty
 */
public interface ICommandFunction {

    List<String> onTabComplete(CommandSender sender, ICommandProperty property, List<String> label, List<String> args);

    boolean onCommand(CommandSender sender, ICommandProperty property, List<String> alias, List<String> args);
}
