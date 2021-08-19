package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.lang.LanguageHelper;
import com.github.mrmks.mc.dev_tools_b.lang.LocaleHelper;
import com.github.mrmks.mc.dev_tools_b.utils.AlternativeProperty;
import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import com.github.mrmks.mc.dev_tools_b.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class CommandAdaptorCfg extends CommandAdaptor implements IConfigurable {

    private LanguageAPI api = null;
    private final String configKey;
    private final AlternativeProperty<String> desc;
    private final AlternativeProperty<String> usg;
    private final AlternativeProperty<String> perm;
    private final AlternativeProperty<String> permMsg;
    private final boolean[] flag = new boolean[4];

    public CommandAdaptorCfg(PluginCommand cmd, String cfgKey) {
        this.configKey = cfgKey;
        this.desc = new AlternativeProperty<>(testString(cmd.getDescription(), flag, 0));
        this.usg = new AlternativeProperty<>(testString(cmd.getUsage(), flag, 1));
        this.perm = new AlternativeProperty<>(testString(cmd.getPermission(), flag, 2));
        this.permMsg = new AlternativeProperty<>(testString(cmd.getPermissionMessage(), flag, 3));
    }

    public CommandAdaptorCfg(LanguageAPI api, PluginCommand cmd, String cfgKey) {
        this(cmd, cfgKey);
        this.api = api;
    }

    @Override
    public String getConfigKey() {
        return configKey;
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        if (flag[0]) loadConfig0(section, "description", desc);
        if (flag[1]) loadConfig0(section, "usage", usg);
        if (flag[2]) loadConfig0(section, "permission", perm);
        if (flag[3]) loadConfig0(section, "permissionMessage", permMsg);
    }

    @Override
    public boolean testPermission(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        if (!flag[2]) return true;
        String permission = perm.getValue();
        if (permission == null || permission.isEmpty()) return true;
        for (String perm : permission.split(";")) {
            if (sender.hasPermission(perm)) return true;
        }
        return false;
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        String perm = this.perm.getValue(), permMsg = getHelper(sender).trans(this.permMsg.getValue());
        if (permMsg == null || permMsg.isEmpty()) return;
        if (perm == null || perm.isEmpty()) perm = "";
        sender.sendMessage(StringUtils.replace(permMsg, "permission", perm).split("\n"));
    }

    @Override
    public void displayUsage(CommandSender sender, String label, String fLabel, ArraySlice<String> args) {
        if (!flag[1]) return;
        LanguageHelper helper = getHelper(sender);
        String desc = helper.trans(this.desc.getValue());
        if (desc != null && !desc.isEmpty()) sender.sendMessage(desc.split("\n"));
        String usg = helper.trans(this.usg.getValue(), "command", fLabel);
        if (usg != null && !usg.isEmpty())
            sender.sendMessage(usg.split("\n"));
    }

    private LanguageHelper getHelper(CommandSender sender) {
        if (api == null) return LocaleHelper.EMPTY;
        if (sender instanceof Player) {
            return api.helper((Player) sender);
        }
        return api.helper();
    }

    private void loadConfig0(ConfigurationSection section, String key, AlternativeProperty<String> ap) {
        if (section.contains(key)) {
            ap.setAlter(section.getString(key, ap.getValue()));
        } else {
            section.set(key, ap.getValue());
        }
    }

    private String testString(String src, boolean[] a, int i) {
        a[i] = src == null || src.isEmpty();
        return src;
    }

}
