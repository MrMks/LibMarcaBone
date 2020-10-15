package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.DevToolsB;
import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import com.github.MrMks.dev_tools_b.lang.TagI18N;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class SubCommand {
    private final String name;
    private final Set<String> aliases;
    private final String description;
    private final String usage;
    private final String permission;
    private final SenderType availableSender;
    private final ICmdFunc func;
    private final LanguageAPI lapi;
    private final HashMap<String, SubCommand> children = new HashMap<>();
    private SubCommand parent;
    public SubCommand(String name, String description, String usageMessage, SenderType type, String permission) {
        this(name, description, usageMessage, type, permission, null, null);
    }

    public SubCommand(String name, String description, String usageMessage, SenderType type, String permission, LanguageAPI lapi){
        this(name, description, usageMessage, type, permission, null, lapi);
    }

    public SubCommand(String name, String description, String usageMessage, SenderType type, String permission, ICmdFunc func){
        this(name, description, usageMessage, type, permission, func, null);
    }

    public SubCommand(String name, String description, String usageMessage, SenderType type, String permission, ICmdFunc func, LanguageAPI lapi) {
        this(name, Collections.emptySet(), description, usageMessage, type, permission, func, lapi);
    }

    public SubCommand(String name, Set<String> alias, String description, String usage, SenderType type, String permission){
        this(name, alias, description, usage, type, permission, null, null);
    }

    public SubCommand(String name, Set<String> alias, String description, String usage, SenderType type, String permission, ICmdFunc func){
        this(name, alias, description, usage, type, permission,func,null);
    }

    public SubCommand(String name, Set<String> alias, String description, String usage, SenderType type, String permission, LanguageAPI lapi){
        this(name, alias, description, usage, type, permission,null, lapi);
    }

    public SubCommand(String name, Set<String> alias, String description, String usage, SenderType type, String permission, ICmdFunc func, LanguageAPI lapi){
        this.name = name;
        this.aliases = Collections.unmodifiableSet(alias);
        this.description = description;
        this.usage = usage;
        this.availableSender = type;
        this.permission = permission;
        this.func = func;
        this.lapi = lapi == null ? LanguageAPI.load("DevToolsB") : lapi;
    }

    public boolean hasParent(){
        return parent != null;
    }

    public void setParent(SubCommand command){
        parent = command;
    }

    public void addSubCommands(SubCommand... subs){
        for (SubCommand cmd : subs){
            addSubCommand(cmd);
        }
    }

    private void addSubCommand(SubCommand subCommand){
        if (!subCommand.hasParent()) {
            subCommand.setParent(this);
            children.put(subCommand.getName(), subCommand);
            subCommand.getAliases().forEach(key->children.putIfAbsent(key, subCommand));
        }
    }

    /**
     * May be useless?
     * @param key key of sub command
     */
    public void removeSubCommand(String key){
        children.remove(key);
    }

    public boolean onCommand(CommandSender commandSender, String alias, List<String> args) {
        if (!(name.equalsIgnoreCase(alias) || aliases.contains(alias))) return false;
        if (checkPermission(commandSender)) {
            boolean flag;
            if (args.size() > 0 && children.containsKey(args.get(0))) {
                flag = children.get(args.get(0)).onCommand(commandSender, args.remove(0), args);
            } else {
                flag = executeCommand(commandSender, alias, args);
            }
            if (!flag) displayUsage(commandSender);
        } else {
            sendMessage(commandSender, "command.no_permission",Collections.emptyMap(),"§cYou have no permission to use this command");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender commandSender, String s, List<String> args) {
        if ((name.equalsIgnoreCase(s) || aliases.contains(s)) && checkPermission(commandSender)) {
            if (args.size() > 0 && children.containsKey(args.get(0))) {
                return children.get(args.get(0)).onTabComplete(commandSender, args.remove(0), args);
            } else {
                return tabComplete(commandSender, s, args);
            }
        } else {
            return Collections.emptyList();
        }
    }

    private boolean checkPermission(CommandSender sender){
        switch (availableSender) {
            case ANYONE:
                return sender.hasPermission(permission);
            case PLAYER:
                return sender instanceof Player && sender.hasPermission(permission);
            case CONSOLE:
                return !(sender instanceof Player);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void displayUsage(CommandSender sender){
        StringBuilder usg = new StringBuilder();
        SubCommand pa = this;
        while (pa.hasParent()) {
            usg.insert(0, " ").insert(0, pa.getName());
            pa = pa.parent;
        }
        usg.insert(0, " ").insert(0, pa.getName()).append(usage);
        usg.insert(0, "§2/");
        if (!description.isEmpty()) usg.append("  §6-- ").append(getDescription());
        sender.sendMessage(usg.toString());
        if (!aliases.isEmpty()) {
            StringBuilder aliasList = new StringBuilder();
            for (String alias : aliases) {
                aliasList.append(alias).append(",");
            }
            String msg = aliasList.toString();
            msg = msg.substring(0,msg.length() - 1);
            usg = new StringBuilder("§7You can use these aliases for command \"" + name + "\": ").append(msg);
            sendMessage(sender, "command.aliases_list", ImmutableMap.of("name", name, "aliases", msg),usg.toString());
        }
    }

    private boolean executeCommand(CommandSender sender, String label, List<String> args){
        if (func != null) {
            return func.onExecute(sender, label, args);
        } else {
            return false;
        }
    }

    private List<String> tabComplete(CommandSender sender, String alias, List<String> args){
        if (func != null) return func.onTabComplete(sender, alias, args);
        else {
            List<String> list = new ArrayList<>();
            if (args.size() == 1) {
                String arg = args.get(0);
                for (SubCommand command : children.values()) {
                    if (command.checkPermission(sender)) {
                        String name = command.getName();
                        if (arg.equalsIgnoreCase("") || name.startsWith(arg)) {
                            list.add(command.getName());
                        }
                    }
                }
            }
            return list;
        }
    }

    private void sendMessage(CommandSender sender, String key, Map<String, String> map, String def) {
        sender.sendMessage(getTransMsg(sender, key, map, def));
    }

    private String getTransMsg(CommandSender sender, String key, Map<String, String> map, String def) {
        String msg = def;
        if (lapi != null) {
            if (sender instanceof Player) {
                UUID uuid = ((Player) sender).getUniqueId();
                msg = lapi.getTranslationWithTag(uuid, key, map);
            } else {
                msg = lapi.getTranslationWithTag("__empty__", key, map);
            }
            if (msg.equals(key)) msg = def;
        }
        return msg;
    }
}
