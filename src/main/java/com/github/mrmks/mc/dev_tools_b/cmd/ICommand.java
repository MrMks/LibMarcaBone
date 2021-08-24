package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {
    boolean execute(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
    List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
}
