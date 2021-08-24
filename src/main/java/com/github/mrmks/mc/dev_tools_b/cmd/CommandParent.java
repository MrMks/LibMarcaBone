package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class CommandParent {
    HashMap<String, ISubCommand> map;

    public void addChild(ISubCommand cmd) {
        if (cmd == null) return;
        addChild(cmd, cmd.getName(), cmd.getAliases());
    }

    private void addChild(ISubCommand cmd, String name, List<String> aliases) {
        if (cmd == null || name == null) return;
        if (map == null) map = new HashMap<>();
        map.put(name, cmd);
        if (aliases != null) {
            for (String alias : aliases) map.putIfAbsent(alias, cmd);
        }
    }

    protected ISubCommand getChild(ArraySlice<String> slice) {
        if (map == null || slice == null || slice.isEmpty() || map.isEmpty())
            return null;
        return map.get(slice.at(0));
    }

    public boolean execute(IParentCommand superCmd, CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        ISubCommand cmd = getChild(args);
        if (cmd != null) {
            fLabel.add(label);
            label = args.first();
            args = args.slice(1);
            if (cmd.testPermission(sender, label, fLabel, args)) {
                if (!cmd.execute(sender, label, fLabel, args)) {
                    cmd.displayUsage(sender, label, fLabel, args);
                }
            }
            else cmd.noPermissionMessage(sender, label, fLabel, args);
        } else {
            if (map != null && args != null && !map.isEmpty()) {
                List<String> lst = map.values().stream()
                        .filter(v->v.testPermission(sender))
                        .map(ISubCommand::getName)
                        .sorted()
                        .collect(Collectors.toList());
                if (args.size() == 0) {
                    superCmd.displayAvailableSubs(sender, label, fLabel, lst);
                } else {
                    superCmd.noSuchCommand(sender, label, fLabel, args, lst);
                }
            }
        }
        return true;
    }

    public List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        ISubCommand cmd = getChild(args);
        if (cmd != null) {
            fLabel.add(label);
            label = args.first();
            args = args.slice(1);
            if (cmd.testPermission(sender, label, fLabel, args))
                return cmd.complete(sender, label, fLabel, args);
        } if (map != null && !map.isEmpty() && args != null && args.size() <= 1) {
            String prefix = args.isEmpty() ? "" : args.first();
            return map.values().stream()
                    .filter(v -> v.testPermission(sender))
                    .map(ISubCommand::getName)
                    .filter(k -> k.startsWith(prefix))
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

}
