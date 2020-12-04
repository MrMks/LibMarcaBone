package com.github.MrMks.dev_tools_b.cmd;


import java.util.HashMap;
import java.util.List;

public final class CommandPackage {
    private final HashMap<CommandProperty, ICommandFunction> map;

    public CommandPackage() {
        map = new HashMap<>();
    }

    public CommandPackage(CommandProperty property, ICommandFunction command) {
        map = new HashMap<>(1);
        this.addCommand(property, command);
    }

    public void addCommand(CommandProperty property, ICommandFunction command){
        map.put(property, command);
    }

    public void addCommand(List<CommandProperty> properties, ICommandFunction command){
        for (CommandProperty property : properties) {
            map.put(property, command);
        }
    }

    public HashMap<CommandProperty, ICommandFunction> getMap() {
        return map;
    }
}
