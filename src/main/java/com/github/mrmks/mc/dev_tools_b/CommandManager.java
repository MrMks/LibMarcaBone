package com.github.mrmks.mc.dev_tools_b;

import com.github.mrmks.mc.dev_tools_b.cmd.*;
import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
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
                "dtb.trans.cmd.reload.permMsg")
                .addShortcut("dtbr");
        ICommandFunction funcReload = new AbstractCommand(api) {
            @Override
            protected boolean commandSelf(CommandSender sender, ICommandProperty property, List<String> alias, List<String> args) {
                api.reload();
                sender.sendMessage(translate(sender, "dtb.trans.cmd.reload.success", "Default language file has been reloaded"));
                return true;
            }
        };

        SubCommand root = new SubCommand(api);
        root.addCommand(property, funcReload);

        CommandPackage pack = new CommandPackage();
        pack.addCommand(new CommandProperty("dtb"), root);
        //pack.addCommand(new CommandProperty("dtbr", "dtb.perm.reload", "reload default translation files", new UsageBuild().append(REQUIRED,"command")).markShortcut(), funcReload);
        CommandRegistry.register(plugin, pack);
    }

    @Deprecated
    public void unregister() {
        CommandRegistry.unregister(plugin);
    }
}