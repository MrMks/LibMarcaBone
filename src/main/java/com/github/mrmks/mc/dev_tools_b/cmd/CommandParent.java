package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandParent {
    protected HashMap<String, ICommand> map;

    public void addChild(ISubCommand cmd) {
        if (cmd == null) return;
        addChild(cmd, cmd.getName(), cmd.getAliases().toArray(new String[0]));
    }

    private void addChild(ICommand cmd, String name, String... aliases){
        if (cmd == null || name == null) return;
        if (map == null) map = new HashMap<>();
        map.put(name, cmd);
        if (aliases != null) {
            for (String alias : aliases) map.putIfAbsent(alias, cmd);
        }
    }

    protected ICommand getChild(ArraySlice<String> slice) {
        if (map == null || slice == null || slice.isEmpty() || map.isEmpty())
            return null;
        return map.get(slice.at(0));
    }

    public boolean execute(IParentCommand superCmd, CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        ICommand cmd = getChild(args);
        if (cmd != null) {
            fLabel = fLabel + " " + args.first();
            label = args.first();
            args = args.slice(1);
            if (cmd.testPermission(sender, label, fLabel, args)) {
                if (!cmd.execute(sender, label, fLabel, args)) {
                    cmd.displayUsage(sender, label, fLabel, args);
                }
            }
            else cmd.noPermissionMessage(sender, label, fLabel, args);
        } else {
            if (!superCmd.executeSelf(sender, label, fLabel, args)) {
                superCmd.displayUsage(sender, label, fLabel, args);
            }
        }

        return true;
    }

    public List<String> complete(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        ICommand cmd = getChild(args);
        if (cmd != null) {
            label = args.first();
            fLabel += " " + label;
            args = args.slice(1);
            if (cmd.testPermission(sender, label, fLabel, args))
                return cmd.complete(sender, label, fLabel, args);
        } if (map != null && !map.isEmpty() && args != null && args.size() <= 1) {
            String prefix = args.isEmpty() ? "" : args.first(), fStr = fLabel + " ";
            ArraySlice<String> sArgs = args.isEmpty() ? args : args.slice(1);
            return map.entrySet().stream()
                    .filter(e -> e.getKey().startsWith(prefix))
                    .filter(e -> e.getValue().testPermission(sender, e.getKey(), fStr + e.getKey(), sArgs))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
