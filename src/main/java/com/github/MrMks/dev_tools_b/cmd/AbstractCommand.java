package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public abstract class AbstractCommand implements ICommand {
    protected final String name;
    protected String desc;
    protected String usg;
    protected String perm;
    protected String permMsg;
    protected List<String> alias;
    protected SenderType type;
    private boolean enabled = true;

    protected AbstractCommand(String name, List<String> alias, SenderType type, String desc, String usg, String perm, String permMsg){
        Validate.notEmpty(name, "Name of command can't be null");

        this.name = name;
        this.desc = isNullOr(desc, "There is no description for command " + name);
        this.alias = ImmutableList.copyOf(alias);
        this.type = type == null ? SenderType.NONE : type;
        this.usg = isNullOr(usg, "");
        this.perm = perm;
        this.permMsg = permMsg;
    }

    private <T> T isNullOr(T obj, T def) {
        if (obj == null) return def;
        else return obj;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getAliases() {
        return this.alias;
    }

    @Override
    public String getDescription() {
        return this.desc;
    }

    @Override
    public String getUsage() {
        return this.usg;
    }

    @Override
    public String getPermission() {
        return this.perm;
    }

    @Override
    public String getPermissionMessage() {
        return this.permMsg;
    }

    @Override
    public SenderType getSenderType() {
        return this.type;
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        if (getPermission() == null || getPermission().isEmpty()) return true;
        if (getSenderType() == null || getSenderType() == SenderType.NONE) return false;
        return sender.hasPermission(getPermission()) && getSenderType().isType(sender);
    }

    protected void sendMessage(CommandSender sender, LanguageAPI lapi, String key, String def, Map<String, String> map) {
        if (def == null || def.isEmpty()) return;

        String msg;
        if (lapi != null) {
            if (sender instanceof Player) {
                msg = lapi.getTranslationWithTag(((Player) sender).getUniqueId(), key, map);
            } else {
                msg = lapi.getTranslationWithTag((String) null, key, map);
            }
            if (msg == null || msg.isEmpty()) msg = def;
        } else {
            msg = def;
        }
        String[] ary = msg.split("\n");
        for (String m : ary) {
            sender.sendMessage(m);
        }
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        if (section != null) {
            enabled = section.getBoolean("enabled", enabled);
            alias = isNullOr(section.getStringList("aliases"), alias);
            desc = section.getString("description", desc);
            usg = section.getString("usage", usg);
            perm = section.getString("permission", perm);
            permMsg = section.getString("permissionMessage", permMsg);

            try {
                type = SenderType.valueOf(section.getString("senderType"));
            } catch (IllegalArgumentException ignored){
                type = SenderType.NONE;
            }
        }
    }

    @Override
    public void saveConfig(ConfigurationSection section) {
        if (section != null) {
            section.set("enabled", enabled);
            section.set("aliases", alias);
            section.set("description", desc);
            section.set("usage", usg);
            section.set("permission", perm);
            section.set("permissionMessage", permMsg);
            section.set("senderType", type.name());
        }
    }
}
