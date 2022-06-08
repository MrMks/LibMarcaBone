package com.github.mrmks.mc.marcabone.cmd;

import com.github.mrmks.mc.marcabone.utils.AlternativeProperty;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

public class CommandProperty {

    private static final String NAME = "name";
    private static final String ALIASES = "aliases";
    private static final String DESCRIPTION = "description";
    private static final String USAGE = "usage";
    private static final String PERMISSION = "permission";
    private static final String PERMISSIONMESSAGE = "permissionMessage";

    private final boolean adapter;

    private final AlternativeProperty<String> name;
    private final AlternativeProperty<String[]> aliases;
    private final AlternativeProperty<String> desc;
    private final AlternativeProperty<String> usg;
    private final AlternativeProperty<String> perm;
    private final AlternativeProperty<String> permMsg;

    private boolean extra_perm;
    private boolean extra_desc;
    private boolean extra_usg;


    // normal command
    CommandProperty(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this(name, aliases, desc, usg, perm, permMsg, false);
    }

    // command adapter
    CommandProperty(PluginCommand cmd) {
        this(cmd.getLabel(), cmd.getAliases().toArray(new String[0]), cmd.getDescription(), cmd.getUsage(), cmd.getPermission(), cmd.getPermissionMessage(), true);
    }

    private CommandProperty(String name, String[] aliases, String desc, String usg, String perm, String permMsg, boolean adapter) {
        this.adapter = adapter;
        this.extra_perm = adapter && (perm == null || perm.isEmpty());
        this.extra_desc = adapter && (desc == null || desc.isEmpty());
        this.extra_usg = adapter && (usg == null || usg.isEmpty());

        this.name = new AlternativeProperty<>(name);
        this.aliases = new AlternativeProperty<>(aliases);
        this.desc = new AlternativeProperty<>(desc);
        this.usg = new AlternativeProperty<>(usg);
        this.perm = new AlternativeProperty<>(perm);
        this.permMsg = new AlternativeProperty<>(permMsg);
    }

    public void loadConfig(ConfigurationSection section) {
        if (adapter) {
            extra_desc = load0(section, DESCRIPTION, desc, extra_desc);
            extra_usg = load0(section, USAGE, usg, extra_usg);
            extra_perm = load0(section, PERMISSION, perm, extra_perm);
            load0(section, PERMISSIONMESSAGE, permMsg, extra_perm);
        } else {
            load0(section, NAME, name);
            load0(section, DESCRIPTION, desc);
            load0(section, USAGE, usg);
            load0(section, PERMISSION, perm);
            load0(section, PERMISSIONMESSAGE, permMsg);

            if (section.isList(ALIASES)) {
                List<String> lst = section.getStringList(ALIASES);
                aliases.setAlter(lst.toArray(new String[0]));
            } else {
                String[] lst = aliases.getValue();
                if (lst != null && lst.length > 0) {
                    section.set(ALIASES, Arrays.asList(lst));
                }
            }
        }
    }

    private boolean load0(ConfigurationSection sec, String k, AlternativeProperty<String> ap, boolean f) {
        if (f) {
            if (sec.isString(k)) {
                String v = sec.getString(k);
                f = !v.isEmpty();
                ap.setAlter(v);
            }
        }
        return f;
    }

    private void load0(ConfigurationSection sec, String k, AlternativeProperty<String> ap) {
        if (sec.isString(k)) {
            String v = sec.getString(k);
            ap.setAlter(v);
        } else {
            String v = ap.getValue();
            if (v != null) sec.set(k, v);
        }
    }

    public boolean hasExtraPermission() {
        return extra_perm;
    }

    public boolean hasExtraDescription() {
        return extra_desc;
    }

    public boolean hasExtraUsage() {
        return extra_usg;
    }

    public String getName() {
        return name.getValue();
    }

    public String[] getAliases() {
        String[] src = aliases.getValue();
        String[] copy = new String[src.length];
        System.arraycopy(src, 0, copy, 0, src.length);
        return copy;
    }

    public String getDescription() {
        return desc.getValue();
    }

    public String getUsage() {
        return usg.getValue();
    }

    public String getPermission() {
        return perm.getValue();
    }

    public String getPermissionMessage() {
        return permMsg.getValue();
    }
}
