package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements ISubCommand {

    private HashMap<String, IFunctionCommand> map;

    @Override
    public boolean execute(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        IFunctionCommand cmd = getChild(args);
        boolean flag;
        if (cmd != null) {
            String nLabel = args.at(0);
            String nfLabel = fLabel + " " + nLabel;
            if (cmd.testPermissionSilence(sender))
                flag = cmd.execute(sender, nLabel, nfLabel, args.slice(1));
            else
                flag = noPermissionMessage(sender, nLabel, nfLabel, args.slice(1));
            if (!flag)
                cmd.displayUsage(sender, nLabel, nfLabel, args.slice(1));
        } else {
            flag = executeSelf(sender, label, fLabel, args);
            if (!flag) {
                displayUsage(sender, label, fLabel, args);
            }
        }

        return true;
    }

    protected boolean executeSelf(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        IFunctionCommand cmd = getChild(args);
        if (cmd != null && cmd.testPermissionSilence(sender))
            return cmd.complete(sender, args.at(0), fLabel + " " + args.at(0), args.slice(1));
        if (map != null && !map.isEmpty() && !args.isEmpty()) {
            String str = args.at(args.size() - 1);
            return map.entrySet().stream()
                    .filter(entry->entry.getKey().startsWith(str))
                    .filter(entry->entry.getValue().testPermissionSilence(sender))
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean testPermissionSilence(CommandSender sender) {
        String permission = getPermission();
        if (permission == null || permission.isEmpty()) return true;
        for (String str : permission.split(";")) {
            if (sender.hasPermission(str))
                return true;
        }
        return false;
    }

    @Override
    public boolean noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String msg = getPermissionMessage();
        if (msg == null || msg.isEmpty())
            msg = ChatColor.RED +  "You have no permission to do this";
        for (String line : msg.replace("<permission>", getPermission()).split("\n")) {
            sender.sendMessage(line);
        }
        return true;
    }

    @Override
    public void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String desc = getDescription();
        if (desc != null && !desc.isEmpty()) {
            for (String line : getDescription().split("\n")) {
                sender.sendMessage(line);
            }
        }
        String usage = getUsage();
        if (usage != null && !usage.isEmpty()) {
            for (String line : usage.replace("<command>", fLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }
    }

    @Override
    public void addChild(ISubCommand cmd) {
        addChild(cmd, cmd.getName(), cmd.getAliases());
    }

    @Override
    public void addChild(IFunctionCommand cmd, String name, String... names) {
        if (name == null || cmd == null) return;
        if (map == null) map = new HashMap<>();
        map.put(name, cmd);
        if (names != null) {
            for (String str : names) if (str != null && !str.isEmpty())
                map.putIfAbsent(str, cmd);
        }
    }

    private IFunctionCommand getChild(ArraySlice<String> slice) {
        if (map == null || map.isEmpty() || slice.isEmpty()) {
            return null;
        }
        return map.get(slice.at(0));
    }
}
