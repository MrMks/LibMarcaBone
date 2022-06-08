package com.github.mrmks.mc.marcabone.cmd;

import com.github.mrmks.mc.marcabone.lang.CombineLocaleHelper;
import com.github.mrmks.mc.marcabone.lang.LanguageAPI;
import com.github.mrmks.mc.marcabone.lang.LanguageHelper;
import com.github.mrmks.mc.marcabone.lang.LocaleHelper;
import com.github.mrmks.mc.marcabone.utils.ArraySlice;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

import static com.github.mrmks.mc.marcabone.utils.StringUtils.split;
import static com.github.mrmks.mc.marcabone.utils.StringUtils.append;

public abstract class AbstractCommand implements ICommand, IConfigurable {

    protected final LanguageAPI api;
    protected final String cfgKey;
    protected AbstractCommand(LanguageAPI api, String cfg) {
        this.api = api;
        this.cfgKey = cfg;
    }

    @Override
    public boolean hasConfigKey() {
        return cfgKey != null && !cfgKey.isEmpty();
    }

    @Override
    public String getConfigKey() {
        return cfgKey;
    }

    @Override
    public void loadConfig(ConfigurationSection section) {}

    protected boolean testPermission(CommandSender sender, String permission) {
        if (sender == null) return false;
        if (permission == null || permission.isEmpty()) return true;
        String[] perms = split(permission, ';', true);
        if (perms.length == 0) return true;
        for (String perm : perms) {
            if (sender.hasPermission(perm)) return true;
        }
        return false;
    }

    protected void noPermissionMessage(CommandSender sender, String permission, String message) {
        if (sender == null || message == null || message.isEmpty()) return;
        String msg = getHelper(sender).trans(message, "permission", permission == null ? "" : permission);
        if (!msg.isEmpty()) sender.sendMessage(split(msg, '\n', true));
    }

    protected void displayUsage(CommandSender sender, String desc, String usg, List<String> label) {
        if (sender == null) return;
        LanguageHelper helper = getHelper(sender);
        if (desc != null) {
            String msg = helper.trans(desc);
            if (!msg.isEmpty()) sender.sendMessage(split(msg, '\n', true));
        }
        if (usg != null) {
            String msg = helper.trans(usg, "command", append(label, ' '));
            if (!msg.isEmpty()) sender.sendMessage(split(msg, '\n', true));
        }
    }

    public void noSuchCommand(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args, List<String> av) {
        if (sender == null || label == null || av == null || av.isEmpty() || args == null || args.isEmpty()) return;
        StringBuilder bd = new StringBuilder("/");
        for (String s : fLabel) bd.append(s).append(' ');
        String fCmd = bd.append(label).append(' ').append(ChatColor.RED).append(args.first()).append(ChatColor.RESET).toString();
        LanguageHelper helper = getHelper(sender);
        sender.sendMessage(fCmd);
        sender.sendMessage(helper.trans("buildin.cmd.parent_no_such_child"));
        sender.sendMessage(helper.trans("buildin.cmd.parent_available_child"));
        sender.sendMessage(append(av, ", "));
    }

    public void displayAvailableSubs(CommandSender sender, String label, List<String> fLabel, List<String> subs) {
        if (sender == null || subs == null || subs.isEmpty()) return;
        sender.sendMessage(append(subs, ", "));
    }

    protected LanguageHelper getHelper(CommandSender sender) {
        LanguageAPI buildIn = LanguageAPI.getBuildIn();
        if (api == null && buildIn == null) return LocaleHelper.EMPTY;
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            if (api == null) return buildIn.helper(pl);
            else if (buildIn == null) return api.helper(pl);
            else return new CombineLocaleHelper(api.helper(pl), buildIn.helper(pl));
        }
        if (api == null) return buildIn.helper();
        else if (buildIn == null) return api.helper();
        else return new CombineLocaleHelper(api.helper(), buildIn.helper());
    }
}
