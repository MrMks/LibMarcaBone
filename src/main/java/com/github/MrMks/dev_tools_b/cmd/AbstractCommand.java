package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class AbstractCommand implements ICommand {

    @Override
    public boolean testPermission(CommandSender sender) {
        if (getPermission() == null || getPermission().isEmpty()) return true;
        if (getSenderType() == null || getSenderType() == SenderType.NONE) return false;
        return sender.hasPermission(getPermission()) && getSenderType().isType(sender);
    }


    protected void sendMessage(CommandSender sender, LanguageAPI lapi, String key, String def, Map<String, String> map) {
        if (def == null || def.isEmpty()) return;

        String msg;
        if (lapi != null) {
            if (sender instanceof Player) {
                msg = lapi.getTranslationWithTag(((Player) sender).getUniqueId(), key, map);
            } else {
                msg = lapi.getTranslationWithTag((String) null, key, map);
            }
            if (msg == null || msg.isEmpty()) msg = def;
        } else {
            msg = def;
        }
        String[] ary = msg.split("\n");
        for (String m : ary) {
            sender.sendMessage(m);
        }
    }

}
