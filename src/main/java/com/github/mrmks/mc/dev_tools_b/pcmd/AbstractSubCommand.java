package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

abstract class AbstractSubCommand extends ParentCommand implements ISubCommand {
    @Override
    public boolean testPermission(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String permission = getPermission();
        if (permission == null || permission.isEmpty()) return true;
        for (String str : permission.split(";")) {
            if (sender.hasPermission(str))
                return true;
        }
        return false;
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String msg = getPermissionMessage();
        if (msg == null || msg.isEmpty())
            msg = ChatColor.RED +  "You have no permission to do this";
        for (String line : msg.replace("<permission>", getPermission()).split("\n")) {
            sender.sendMessage(line);
        }
    }

    @Override
    public void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String desc = getDescription();
        if (desc != null && !desc.isEmpty()) {
            for (String line : getDescription().split("\n")) {
                sender.sendMessage(line);
            }
        }
        String usage = getUsage();
        if (usage != null && !usage.isEmpty()) {
            for (String line : usage.replace("<command>", fLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }
    }
}
