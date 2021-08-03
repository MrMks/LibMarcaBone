package com.github.mrmks.mc.dev_tools_b.pcmd;

import java.util.Arrays;
import java.util.List;

public abstract class FunctionCommand extends AbstractCommand {

    private final String name;
    private final String[] aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final String permissionMessage;

    public FunctionCommand(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.description = desc;
        this.usage = usg;
        this.permission = perm;
        this.permissionMessage = permMsg;
        this.name = name = CommandUtils.pureName(name);
        this.aliases = CommandUtils.pureAliases(name, aliases);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description == null ? "" : description;
    }

    @Override
    public String getUsage() {
        return usage == null ? "" : usage;
    }

    @Override
    public String getPermission() {
        return permission == null ? "" : permission;
    }

    @Override
    public String getPermissionMessage() {
        return permissionMessage == null ? "" : permissionMessage;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(aliases);
    }
}
