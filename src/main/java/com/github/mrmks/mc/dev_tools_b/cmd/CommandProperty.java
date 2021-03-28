package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.utils.AlternativeProperty;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandProperty implements IConfigurable {
    private final String name;
    private final AlternativeProperty<List<String>> alias;
    private List<String> activeAliases;
    private final AlternativeProperty<String> permission;

    private final AlternativeProperty<String> description;
    private final AlternativeProperty<String> usage;
    private final AlternativeProperty<String> permissionMessage;

    private final AlternativeProperty<String> descriptionKey;
    private final AlternativeProperty<String> usageKey;
    private final AlternativeProperty<String> permissionMessageKey;

    private boolean registered = false;
    private boolean isShortcut = false;

    private boolean enable = true;

    public CommandProperty(String name) {
        this(name, "", "", "");
    }

    public CommandProperty(String name, String perm, String desc, String usg) {
        this(name, null, perm, desc, usg);
    }

    public CommandProperty(String name, List<String> aliases, String permission, String desc, String usage){
        this(name, aliases, permission, desc, usage, "You have no permission to do this");
    }

    public CommandProperty(String name, List<String> aliases, String permission, String desc, String usg, String permMsg) {
        this(name, aliases, permission, desc, null, usg, null, permMsg, null);
    }

    public CommandProperty(String name, List<String> aliases, String perm, String desc, String descKey, String usg, String usgKey, String permMsg, String permMsgKey) {
        if (name == null || name.isEmpty()) {
            this.name = null;
            this.alias = null;
            this.permission = null;
            this.description = null;
            this.usage = null;
            this.permissionMessage = null;
            this.descriptionKey = null;
            this.usageKey = null;
            this.permissionMessageKey = null;
        } else {
            this.name = name;
            if (aliases == null || aliases.isEmpty() || aliases.stream().allMatch(s->s == null || s.isEmpty())) {
                alias = new AlternativeProperty<>(Collections.emptyList());
            } else {
                alias = new AlternativeProperty<>(aliases.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList()));
            }
            this.activeAliases = new ArrayList<>(alias.getValue());
            this.permission = new AlternativeProperty<>(perm);
            this.description = new AlternativeProperty<>(desc == null ? "" : desc);
            this.descriptionKey = new AlternativeProperty<>(descKey);
            this.usage = new AlternativeProperty<>(usg == null ? "" : usg);
            this.usageKey = new AlternativeProperty<>(usgKey);
            this.permissionMessage = new AlternativeProperty<>(permMsg == null ? "" : permMsg);
            this.permissionMessageKey = new AlternativeProperty<>(permMsgKey);
        }
    }

    public boolean isValid() {
        return !registered && name != null;
    }

    public boolean isEnable() {
        return enable;
    }

    public String getName() {
        return name;
    }

    public List<String> getAlias() {
        return activeAliases;
    }

    public String getDescription() {
        return description.getValue() == null ? "" : description.getValue();
    }

    public String getUsage() {
        return usage.getValue() == null ? "" : usage.getValue();
    }

    public boolean hasPermission() {
        return permission.getValue() != null && !permission.getValue().isEmpty();
    }

    public String getPermission() {
        return permission.getValue();
    }

    public String getPermissionMessage() {
        return permissionMessage.getValue() == null ? "" : permissionMessage.getValue();
    }

    public boolean hasDescriptionKey() {
        return descriptionKey.getValue() != null && !descriptionKey.getValue().isEmpty();
    }

    public String getDescriptionKey() {
        return descriptionKey.getValue();
    }

    public boolean hasUsageKey() {
        return usageKey.getValue() != null && !usageKey.getValue().isEmpty();
    }

    public String getUsageKey() {
        return usageKey.getValue();
    }

    public boolean hasPermissionMessageKey() {
        return permissionMessageKey.getValue() != null && !permissionMessageKey.getValue().isEmpty();
    }

    public String getPermissionMessageKey() {
        return permissionMessageKey.getValue();
    }

    public void setRegistered(boolean flag) {
        if (registered != flag) {
            if (registered) {
                this.activeAliases = new ArrayList<>(alias.getValue());
            }
            this.registered = flag;
        }
    }

    private final static String ALIASES = "aliases";
    private final static String PERMISSION = "permission";
    private final static String DESCRIPTION = "description";
    private final static String DESCRIPTION_KEY = "descriptionKey";
    private final static String USAGE = "usage";
    private final static String USAGE_KEY = "usageKey";
    private final static String PERMISSION_MESSAGE = "permissionMessage";
    private final static String PERMISSION_MESSAGE_KEY = "permissionMessageKey";
    private final static String ENABLE = "enable";

    @Override
    public void loadDefaultConfiguration() {
        alias.removeAlter();
        permission.removeAlter();
        description.removeAlter();
        descriptionKey.removeAlter();
        usage.removeAlter();
        usageKey.removeAlter();
        permissionMessage.removeAlter();
        permissionMessageKey.removeAlter();

        enable = true;
    }

    @Override
    public void loadConfiguration(ConfigurationSection section) {
        if (section != null) {
            List<String> tmp = section.getStringList(ALIASES);
            if (tmp != null) {
                alias.setAlter(tmp);
                activeAliases.clear();
                activeAliases.addAll(alias.getValue());
            }
            permission.setAlter(getStringConfig(section, PERMISSION, permission.getValue()));
            description.setAlter(getStringConfig(section, DESCRIPTION, description.getValue()));
            descriptionKey.setAlter(getStringConfig(section, DESCRIPTION_KEY, descriptionKey.getValue()));
            usage.setAlter(getStringConfig(section, USAGE, usage.getValue()));
            usageKey.setAlter(getStringConfig(section, USAGE_KEY, usageKey.getValue()));
            permissionMessage.setAlter(getStringConfig(section, PERMISSION_MESSAGE, permissionMessage.getValue()));
            permissionMessageKey.setAlter(getStringConfig(section, PERMISSION_MESSAGE_KEY, permissionMessageKey.getValue()));

            enable = section.getBoolean(ENABLE, enable);
        }
    }

    private String getStringConfig(ConfigurationSection section, String key, String def) {
        return (section == null
                || key == null
                || !section.isString(key)
                || section.getString(key).isEmpty()) ? def : section.getString(key);
    }

    @Override
    public void saveConfiguration(ConfigurationSection section) {
        if (section != null) {
            if (alias.getValue() != null && !alias.getValue().isEmpty())
                section.set(ALIASES, alias);

            if (hasPermission())
                section.set(PERMISSION, getPermission());

            if (!getDescription().isEmpty())
                section.set(DESCRIPTION, getDescription());
            if (hasDescriptionKey())
                section.set(DESCRIPTION_KEY, getDescriptionKey());
            if (!getUsage().isEmpty())
                section.set(USAGE, getUsage());
            if (hasUsageKey())
                section.set(USAGE_KEY, getUsageKey());
            if (!getPermissionMessage().isEmpty())
                section.set(PERMISSION_MESSAGE, getPermissionMessage());
            if (hasPermissionMessageKey())
                section.set(PERMISSION_MESSAGE_KEY, getPermissionMessageKey());

            section.set(ENABLE, enable);
        }
    }

    public CommandProperty markShortcut() {
        isShortcut = true;
        return this;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isShortcut() {
        return isShortcut;
    }
}
