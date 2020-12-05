package com.github.mrmks.dev_tools_b.cmd;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandProperty implements IConfigurable {
    private final String name;
    private List<String> alias;
    private List<String> activeAliases;
    private String permission;

    private String description;
    private String usage;
    private String permissionMessage;

    private String descriptionKey;
    private String usageKey;
    private String permissionMessageKey;

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
        } else {
            this.name = name;
            if (aliases == null || aliases.isEmpty() || aliases.stream().allMatch(s->s == null || s.isEmpty())) {
                alias = Collections.emptyList();
            } else {
                alias = aliases.stream().filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
            }
            this.activeAliases = new ArrayList<>(alias);
            this.permission = perm;
            this.description = desc == null ? "" : desc;
            this.descriptionKey = descKey;
            this.usage = usg == null ? "" : usg;
            this.usageKey = usgKey;
            this.permissionMessage = permMsg == null ? "" : permMsg;
            this.permissionMessageKey = permMsgKey;
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
        return description == null ? "" : description;
    }

    public String getUsage() {
        return usage == null ? "" : usage;
    }

    public boolean hasPermission() {
        return permission != null && !permission.isEmpty();
    }

    public String getPermission() {
        return permission;
    }

    public String getPermissionMessage() {
        return permissionMessage == null ? "" : permissionMessage;
    }

    public boolean hasDescriptionKey() {
        return descriptionKey != null && !descriptionKey.isEmpty();
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public boolean hasUsageKey() {
        return usageKey != null && !usageKey.isEmpty();
    }

    public String getUsageKey() {
        return usageKey;
    }

    public boolean hasPermissionMessageKey() {
        return permissionMessageKey != null && !permissionMessageKey.isEmpty();
    }

    public String getPermissionMessageKey() {
        return permissionMessageKey;
    }

    public void setRegistered(boolean flag) {
        if (registered != flag) {
            if (registered) {
                this.activeAliases = new ArrayList<>(alias);
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
    public void loadConfiguration(ConfigurationSection section) {
        if (section != null) {
            List<String> tmp = section.getStringList(ALIASES);
            if (tmp != null) {
                alias = tmp;
                activeAliases.clear();
                activeAliases.addAll(alias);
            }
            permission = getStringConfig(section, PERMISSION, permission);
            description = getStringConfig(section, DESCRIPTION, description);
            descriptionKey = getStringConfig(section, DESCRIPTION_KEY, descriptionKey);
            usage = getStringConfig(section, USAGE, usage);
            usageKey = getStringConfig(section, USAGE_KEY, usageKey);
            permissionMessage = getStringConfig(section, PERMISSION_MESSAGE, permissionMessage);
            permissionMessageKey = getStringConfig(section, PERMISSION_MESSAGE_KEY, permissionMessageKey);

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
            if (alias != null && !alias.isEmpty())
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
