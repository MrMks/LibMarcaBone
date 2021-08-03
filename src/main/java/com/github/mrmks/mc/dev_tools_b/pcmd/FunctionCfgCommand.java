package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.AlternativeProperty;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

import static com.github.mrmks.mc.dev_tools_b.pcmd.CommandUtils.*;

public abstract class FunctionCfgCommand extends AbstractCommand implements IConfigurable {
    private final String configKey;
    private final AlternativeProperty<String> name;
    private final AlternativeProperty<String[]> aliases;
    private final AlternativeProperty<String> description;
    private final AlternativeProperty<String> usage;
    private final AlternativeProperty<String> permission;
    private final AlternativeProperty<String> permissionMessage;
    private final boolean[] loadFlag = new boolean[4];

    public FunctionCfgCommand(String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.configKey = configKey;
        this.description = new AlternativeProperty<>(desc);
        this.usage = new AlternativeProperty<>(usg);
        this.permission = new AlternativeProperty<>(perm);
        this.permissionMessage = new AlternativeProperty<>(permMsg);
        this.name = new AlternativeProperty<>(name = pureName(name));
        this.aliases = new AlternativeProperty<>(pureAliases(name, aliases));
        Arrays.fill(loadFlag, true);
    }

    protected FunctionCfgCommand(PluginCommand cmd, String cfgKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.configKey = cfgKey;
        this.name = new AlternativeProperty<>(name = pureName(name));
        this.aliases = new AlternativeProperty<>(pureAliases(name, aliases));
        this.description = new AlternativeProperty<>(selectString(cmd.getDescription(), desc, loadFlag, 0));
        this.usage = new AlternativeProperty<>(selectString(cmd.getUsage(), usg, loadFlag, 1));
        this.permission = new AlternativeProperty<>(selectString(cmd.getPermission(), perm, loadFlag, 2));
        this.permissionMessage = new AlternativeProperty<>(selectString(cmd.getPermissionMessage(), permMsg, loadFlag, 3));
    }

    @Override
    public String getConfigKey() {
        return configKey;
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        loadConfig0(section, "name", name);
        List<String> list = section.getStringList("aliases");
        list.removeIf(str -> str == null || str.isEmpty());
        String[] tmp = list.toArray(new String[0]);
        aliases.setAlter(tmp);
        section.set("aliases", Arrays.asList(aliases.getValue()));

        if (loadFlag[0]) loadConfig0(section, "description", description);
        if (loadFlag[1]) loadConfig0(section, "usage", usage);
        if (loadFlag[2]) loadConfig0(section, "permission", permission);
        if (loadFlag[3]) loadConfig0(section, "permissionMessage", permissionMessage);
    }

    private void loadConfig0(ConfigurationSection section, String key, AlternativeProperty<String> ap) {
        ap.setAlter(section.getString(key, ap.getValue()));
        section.set(key, ap.getValue());
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public String getDescription() {
        return description.getValue();
    }

    @Override
    public String getUsage() {
        return usage.getValue();
    }

    @Override
    public String getPermission() {
        return permission.getValue();
    }

    @Override
    public String getPermissionMessage() {
        return permissionMessage.getValue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(aliases.getValue());
    }

}
