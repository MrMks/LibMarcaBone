package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

public class CommandRegistry {
    private static final Set<PluginCommand> set = new HashSet<>();
    private static CommandMap commandMap;
    private static boolean reflected = false;

    public static void register(PluginCommand pc, ICommand command) {
        if (pc != null && pc.isRegistered()) {
            if (set.add(pc)) {
                Executor e = new Executor(findRoot(command));
                pc.setTabCompleter(e);
                pc.setExecutor(e);
            }
        }
    }

    public static void register(PluginCommand pc, IChildCommand... commands) {
        if (pc != null && pc.isRegistered()) {
            SubCommand root = new SubCommand(pc.getName(), pc.getAliases(), SenderType.ANYONE, pc.getDescription(), pc.getUsage(), pc.getPermission(), pc.getPermissionMessage(), LanguageAPI.DEFAULT);
            if (set.add(pc)) {
                root.add(commands);
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

    /**
     * 实验性功能, 使用反射实现指令注册
     * 该方法可能不稳定，且存在依赖于具体实现的情况，请酌情使用
     */
    public static void register(Plugin plugin, String prefix, IChildCommand sub) {
        reflectCommandMap();
        if (commandMap != null) {
            CommandWrapper wrapper = new CommandWrapper(plugin, sub);
            commandMap.register(prefix, wrapper);
        }
    }

    private static void reflectCommandMap() {
        if (!reflected && commandMap == null) {
            try {
                Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
                field.setAccessible(true);
                Object obj = field.get(Bukkit.getPluginManager());
                if (obj instanceof CommandMap) {
                    commandMap = (CommandMap) obj;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {
                reflected = true;
            }
        }
    }

    private static ICommand findRoot(ICommand cmd) {
        while (cmd instanceof IChildCommand) {
            IChildCommand cCmd = (IChildCommand) cmd;
            if (cCmd.hasParent()) cmd = cCmd.getParent();
        }
        return cmd;
    }

    private static class Executor implements CommandExecutor, TabCompleter {
        private final ICommand command;
        Executor(ICommand command) {
            this.command = command;
        }
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            return this.command.execute(sender, label, new ArrayList<>(Arrays.asList(args)));
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            return this.command.tabComplete(sender, alias, new ArrayList<>(Arrays.asList(args)));
        }
    }
}
