package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class SubCommand extends AbstractCommand implements IConfigurable {
    private final HashMap<CommandProperty, ICommandFunction> cmdMap = new HashMap<>();
    private final HashMap<String, CommandProperty> aliasMap = new HashMap<>();

    public SubCommand() {
        super();
    }

    public SubCommand(LanguageAPI api) {
        super(api);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, CommandProperty property, List<String> labels, List<String> args) {
        if (!testPermissionSilent(commandSender, property)) return Collections.emptyList();

        List<String> completion;
        if (args.size() > 1) {
            String nextLabel = args.get(0);
            if (aliasMap.containsKey(nextLabel) && aliasMap.get(nextLabel).isEnable()) {
                CommandProperty subProperty = aliasMap.get(nextLabel);
                ICommandFunction subFunction = cmdMap.get(subProperty);

                labels.add(args.remove(0));
                completion = subFunction.onTabComplete(commandSender, subProperty, labels, args);
            } else {
                completion = tabCompleteSelf(commandSender, property, labels, args);
            }
        } else {
            if (args.size() == 0 || args.get(0).length() == 0) completion = new ArrayList<>(aliasMap.keySet());
            else completion = aliasMap.entrySet().stream()
                    .filter(e-> e.getValue().isEnable() && e.getKey().startsWith(args.get(0)))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            completion.sort(String.CASE_INSENSITIVE_ORDER);
        }

        return completion;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, CommandProperty property, List<String> labels, List<String> args) {
        if (!testPermissionSilent(commandSender, property)) {
            displayPermissionMessage(commandSender, property);
            return true;
        }

        boolean suc;
        if (args.size() > 0) {
            String nextLabel = args.get(0);
            if (aliasMap.containsKey(nextLabel) && aliasMap.get(nextLabel).isEnable()) {
                CommandProperty subProperty = aliasMap.get(nextLabel);
                ICommandFunction subFunction = cmdMap.get(subProperty);

                List<String> subLabels = new ArrayList<>(labels);
                List<String> subArgs = new ArrayList<>(args);
                subLabels.add(subArgs.remove(0));
                return subFunction.onCommand(commandSender, subProperty, subLabels, subArgs);
            } else {
                suc = commandSelf(commandSender, property, labels, args);
            }
        } else {
            suc = commandSelf(commandSender, property, labels, args);
        }

        if (!suc) displayHelpMessage(commandSender, property, labels);

        return suc;
    }

    @Override
    public void loadDefaultConfiguration() {
        for (Map.Entry<CommandProperty, ICommandFunction> entry : cmdMap.entrySet()) {
            entry.getKey().loadDefaultConfiguration();
            if (entry.getValue() instanceof IConfigurable) {
                ((IConfigurable) entry.getValue()).loadDefaultConfiguration();
            }
        }
    }

    @Override
    public void loadConfiguration(ConfigurationSection section) {
        if (section != null && section.isConfigurationSection("children")) {
            ConfigurationSection cSection = section.getConfigurationSection("children");
            for (Map.Entry<CommandProperty, ICommandFunction> entry : cmdMap.entrySet()) {
                if (cSection.isConfigurationSection(entry.getKey().getName())) {
                    cSection = cSection.getConfigurationSection(entry.getKey().getName());

                    CommandProperty property = entry.getKey();
                    property.loadConfiguration(cSection);
                    if (!property.isShortcut() && entry.getValue() instanceof IConfigurable) {
                        ((IConfigurable) entry.getValue()).loadConfiguration(cSection);
                    }
                }
            }
        }
    }

    @Override
    public void saveConfiguration(ConfigurationSection section) {
        if (section != null) {
            section = section.createSection("children");
            for (Map.Entry<CommandProperty, ICommandFunction> entry : cmdMap.entrySet()) {
                ConfigurationSection cSection = section.createSection(entry.getKey().getName());

                CommandProperty property = entry.getKey();
                property.saveConfiguration(cSection);
                if (!property.isShortcut() && entry.getValue() instanceof IConfigurable) {
                    ((IConfigurable) entry.getValue()).saveConfiguration(cSection);
                }
            }
        }
    }

    public void register(CommandPackage pack) {
        Map<CommandProperty, ICommandFunction> map = pack.getMap();
        Iterator<CommandProperty> iterator = map.keySet().iterator();
        HashSet<String> set = new HashSet<>();
        while (iterator.hasNext()) {
            CommandProperty property = iterator.next();
            if (property.isValid() && !set.contains(property.getName())) {
                property.setRegistered(true);
                set.add(property.getName());
            }
            else {
                iterator.remove();
            }
        }
        for (CommandProperty property : map.keySet()) {
            ICommandFunction function = map.get(property);

            cmdMap.put(property, function);

            aliasMap.put(property.getName(), property);
            Iterator<String> aliasIterator = property.getAlias().iterator();
            while (aliasIterator.hasNext()) {
                String alias = aliasIterator.next();
                if (!set.contains(alias)) {
                    aliasMap.put(alias, property);
                } else aliasIterator.remove();
            }
        }
    }
}
