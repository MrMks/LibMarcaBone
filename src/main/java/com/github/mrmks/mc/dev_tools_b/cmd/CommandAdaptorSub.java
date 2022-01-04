package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.PluginCommand;

/**
 * Since {@link CommandAdaptorDirect} is not for commands with sub commands, this class will take that duty.
 */
public class CommandAdaptorSub extends CommandAdaptorDirect implements IParentSubCommand {

    CommandParent parent;
    public CommandAdaptorSub(LanguageAPI api, String cfg, PluginCommand cmd) {
        super(api, cfg, cmd);
        this.parent = new CommandParent();
    }

    public CommandAdaptorSub(LanguageAPI api, PluginCommand cmd) {
        this(api, null, cmd);
    }

    public CommandAdaptorSub(String cfg, PluginCommand cmd) {
        this(null, cfg, cmd);
    }

    public CommandAdaptorSub(PluginCommand cmd) {
        this(null, null, cmd);
    }

    @Override
    public CommandParent getParent() {
        return parent;
    }
}
