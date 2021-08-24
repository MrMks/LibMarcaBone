package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ISubCommand extends ICommand {
    String getName();
    String getDescription();
    String getUsage();
    String getPermission();
    String getPermissionMessage();
    List<String> getAliases();

    void displayUsage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
    boolean testPermission(CommandSender sender);
    boolean testPermission(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
    void noPermissionMessage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);

}
