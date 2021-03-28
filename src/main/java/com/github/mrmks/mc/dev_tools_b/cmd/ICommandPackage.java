package com.github.mrmks.mc.dev_tools_b.cmd;

import java.util.Map;

/**
 * If {@link SubCommand} can't match your need, you can write your own SubCommand,
 * but notice that you should implements this Interface so that my command system can identify your shortcut property correctly.
 *
 * @see IConfigurable IConfigurable
 */
public interface ICommandPackage {
    void addCommand(CommandProperty property, ICommandFunction function);
    Map<CommandProperty, ICommandFunction> getMap();
}
