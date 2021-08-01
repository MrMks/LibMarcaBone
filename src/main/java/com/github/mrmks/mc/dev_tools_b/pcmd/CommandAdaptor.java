package com.github.mrmks.mc.dev_tools_b.pcmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandAdaptor implements TabExecutor {

    private HashMap<String, IFunctionCommand> map;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        IFunctionCommand cmd = getChild(args);
        if (cmd != null) {
            String fLabel = label + " " + args[0];
            label = args[0];
            ArgSlice slice = new ArgSlice(args, 1);
            boolean flag;
            if (cmd.testPermissionSilence(sender))
                flag = cmd.execute(sender, label, fLabel, slice);
            else
                flag = cmd.noPermissionMessage(sender, label, fLabel, slice);
            if (flag)
                cmd.displayUsage(sender, label, fLabel, slice);
            return true;
        }
        else return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        IFunctionCommand cmd = getChild(args);
        if (cmd != null && cmd.testPermissionSilence(sender))
            return cmd.complete(sender, args[0], label + " " + args[0], new ArgSlice(args, 1));
        if (map != null && !map.isEmpty() && args.length > 0) {
            String str = args[args.length - 1];
            return map.entrySet().stream()
                    .filter(entry->entry.getKey().startsWith(str))
                    .filter(entry->entry.getValue().testPermissionSilence(sender))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public CommandAdaptor addChild(ISubCommand cmd) {
        return addChild(cmd, cmd.getName(), cmd.getAliases());
    }

    public CommandAdaptor addChild(IFunctionCommand cmd, String name, String... str) {
        if (cmd == null || name == null) return this;
        if (map == null) map = new HashMap<>();
        map.put(name, cmd);
        if (str != null) {
            for (String s : str) if (s != null && !s.isEmpty())
                map.putIfAbsent(s, cmd);
        }
        return this;
    }

    private IFunctionCommand getChild(String[] args) {
        if (map == null || args == null || args.length == 0)
            return null;
        return map.get(args[0]);
    }
}
