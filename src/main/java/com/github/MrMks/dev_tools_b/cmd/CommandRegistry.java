package com.github.MrMks.dev_tools_b.cmd;

import org.bukkit.command.*;

import java.util.*;

public class CommandRegistry {
    private static final Set<PluginCommand> set = new HashSet<>();
    public static void register(PluginCommand pc, SubCommand command) {
        if (pc != null && pc.isRegistered()) {
            if (set.add(pc)) {
                Executor e = new Executor(command);
                pc.setTabCompleter(e);
                pc.setExecutor(e);
            }
        }
    }

    public static void register(PluginCommand pc, SubCommand... commands) {
        if (pc != null && pc.isRegistered()) {
            SubCommand root = new SubCommand(pc.getName(), new HashSet<>(pc.getAliases()), pc.getDescription(), pc.getUsage(), SenderType.ANYONE, pc.getPermission());
            if (set.add(pc)) {
                root.addSubCommands(commands);
                Executor e = new Executor(root);
                pc.setTabCompleter(e);
                pc.setExecutor(e);
            }
        }
    }

    public static void unregister(PluginCommand pc) {
        if (pc != null && pc.isRegistered()) {
            if (set.remove(pc)) {
                pc.setExecutor(null);
                pc.setTabCompleter(null);
            }
        }
    }

    private static class Executor implements CommandExecutor, TabCompleter {
        private final SubCommand command;
        Executor(SubCommand command) {
            this.command = command;
        }
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            return this.command.onCommand(sender, label, new ArrayList<>(Arrays.asList(args)));
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return this.command.onTabComplete(sender, alias, new ArrayList<>(Arrays.asList(args)));
        }
    }
}
