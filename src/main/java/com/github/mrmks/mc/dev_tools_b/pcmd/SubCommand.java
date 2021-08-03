package com.github.mrmks.mc.dev_tools_b.pcmd;

import java.util.Arrays;
import java.util.List;

public class SubCommand extends AbstractSubCommand {

    private final String name;
    private final String[] aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final String permissionMessage;

    public SubCommand(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.description = desc;
        this.usage = usg;
        this.permission = perm;
        this.permissionMessage = permMsg;
        this.name = name = CommandUtils.pureName(name);
        this.aliases = CommandUtils.pureAliases(name, aliases);
    }

    public SubCommand(String name) {
        this(name, (String[]) null);
    }

    public SubCommand(String name, String[] aliases) {
        this(name, aliases, null);
    }

    public SubCommand(String name, String permission) {
        this(name, (String[]) null, permission);
    }

    public SubCommand(String name, String[] aliases, String permission) {
        this(name, aliases, null, permission);
    }

    public SubCommand(String name, String usage, String permission) {
        this(name, null, usage, permission);
    }

    public SubCommand(String name, String[] aliases, String usage, String permission) {
        this(name, aliases, null, usage, permission);
    }

    public SubCommand(String name, String[] aliases, String desc, String usg, String permission) {
        this(name, aliases, null, usg, permission, null);
    }

    @Override
    public String getName() {
        return name;
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
    public List<String> getAliases() {
        return Arrays.asList(aliases);
    }
}
