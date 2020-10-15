package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class FuncCommand extends AbstractCommand implements IChildCommand {
    private final String name, description, usage, permission, permissionMessage;
    private final List<String> aliases;
    private final SenderType availableSender;

    private final LanguageAPI lapi;
    private final ICmdFunc func;

    public FuncCommand(String name, List<String> alias, SenderType type, String desc, String usage, String perm, String permMsg, LanguageAPI lapi, ICmdFunc func) {
        this.name = name;
        this.aliases = alias;
        this.availableSender = type;
        this.description = desc;
        this.usage = usage;
        this.permission = perm;
        this.permissionMessage = permMsg;

        this.lapi = lapi;
        this.func = func;
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
    public boolean execute(CommandSender sender, String label, List<String> args) {
        if (!(getName().equalsIgnoreCase(label) || getAliases().contains(label))) return false;

        if (testPermission(sender)) {
            if (func != null) {
                return func.onExecute(sender, label, args);
            }
        } else {
            sendMessage(sender, lapi, "command.no_permission", getPermissionMessage(), Collections.emptyMap());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, List<String> args) {
        if (!(getName().equalsIgnoreCase(label) || getAliases().contains(label))) return ImmutableList.of();

        if (testPermission(sender)) {
            if (func != null) {
                return func.onTabComplete(sender, label, args);
            }
        } else {
            sendMessage(sender, lapi, "command.no_permission", getPermissionMessage(), Collections.emptyMap());
        }
        return ImmutableList.of();
    }

    private IParentCommand parentCommand;
    @Override
    public void setParent(IParentCommand parent) {
        this.parentCommand = parent;
    }

    @Override
    public IParentCommand getParent() {
        return parentCommand;
    }

    @Override
    public boolean hasParent() {
        return parentCommand != null && parentCommand.getChild(name) == this;
    }
}
