package com.github.mrmks.dev_tools_b;

import com.github.mrmks.dev_tools_b.cmd.*;
import com.github.mrmks.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

class CommandManager {

    private final Plugin plugin;
    public CommandManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register(LanguageAPI api) {
        CommandProperty property = new CommandProperty("reload", null,
                "dtb.perm.reload",
                "reload default translation files",
                "dtb.trans.cmd.reload.desc",
                "<command>",
                "dtb.trans.cmd.reload.usage",
                "You have no permission to do this",
                "dtb.trans.cmd.reload.permMsg");
        ICommandFunction funcReload = new AbstractCommand(api) {
            @Override
            public List<String> onTabComplete(CommandSender sender, CommandProperty property, List<String> label, List<String> args) {
                return tabCompleteSelf(sender, property, label, args);
            }

            @Override
            public boolean onCommand(CommandSender sender, CommandProperty property, List<String> alias, List<String> args) {
                LanguageAPI.DEFAULT.reload();
                sender.sendMessage(translate(sender, "dtb.trans.cmd.reload.success"));
                return true;
            }
        };

        SubCommand root = new SubCommand(api);
        root.register(new CommandPackage(property, funcReload));
        CommandPackage pack = new CommandPackage();
        pack.addCommand(new CommandProperty("dtb"), root);
        pack.addCommand(new CommandProperty("dtbr", "dtb.perm.reload", "reload default translation files", "<command>").markShortcut(), funcReload);
        CommandRegistry.register(plugin, pack);
    }

    @Deprecated
    public void unregister() {
        CommandRegistry.unregister(plugin);
    }
}