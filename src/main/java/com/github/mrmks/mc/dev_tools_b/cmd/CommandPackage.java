package com.github.mrmks.mc.dev_tools_b.cmd;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public final class CommandPackage implements ICommandPackage {
    private final HashSet<String> knownName;
    private final HashMap<CommandProperty, ICommandFunction> map;
    //private final HashMap<ICommandFunction, List<CommandProperty>> revertMap;

    public CommandPackage() {
        knownName = new HashSet<>();
        map = new HashMap<>();
        //revertMap = new HashMap<>();
    }

    public CommandPackage(CommandProperty property, ICommandFunction command) {
        this();
        this.addCommand(property, command);
    }

    public void addCommand(CommandProperty property, ICommandFunction command){
        // return if register a registered property
        if (map.containsKey(property) || knownName.contains(property.getName())) return;

        //if (!revertMap.containsKey(command)) revertMap.put(command, new ArrayList<>());
        map.put(property, command);
        knownName.add(property.getName());
        //revertMap.get(command).add(property);
    }

    public Map<CommandProperty, ICommandFunction> getMap() {
        return map;
    }
}
