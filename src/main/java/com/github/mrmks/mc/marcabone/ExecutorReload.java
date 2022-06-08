package com.github.mrmks.mc.marcabone;

import com.github.mrmks.mc.marcabone.lang.LanguageAPI;
import com.github.mrmks.mc.marcabone.lang.LanguageHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ExecutorReload implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        LanguageAPI api = LanguageAPI.getBuildIn();
        if (api != null) {
            api.reload();
            LanguageHelper helper = commandSender instanceof Player ? api.helper((Player) commandSender) : api.helper();
            commandSender.sendMessage(helper.trans("buildin.reload"));
        } else {
            commandSender.sendMessage("Build-in language file reloaded");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
