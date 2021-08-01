package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * This class defines some method used in {@link SubCommand}, they are useful for me.
 * You can extends this class and override {@link #tabCompleteSelf(CommandSender, ICommandProperty, List, List)}
 * and {@link #commandSelf(CommandSender, ICommandProperty, List, List)} to complete your command function.
 */
public abstract class AbstractCommand implements ICommandFunction {
    private final LanguageAPI lapi;

    protected AbstractCommand() {
        this(null);
    }
    protected AbstractCommand(LanguageAPI api) {
        this.lapi = api;
    }

    /**
     * For example here is a command str "a b c d e", and this command is related to c,
     * then the property is the ICommandProperty with name "c",
     * the label list is ["a", "b", "c"], the args list is ["d", "e"];
     *
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, ICommandProperty property, List<String> label, List<String> args) {
        if (!testPermissionSilent(sender, property)) return Collections.emptyList();
        else return tabCompleteSelf(sender, property, label, args);
    }

    protected List<String> tabCompleteSelf(CommandSender sender, ICommandProperty property, List<String> labels, List<String> args) {
        return Collections.emptyList();
    }

    /**
     * @see #onTabComplete(CommandSender, ICommandProperty, List, List)
     */
    @Override
    public boolean onCommand(CommandSender sender, ICommandProperty property, List<String> alias, List<String> args) {
        if (!testPermissionSilent(sender, property)) {
            displayPermissionMessage(sender, property);
            return true;
        } else return commandSelf(sender, property, alias, args);
    }

    protected boolean commandSelf(CommandSender sender, ICommandProperty property, List<String> labels, List<String> args) {
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean testPermissionSilent(CommandSender sender, ICommandProperty property) {
        if (!property.hasPermission()) return true;

        for (String p : property.getPermission().split(";")) {
            if (sender.hasPermission(p)) return true;
        }
        return false;
    }

    protected void displayPermissionMessage(CommandSender sender, ICommandProperty property) {
        String permMsg = property.hasPermissionMessageKey() ?
                translate(sender, property.getPermissionMessageKey(), property.getPermissionMessage()) : property.getPermissionMessage();

        if (permMsg != null && permMsg.length() != 0) {
            for (String line : permMsg.replace("<permission>", property.getPermission()).split("\n")) {
                sender.sendMessage(line);
            }
        }
    }

    protected void displayHelpMessage(CommandSender sender, ICommandProperty property, List<String> label) {
        String usage = property.hasUsageKey() ? translate(sender, property.getUsageKey(), property.getUsage()) : property.getUsage();
        String desc = property.hasDescriptionKey() ? translate(sender, property.getDescriptionKey(), property.getDescription()) : property.getDescription();

        StringBuilder builder = new StringBuilder();
        for (String l : label) builder.append(l).append(' ');
        if (builder.length() > 0) builder.deleteCharAt(builder.length() - 1);
        String fullLabel = builder.toString();

        if (usage != null && usage.length() > 0) {
            for (String line : usage.replace("<command>", fullLabel).split("\n")) {
                sender.sendMessage(line);
            }
        }

        if (desc != null && desc.length() > 0) {
            for (String line : desc.split("\n")) {
                sender.sendMessage(line);
            }
        }
    }

    protected void displayArgumentMessage(CommandSender sender, String key, String def, List<String> args) {
        String res = translate(sender, key, def);
        for (int i = 0; i < args.size(); i++) {
            res = res.replace("<arg" + (i + 1) + ">", args.get(i));
        }
        for (String line : res.split("\n")) sender.sendMessage(line);
    }

    protected String translate(CommandSender sender, String key, String def) {
        UUID uuid = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        return lapi == null ? def :
                lapi.hasKey(uuid, key) ? lapi.getTranslation(uuid, key) : def;
    }
}