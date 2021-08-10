package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.lang.LanguageHelper;
import com.github.mrmks.mc.dev_tools_b.lang.LocaleHelper;
import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractCommand implements ISubCommand {

    private final LanguageAPI api;
    AbstractCommand(LanguageAPI api) {
        this.api = api;
    }

    AbstractCommand() {
        this.api = null;
    }

    @Override
    public boolean testPermission(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String permission = getPermission();
        if (permission == null || permission.isEmpty()) return true;
        for (String perm : permission.split(";")) {
            if (sender.hasPermission(perm)) return true;
        }
        return false;
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String perm = getPermission(), permMsg = getHelper(sender).trans(getPermissionMessage());
        if (permMsg == null || permMsg.isEmpty()) return;
        if (perm == null || perm.isEmpty()) perm = "none";
        sender.sendMessage(permMsg.replace("<permission>", perm).split("\n"));
    }

    @Override
    public void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        LanguageHelper helper = getHelper(sender);
        String desc = helper.trans(getDescription());
        if (desc != null && !desc.isEmpty()) sender.sendMessage(desc.split("\n"));
        String usg = helper.trans(getUsage());
        if (usg != null && !usg.isEmpty())
            sender.sendMessage(usg.replace("<command>", fLabel).split("\n"));
    }

    protected LanguageHelper getHelper(CommandSender sender) {
        if (api == null) return LocaleHelper.EMPTY;
        if (sender instanceof Player) {
            return api.helper((Player) sender);
        }
        return api.helper();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return execute(commandSender, s, s, new ArgSlice(strings));
    }

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return complete(commandSender, s, s, new ArgSlice(strings));
    }

}
