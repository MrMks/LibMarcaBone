package com.github.mrmks.mc.dev_tools_b.pcmd;

public class SubCommand extends AbstractCommand {

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

        // set the name;
        if (name == null) {
            if (aliases != null) {
                for (int i = 0; i < aliases.length; i++) {
                    name = aliases[i];
                    if (name != null && !name.isEmpty()) {
                        aliases[i] = null;
                        break;
                    }
                }
            }
            if (name == null) name = "null";
        }
        this.name = name;

        // set the aliases;
        if (aliases == null) {
            this.aliases = new String[0];
        } else {
            int i = 0;
            for (String str : aliases) {
                if (str != null && !str.isEmpty() && !str.equals(name))
                    i += 1;
            }
            if (i == 0) this.aliases = new String[0];
            else {
                String[] tmp = new String[i];
                int j = 0;
                for (String str : aliases) {
                    if (str != null && !str.isEmpty() && !str.equals(name))
                        tmp[j++] = str;
                }
                this.aliases = tmp;
            }
        }
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
    public String[] getAliases() {
        return aliases;
    }
}
