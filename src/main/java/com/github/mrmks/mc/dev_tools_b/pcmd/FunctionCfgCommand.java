package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public abstract class FunctionCfgCommand extends AbstractCommand implements IConfigurable {
    private final CommandPropertyCfg property;

    public FunctionCfgCommand(String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.property = new CommandPropertyCfg(configKey, name, aliases, desc, usg, perm, permMsg);
    }

    public FunctionCfgCommand(LanguageAPI api, String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api);
        this.property = new CommandPropertyCfg(configKey, name, aliases, desc, usg, perm, permMsg);
    }

    protected FunctionCfgCommand(PluginCommand cmd, String cfgKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.property = new CommandPropertyCfg(cmd, cfgKey, name, aliases, desc, usg, perm, permMsg);
    }

    protected FunctionCfgCommand(LanguageAPI api, PluginCommand cmd, String cfgKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api);
        this.property = new CommandPropertyCfg(cmd, cfgKey, name, aliases, desc, usg, perm, permMsg);
    }

    @Override
    public String getConfigKey() {
        return property.getConfigKey();
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        property.loadConfig(section);
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public String getUsage() {
        return property.getUsage();
    }

    @Override
    public String getPermission() {
        return property.getPermission();
    }

    @Override
    public String getPermissionMessage() {
        return property.getPermissionMessage();
    }

    @Override
    public List<String> getAliases() {
        return property.getAliases();
    }

}
