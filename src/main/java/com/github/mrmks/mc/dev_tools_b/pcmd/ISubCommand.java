package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

public interface ISubCommand extends ICommand, ICommandProperty{
    @Override
    default boolean testPermission(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String permission = getPermission();
        if (permission == null || permission.isEmpty()) return true;
        for (String perm : permission.split(";")) {
            if (sender.hasPermission(perm)) return true;
        }
        return false;
    }

    @Override
    default void noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String perm = getPermission(), permMsg = getPermissionMessage();
        if (permMsg == null || permMsg.isEmpty()) return;
        if (perm == null || perm.isEmpty()) perm = "none";
        sender.sendMessage(permMsg.replace("<permission>", perm).split("\n"));
    }

    @Override
    default void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String desc = getDescription();
        if (desc != null && !desc.isEmpty()) sender.sendMessage(desc.split("\n"));
        String usg = getUsage();
        if (usg != null && !usg.isEmpty())
            sender.sendMessage(getUsage().replace("<command>", fLabel).split("\n"));
    }
}
