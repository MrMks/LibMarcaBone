package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {
    /**
     * @return the name of this command
     */
    String getName();

    /**
     * @return aliases without name
     */
    List<String> getAliases();

    /**
     * @return a short description to show what dose this command do
     */
    String getDescription();

    /**
     * {@see 参数格式 https://minecraft-zh.gamepedia.com/%E5%91%BD%E4%BB%A4#.E5.91.BD.E4.BB.A4.E6.8C.87.E5.BC.95}
     * @return a string help users use this command
     */
    String getUsage();

    /**
     * @return the permission this command requires
     */
    String getPermission();

    /**
     * @return the message show to user when they try to use this command without the permission, or {@code null} to avoid the print
     */
    String getPermissionMessage();

    SenderType getSenderType();

    /**
     * @param sender the sender of the command
     * @param label the alias used to execute this command
     * @param args a list of args, can be null(simple consider as a empty list)
     * @return {@code true} if command is handled, {@code false} otherwise
     * @throws IllegalArgumentException if sender or label is null
     */
    boolean execute(CommandSender sender, String label, List<String> args);

    /**
     * @param sender the sender of the command
     * @param label the alias used to execute this command
     * @param args a list of args, can be null(simple consider as a empty list)
     * @return a list, can be null(in this case, show online players' name as the complete list)
     * @throws IllegalArgumentException if sender or label is null
     */
    List<String> tabComplete(CommandSender sender, String label, List<String> args);

    boolean testPermission(CommandSender sender);
    //String getHelpMessage(CommandSender sender, String label, List<String> args);
}
