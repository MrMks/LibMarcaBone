package com.github.MrMks.dev_tools_b.cmd;


import java.util.HashMap;
import java.util.Map;

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

    public void addCommands(CommandPackage pack) {
        for (Map.Entry<CommandProperty, ICommandFunction> entry : pack.getMap().entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
    }

    public HashMap<CommandProperty, ICommandFunction> getMap() {
        return map;
    }
}
