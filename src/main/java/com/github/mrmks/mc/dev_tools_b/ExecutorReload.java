package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class ExecutorReload implements TabExecutor {

    private final LanguageAPI api;
    public ExecutorReload(LanguageAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        api.reload();
        commandSender.sendMessage("Build-in language file reloaded");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
