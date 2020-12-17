package com.github.mrmks.dev_tools_b.cmd;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CommandPackage {
    private final HashMap<CommandProperty, ICommandFunction> map;
    private final HashMap<ICommandFunction, List<CommandProperty>> revertMap;

    public CommandPackage() {
        map = new HashMap<>();
        revertMap = new HashMap<>();
    }

    public CommandPackage(CommandProperty property, ICommandFunction command) {
        this();
        this.addCommand(property, command);
    }

    public void addCommand(CommandProperty property, ICommandFunction command){
        // return if register a registered property
        if (map.containsKey(property)) return;
        // return if register a registered command with non-shortcut property and an non-shortcut property has been registered with the command
        if (!property.isShortcut() && revertMap.containsKey(command)) {
            List<CommandProperty> list = revertMap.get(command);
            for (CommandProperty oldProperty : list) {
                if (!oldProperty.isShortcut()) return;
            }
        }

        if (!revertMap.containsKey(command)) revertMap.put(command, new ArrayList<>());
        map.put(property, command);
        revertMap.get(command).add(property);
    }

    public void addCommands(CommandPackage pack) {
        for (Map.Entry<CommandProperty, ICommandFunction> entry : pack.getMap().entrySet()) {
            addCommand(entry.getKey(), entry.getValue());
        }
    }

    public HashMap<CommandProperty, ICommandFunction> getMap() {
        return map;
    }
}
