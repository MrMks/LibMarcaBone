package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

public class SubCommand extends FunctionCommand implements IParentCommand {

    private final CommandParent parent = new CommandParent();

    public SubCommand(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(name, aliases, desc, usg, perm, permMsg);
    }

    public SubCommand(LanguageAPI api, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api, name, aliases, desc, usg, perm, permMsg);
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
    public CommandParent getParent() {
        return parent;
    }
}
