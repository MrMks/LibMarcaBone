package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.cmd.*;
import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;

class CommandManager {

    private final Plugin plugin;
    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register(LanguageAPI api, PluginCommand root) {
        CommandConfiguration cmdCfg = new CommandConfiguration(plugin);
        CommandAdaptor rootAdaptor = new CommandAdaptor();
        rootAdaptor.addChild(cmdCfg.loadCommand(new CommandReload(api)));
        root.setExecutor(rootAdaptor);
    }

    public void unregister(PluginCommand root) {
        root.setExecutor(null);
    }

    private static class CommandReload extends FunctionCfgCommand {

        private final LanguageAPI api;
        public CommandReload(LanguageAPI api) {
            super(api, "dtb.cmd.reload", "reload", new String[]{"r"}, "dtb.cmd.reload.desc", "dtb.cmd.reload.usg", "devTools.reload", "dtb.cmd.reload.permMsg");
            this.api = api;
        }

        @Override
        public boolean execute(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
            api.reload();
            return true;
        }

        @Override
        public List<String> complete(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
            return Collections.emptyList();
        }
    }
}