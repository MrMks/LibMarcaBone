package com.github.MrMks.dev_tools_b;

import com.github.MrMks.dev_tools_b.cmd.CommandRegistry;
import com.github.MrMks.dev_tools_b.cmd.ICmdFunc;
import com.github.MrMks.dev_tools_b.cmd.SenderType;
import com.github.MrMks.dev_tools_b.cmd.SubCommand;
import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.Collections;
import java.util.List;

public class CommandManager {
    public static void register(PluginCommand pm) {
        SubCommand reloadDTBLapi = new SubCommand("reload", "reload default command translation file", "", SenderType.ANYONE, "devTools.reload", new ICmdFunc() {
            @Override
            public List<String> onTabComplete(CommandSender sender, String label, List<String> args) {
                return Collections.emptyList();
            }

            @Override
            public boolean onExecute(CommandSender sender, String label, List<String> args) {
                LanguageAPI.unload("DevToolsB");
                LanguageAPI.load("DevToolsB");
                sender.sendMessage("§2Default command translate has been reloaded");
                return true;
            }
        });
        CommandRegistry.register(pm, new SubCommand[]{reloadDTBLapi});
    }

    public static void unregister(PluginCommand pm) {
        CommandRegistry.unregister(pm);
    }
}