package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

public abstract class CommandAdaptorCfgDirect extends FunctionCfgCommand implements TabExecutor {

    public CommandAdaptorCfgDirect(PluginCommand cmd, String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(cmd, configKey, name, aliases, desc, usg, perm, permMsg);
    }

    public CommandAdaptorCfgDirect(LanguageAPI api, PluginCommand cmd, String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api, cmd, configKey, name, aliases, desc, usg, perm, permMsg);
    }
}
