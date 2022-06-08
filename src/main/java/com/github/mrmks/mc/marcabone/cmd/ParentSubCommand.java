package com.github.mrmks.mc.marcabone.cmd;

import com.github.mrmks.mc.marcabone.lang.LanguageAPI;

/**
 * This is used to implement a parent command.
 */
public class ParentSubCommand extends FunctionCommand implements IParentSubCommand {

    CommandParent parent;
    public ParentSubCommand(LanguageAPI api, String cfg, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api, cfg, name, aliases, desc, usg, perm, permMsg);
        this.parent = new CommandParent();
    }

    public ParentSubCommand(String name, String desc, String usg, String perm, String permMsg) {
        super(name, null, desc, usg, perm, permMsg);
        this.parent = new CommandParent();
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }

    public static ParentSubCommand auto(LanguageAPI api, String prefix, String node, String[] aliases) {
        node = "cmd.".concat(node);
        String cmdPrefix = prefix + '.' + node;

        String name = node;
        int index = node.lastIndexOf('.');
        if (index >= 0) {
            name = node.substring(index + 1);
        }

        return new ParentSubCommand(api, cmdPrefix, name, aliases,
                cmdPrefix.concat(".desc"),
                cmdPrefix.concat(".usg"),
                prefix + ".perm." + node,
                cmdPrefix.concat(".permMsg"));
    }

}
