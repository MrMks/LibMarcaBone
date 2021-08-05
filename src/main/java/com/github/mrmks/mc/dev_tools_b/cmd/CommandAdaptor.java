package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandAdaptor implements TabExecutor, IParentCommand {

    private final CommandParent parent = new CommandParent();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return execute(sender, label, label, new ArgSlice(args));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return complete(sender, label, label, new ArgSlice(args));
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }

    @Override
    public boolean testPermission(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        return true;
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {}

    @Override
    public void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {}
}
