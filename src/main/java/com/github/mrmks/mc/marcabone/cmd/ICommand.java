package com.github.mrmks.mc.marcabone.cmd;

import com.github.mrmks.mc.marcabone.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {
    boolean execute(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
    List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args);
}
