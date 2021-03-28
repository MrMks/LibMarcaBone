package com.github.mrmks.mc.dev_tools_b.cmd;

import java.util.Collections;
import java.util.List;

public class ShortcutProperty {

    private final String name;
    private final String desc;
    private final String descKey;

    public ShortcutProperty(String name) {
        this.name = name;
        this.desc = null;
        this.descKey = null;
    }

    public ShortcutProperty(String name, String desc, String descKey) {
        if (name == null) throw new IllegalArgumentException("name can't be null");

        this.name = name;
        this.desc = desc;
        this.descKey = descKey;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return desc;
    }

    public boolean hasDescriptionKey() {
        return descKey != null;
    }

    public String getDescriptionKey() {
        return descKey;
    }

    ICommandProperty wrap(CommandProperty property) {
        return new ShortcutPropertyWrapper(property);
    }

    class ShortcutPropertyWrapper implements ICommandProperty{
        private final CommandProperty property;
        ShortcutPropertyWrapper(CommandProperty property) {
            this.property = property;
        }

        @Override
        public boolean isEnable() {
            return true;
        }

        @Override
        public String getName() {
            return ShortcutProperty.this.getName();
        }

        @Override
        public List<String> getAlias() {
            return property.getAlias();
        }

        @Override
        public String getDescription() {
            String desc = ShortcutProperty.this.getDescription();
            return desc == null ? property.getDescription() : desc;
        }

        @Override
        public String getUsage() {
            return property.getUsage();
        }

        @Override
        public boolean hasPermission() {
            return property.hasPermission();
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
        public boolean hasDescriptionKey() {
            boolean has = ShortcutProperty.this.hasDescriptionKey();
            return has || property.hasDescriptionKey();
        }

        @Override
        public String getDescriptionKey() {
            boolean has = ShortcutProperty.this.hasDescriptionKey();
            return has ? ShortcutProperty.this.getDescriptionKey() : property.getDescriptionKey();
        }

        @Override
        public boolean hasUsageKey() {
            return property.hasUsageKey();
        }

        @Override
        public String getUsageKey() {
            return property.getUsageKey();
        }

        @Override
        public boolean hasPermissionMessageKey() {
            return property.hasPermissionMessageKey();
        }

        @Override
        public String getPermissionMessageKey() {
            return property.getPermissionMessageKey();
        }

        @Override
        public boolean hasShortcut() {
            return false;
        }

        @Override
        public List<ICommandProperty> getShortcuts() {
            return Collections.emptyList();
        }
    }
}
