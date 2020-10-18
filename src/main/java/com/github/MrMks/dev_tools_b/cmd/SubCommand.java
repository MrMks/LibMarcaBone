package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class SubCommand extends AbstractCommand implements IChildCommand, IParentCommand {
    private IParentCommand parent;
    private final HashMap<String, IChildCommand> children = new HashMap<>();
    private final HashMap<String, IChildCommand> aliasMap = new HashMap<>();

    private final LanguageAPI lapi;

    public SubCommand(String name, List<String> alias, SenderType type, String desc, String usage, String perm, String permMsg, LanguageAPI lapi) {
        super(name, alias, type, desc, usage, perm, permMsg);

        this.lapi = lapi;
    }

    @Override
    public boolean execute(CommandSender commandSender, String alias, List<String> args) {
        if (!getAliases().contains(alias)) return false;

        if (testPermission(commandSender)) {
            if (args.size() > 0 && aliasMap.containsKey(args.get(0))) {
                return aliasMap.get(args.get(0)).execute(commandSender, args.remove(0), args);
            } else {
                sendMessage(
                        commandSender,
                        lapi,
                        "command.no_sub_command",
                        "Description: " + getDescription() + "\n" + "Usage: " + getUsage(),
                        ImmutableMap.of("desc",getDescription(), "usage", getUsage())
                );
            }
        } else {
            sendMessage(commandSender, lapi, "command.no_permission", getPermissionMessage(), Collections.emptyMap());
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender commandSender, String s, List<String> args) {
        if (getAliases().contains(s) && testPermission(commandSender)) {
            if (args.size() > 0 && aliasMap.containsKey(args.get(0))) {
                return aliasMap.get(args.get(0)).tabComplete(commandSender, args.remove(0), args);
            } else {
                return ImmutableList.copyOf(aliasMap.keySet().iterator());
            }
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        super.loadConfig(section);
        if (section != null && section.isConfigurationSection("children")) {
            ConfigurationSection cSection = section.getConfigurationSection("children");
            for (ICommand cmd : children.values()) {
                if (cSection.isConfigurationSection(cmd.getName())) {
                    cmd.loadConfig(cSection.getConfigurationSection(cmd.getName()));
                }
            }
        }
    }

    @Override
    public void saveConfig(ConfigurationSection section) {
        super.saveConfig(section);
        if (section != null) {
            section = section.createSection("children");
            for (ICommand cmd : children.values()) {
                cmd.saveConfig(section.createSection(cmd.getName()));
            }
        }
    }

    @Override
    public void setParent(IParentCommand parent) {
        this.parent = parent;
    }

    @Override
    public IParentCommand getParent() {
        return parent;
    }

    @Override
    public boolean hasParent() {
        return parent != null;
    }

    @Override
    public void add(IChildCommand... children) {
        for (IChildCommand child : children) {
            if (!child.hasParent() && !this.children.containsKey(child.getName())) {
                child.setParent(this);
                this.children.put(child.getName(), child);
                for (String alias : child.getAliases()) {
                    if (!aliasMap.containsKey(alias)) aliasMap.put(alias, child);
                }
            }
        }
    }

    @Override
    public IChildCommand getChild(String name) {
        return children.get(name);
    }

    @Override
    public Collection<IChildCommand> getChildren() {
        return ImmutableList.copyOf(children.values());
    }

    @Override
    public boolean hasChild(String name) {
        return children.containsKey(name);
    }
}
