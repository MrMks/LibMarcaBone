package com.github.mrmks.mc.dev_tools_b.cmd;

import java.util.Arrays;
import java.util.List;

import static com.github.mrmks.mc.dev_tools_b.cmd.CommandUtils.pureAliases;
import static com.github.mrmks.mc.dev_tools_b.cmd.CommandUtils.pureName;

public class CommandProperty implements ICommandProperty {

    private final String name;
    private final String[] aliases;
    private final String desc;
    private final String usg;
    private final String perm;
    private final String permMsg;

    public CommandProperty(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.name = name = pureName(name);
        this.aliases = pureAliases(name, aliases);
        this.desc = desc;
        this.usg = usg;
        this.perm = perm;
        this.permMsg = permMsg;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return desc;
    }

    @Override
    public String getUsage() {
        return usg;
    }

    @Override
    public String getPermission() {
        return perm;
    }

    @Override
    public String getPermissionMessage() {
        return permMsg;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(aliases);
    }
}
