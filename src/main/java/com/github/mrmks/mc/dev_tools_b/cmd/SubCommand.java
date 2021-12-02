package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;

/**
 * This is used to implement a parent command.
 */
public class SubCommand extends FunctionCommand implements IParentCommand {

    CommandParent parent;
    public SubCommand(LanguageAPI api, String cfg, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api, cfg, name, aliases, desc, usg, perm, permMsg);
        this.parent = new CommandParent();
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }

}
