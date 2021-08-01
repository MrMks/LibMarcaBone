package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface IFunctionCommand {
    boolean execute(CommandSender sender, String label, String fLabel, ArraySlice<String> args);
    List<String> complete(CommandSender sender, String label, String fLabel, ArraySlice<String> args);
    boolean testPermissionSilence(CommandSender sender);
    boolean noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args);
    void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args);
}