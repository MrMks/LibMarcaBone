package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * 内部使用的类，用于将bukkit的命令系统转接到此组成方式
 */
@Deprecated
public final class RootCommand extends Command implements PluginIdentifiableCommand {
    private final Plugin plugin;
    private final HashMap<String, ICommand> subs = new HashMap<>();
    protected RootCommand(Plugin plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args != null && args.length > 0) {
            List<String> listArgs = new ArrayList<>(Arrays.asList(args));
            String key = listArgs.remove(0);
            if (subs.containsKey(key)) {
                return subs.get(key).execute(sender, commandLabel, listArgs);
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args != null && args.length > 0) {
            List<String> listArgs = new ArrayList<>(Arrays.asList(args));
            String key = listArgs.remove(0);
            if (subs.containsKey(key)) {
                return subs.get(key).tabComplete(sender, alias, listArgs);
            }
        }
        return Collections.emptyList();
    }

    public void add(IChildCommand... subs) {
        for (IChildCommand sub : subs) {
            this.subs.put(sub.getName(), sub);
        }
    }

    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }
}
