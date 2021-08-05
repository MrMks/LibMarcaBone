package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.AlternativeProperty;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

import static com.github.mrmks.mc.dev_tools_b.cmd.CommandUtils.*;

public class CommandPropertyCfg implements ICommandProperty,IConfigurable {

    private final String configKey;
    private final AlternativeProperty<String> name;
    private final AlternativeProperty<String[]> aliases;
    private final AlternativeProperty<String> desc;
    private final AlternativeProperty<String> usg;
    private final AlternativeProperty<String> perm;
    private final AlternativeProperty<String> permMsg;
    private final boolean[] flag = new boolean[4];

    public CommandPropertyCfg(String key, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.configKey = key;
        this.name = new AlternativeProperty<>(name = pureName(name));
        this.aliases = new AlternativeProperty<>(pureAliases(name, aliases));
        this.desc = new AlternativeProperty<>(desc);
        this.usg = new AlternativeProperty<>(usg);
        this.perm = new AlternativeProperty<>(perm);
        this.permMsg = new AlternativeProperty<>(permMsg);
        Arrays.fill(flag, true);
    }

    public CommandPropertyCfg(PluginCommand cmd, String key, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.configKey = key;
        this.name = new AlternativeProperty<>(name = pureName(name));
        this.aliases = new AlternativeProperty<>(pureAliases(name, aliases));
        this.desc = new AlternativeProperty<>(selectString(cmd.getDescription(), desc, flag, 0));
        this.usg = new AlternativeProperty<>(selectString(cmd.getUsage(), usg, flag, 1));
        this.perm = new AlternativeProperty<>(selectString(cmd.getPermission(), perm, flag, 2));
        this.permMsg = new AlternativeProperty<>(selectString(cmd.getPermissionMessage(), permMsg, flag, 3));
    }

    @Override
    public String getName() {
        return name.getValue();
    }

    @Override
    public String getDescription() {
        return desc.getValue();
    }

    @Override
    public String getUsage() {
        return usg.getValue();
    }

    @Override
    public String getPermission() {
        return perm.getValue();
    }

    @Override
    public String getPermissionMessage() {
        return permMsg.getValue();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(aliases.getValue());
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

        if (flag[0]) loadConfig0(section, "description", desc);
        if (flag[1]) loadConfig0(section, "usage", usg);
        if (flag[2]) loadConfig0(section, "permission", perm);
        if (flag[3]) loadConfig0(section, "permissionMessage", permMsg);
    }

    private void loadConfig0(ConfigurationSection section, String key, AlternativeProperty<String> ap) {
        ap.setAlter(section.getString(key, ap.getValue()));
        section.set(key, ap.getValue());
    }
}
