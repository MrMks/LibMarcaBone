package com.github.mrmks.mc.dev_tools_b.cmd;

import java.util.List;

/**
 * This interface IS NOT completely designed, almost every valid check is completed in {@link CommandProperty}.
 * Please DO NOT use this interface by now.
 *
 * @see CommandProperty CommandProperty
 */
public interface ICommandProperty {

    boolean isEnable();

    String getName();
    List<String> getAlias();
    String getDescription();
    String getUsage();
    boolean hasPermission();
    String getPermission();
    String getPermissionMessage();

    boolean hasDescriptionKey();
    String getDescriptionKey();
    boolean hasUsageKey();
    String getUsageKey();
    boolean hasPermissionMessageKey();
    String getPermissionMessageKey();

    boolean hasShortcut();
    List<ICommandProperty> getShortcuts();
}
