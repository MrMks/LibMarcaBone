package com.github.mrmks.mc.marcabone.cmd;

import com.github.mrmks.mc.marcabone.lang.LanguageAPI;
import com.github.mrmks.mc.marcabone.utils.ArraySlice;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.List;

/**
 * This class used to implement your command functions.
 */
public abstract class FunctionCommand extends AbstractCommand implements ISubCommand {
    private final CommandProperty property;
    protected FunctionCommand(LanguageAPI api, String cfg, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        super(api, cfg);
        this.property = new CommandProperty(name, aliases, desc, usg, perm, permMsg);
    }

    protected FunctionCommand(LanguageAPI api, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this(api, null, name, aliases, desc, usg, perm, permMsg);
    }

    protected FunctionCommand(String cfg, String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this(null, cfg, name, aliases, desc, usg, perm, permMsg);
    }

    protected FunctionCommand(String name, String[] aliases, String desc, String usg, String perm, String permMsg) {
        this(null, null, name, aliases, desc, usg, perm, permMsg);
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        return testPermission(sender, getPermission());
    }

    @Override
    public boolean testPermission(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        return testPermission(sender, getPermission());
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        noPermissionMessage(sender, getPermission(), getPermissionMessage());
    }

    @Override
    public void displayUsage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        displayUsage(sender, getDescription(), getUsage(), label, fLabel);
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public String getUsage() {
        return property.getUsage();
    }

    @Override
    public String getPermission() {
        return property.getPermission();
    }

    @Override
    public String getPermissionMessage() {
        return property.getPermissionMessage();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(property.getAliases());
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        property.loadConfig(section);
    }
}
