package com.github.mrmks.mc.dev_tools_b.pcmd;

import com.github.mrmks.mc.dev_tools_b.utils.AlternativeProperty;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public class SubCfgCommand extends AbstractCommand implements IConfigurable {

    private final String configKey;
    private final AlternativeProperty<String> name;
    private final AlternativeProperty<String[]> aliases;
    private final AlternativeProperty<String> description;
    private final AlternativeProperty<String> usage;
    private final AlternativeProperty<String> permission;
    private final AlternativeProperty<String> permissionMessage;

    public SubCfgCommand(String configKey, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this.configKey = configKey;
        this.description = new AlternativeProperty<>(desc);
        this.usage = new AlternativeProperty<>(usg);
        this.permission = new AlternativeProperty<>(perm);
        this.permissionMessage = new AlternativeProperty<>(permMsg);

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
        this.name = new AlternativeProperty<>(name);

        // set the aliases;
        if (aliases == null) {
            aliases = new String[0];
        } else {
            int i = 0;
            for (String str : aliases) {
                if (str != null && !str.isEmpty() && !str.equals(name))
                    i += 1;
            }
            if (i == 0) aliases = new String[0];
            else {
                String[] tmp = new String[i];
                int j = 0;
                for (String str : aliases) {
                    if (str != null && !str.isEmpty() && !str.equals(name))
                        tmp[j++] = str;
                }
                aliases = tmp;
            }
        }
        this.aliases = new AlternativeProperty<>(aliases);
    }

    @Override
    public String getConfigKey() {
        return configKey;
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        section = section.getConfigurationSection(configKey);
        loadConfig0(section, "description", description);
        loadConfig0(section, "usage", usage);
        loadConfig0(section, "permission", permission);
        loadConfig0(section, "permissionMessage", permissionMessage);
        loadConfig0(section, "name", name);
        List<String> list = section.getStringList("aliases");
        list.removeIf(str -> str == null || str.isEmpty());
        String[] tmp = list.toArray(new String[0]);
        aliases.setAlter(tmp);
        section.set("aliases", Arrays.asList(aliases.getValue()));
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
    public String[] getAliases() {
        return aliases.getValue();
    }
}
