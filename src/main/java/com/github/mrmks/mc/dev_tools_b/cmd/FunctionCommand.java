package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

import java.util.List;

import static com.github.mrmks.mc.dev_tools_b.cmd.CommandUtils.nonNull;

public abstract class FunctionCommand extends AbstractCommand {

    private final CommandProperty property;

    public FunctionCommand(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.property = new CommandProperty(name, aliases, desc, usg, perm, permMsg);
    }

    public FunctionCommand(LanguageAPI api, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api);
        this.property = new CommandProperty(name, aliases, desc, usg, perm, permMsg);
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public String getDescription() {
        return nonNull(property.getDescription(), "");
    }

    @Override
    public String getUsage() {
        return nonNull(property.getUsage(), "");
    }

    @Override
    public String getPermission() {
        return nonNull(property.getPermission(), "");
    }

    @Override
    public String getPermissionMessage() {
        return nonNull(property.getPermissionMessage(), "");
    }

    @Override
    public List<String> getAliases() {
        return property.getAliases();
    }
}
