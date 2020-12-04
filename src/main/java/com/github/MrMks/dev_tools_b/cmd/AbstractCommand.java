package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCommand implements ICommandFunction {
    private final LanguageAPI lapi;

    protected AbstractCommand() {
        this(null);
    }
    protected AbstractCommand(LanguageAPI api) {
        this.lapi = api;
    }

    protected List<String> tabCompleteSelf(CommandSender sender, CommandProperty property, List<String> labels, List<String> args) {
        return Collections.emptyList();
    }

    protected boolean commandSelf(CommandSender sender, CommandProperty property, List<String> labels, List<String> args) {
        return false;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean testPermissionSilent(CommandSender sender, CommandProperty property) {
        if (!property.hasPermission()) return true;

        for (String p : property.getPermission().split(";")) {
            if (sender.hasPermission(p)) return true;
        }
        return false;
    }

    protected void displayPermissionMessage(CommandSender sender, CommandProperty property) {
        String permMsg = property.hasPermissionMessageKey() ? translate(sender, property.getPermissionMessageKey()) : property.getPermissionMessage();

        if (permMsg != null && permMsg.length() != 0) {
            for (String line : permMsg.replace("<permission>", property.getPermission()).split("\n")) {
                sender.sendMessage(line);
            }
        }
    }

    protected void displayHelpMessage(CommandSender sender, CommandProperty property, List<String> label) {
        String usage = property.hasUsageKey() ? translate(sender, property.getUsageKey()) : property.getUsage();
        String desc = property.hasDescriptionKey() ? translate(sender, property.getDescriptionKey()) : property.getDescription();

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

    protected String translate(CommandSender sender, String str) {
        return lapi == null ? str : lapi.getTranslation(sender instanceof Player ? ((Player) sender).getUniqueId() : null, str);
    }
}
