package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICmdFunc {
    List<String> onTabComplete(CommandSender sender, String label, List<String> args);

    /**
     *
     * @param sender the source of this command
     * @param label the alias used
     * @param args passed args
     * @return {@code true} if command was valid, false otherwise, return false will display help message;
     */
    boolean onExecute(CommandSender sender, String label, List<String> args);
}
