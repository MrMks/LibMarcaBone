package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * constructors are deprecated
 * waiting for rebuild
 */
public class SubCommand extends AbstractCommand implements IChildCommand, IParentCommand {
    private final String name;
    private final List<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final String permissionMessage;

    private final SenderType availableSender;

    private IParentCommand parent;
    private final HashMap<String, IChildCommand> children = new HashMap<>();

    private final LanguageAPI lapi;

    public SubCommand(String name, List<String> alias, SenderType type, String desc, String usage, String perm, String permMsg, LanguageAPI lapi) {
        this.name = name;
        this.aliases = new ArrayList<>(alias);
        this.availableSender = type;
        this.description = desc;
        this.usage = usage;
        this.permission = perm;
        this.permissionMessage = permMsg;

        this.lapi = lapi;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public String getPermissionMessage() {
        return permissionMessage;
    }

    @Override
    public SenderType getSenderType() {
        return availableSender;
    }

    @Override
    public boolean execute(CommandSender commandSender, String alias, List<String> args) {
        if (!(name.equalsIgnoreCase(alias) || aliases.contains(alias))) return false;

        if (testPermission(commandSender)) {
            if (args.size() > 0 && children.containsKey(args.get(0))) {
                return children.get(args.get(0)).execute(commandSender, args.remove(0), args);
            } else {
                sendMessage(
                        commandSender,
                        lapi,
                        "command.no_sub_command",
                        "Description: " + getDescription() + "\n" + "Usage: " + getUsage(),
                        ImmutableMap.of("desc",getDescription(), "usage", getUsage())
                );
            }
        } else {
            sendMessage(commandSender, lapi, "command.no_permission", getPermissionMessage(), Collections.emptyMap());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String s, List<String> args) {
        if ((name.equalsIgnoreCase(s) || aliases.contains(s)) && testPermission(commandSender)) {
            if (args.size() > 0 && children.containsKey(args.get(0))) {
                return children.get(args.get(0)).tabComplete(commandSender, args.remove(0), args);
            } else {
                return ImmutableList.copyOf(children.keySet().iterator());
            }
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void setParent(IParentCommand parent) {
        this.parent = parent;
    }

    @Override
    public IParentCommand getParent() {
        return parent;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public void add(IChildCommand... children) {
        for (IChildCommand child : children) {
            if (!this.children.containsKey(child.getName())) this.children.put(child.getName(), child);
        }
    }

    @Override
    public IChildCommand getChild(String name) {
        return children.get(name);
    }

    @Override
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }
}
