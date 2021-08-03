package com.github.mrmks.mc.dev_tools_b.pcmd;

import org.bukkit.command.PluginCommand;

public class SubCfgCommand extends FunctionCfgCommand implements IConfigurable, IParentCommand {

    private final CommandParent parent = new CommandParent();

    public SubCfgCommand(String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(configKey, name, aliases, desc, usg, perm, permMsg);
    }

    protected SubCfgCommand(PluginCommand cmd, String key, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(cmd, key, name, aliases, desc, usg, perm, permMsg);
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }
}
