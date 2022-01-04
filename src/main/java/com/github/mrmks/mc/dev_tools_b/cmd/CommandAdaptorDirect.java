package com.github.mrmks.mc.dev_tools_b.cmd;

import com.github.mrmks.mc.dev_tools_b.lang.LanguageAPI;
import com.github.mrmks.mc.dev_tools_b.lang.LanguageHelper;
import com.github.mrmks.mc.dev_tools_b.utils.ArraySlice;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is used to implement a command without any sub commands.
 */
public abstract class CommandAdaptorDirect extends AbstractCommand implements ISubCommand, TabExecutor {

    private final CommandProperty property;
    private final PluginCommand cmd;
    protected CommandAdaptorDirect(LanguageAPI api, String cfg, PluginCommand cmd) {
        super(api, cfg);
        this.property = new CommandProperty(cmd);
        this.cmd = cmd;
    }

    @Override
    public boolean testPermission(CommandSender sender) {
        return testPermission(sender, getPermission());
    }

    @Override
    public boolean testPermission(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        return !property.hasExtraPermission() && fLabel.size() == 1 || testPermission(sender, getPermission());
    }

    @Override
    public void noPermissionMessage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        noPermissionMessage(sender, getPermissionMessage(), getPermission());
    }

    @Override
    public void displayUsage(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        displayUsage(sender, getDescription(), getUsage(), fLabel);
    }

    @Override
    public String getName() {
        return property.getName();
    }

    @Override
    public String getDescription() {
        return property.getDescription();
    }

    @Override
    public String getUsage() {
        return property.getUsage();
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
    public List<String> getAliases() {
        return Arrays.asList(property.getAliases());
    }

    @Override
    public void loadConfig(ConfigurationSection section) {
        property.loadConfig(section);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return execute(commandSender, s, new ArrayList<>(), new ArraySlice<>(strings));
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return complete(commandSender, s, new ArrayList<>(), new ArraySlice<>(strings));
    }

    public void registerHelpTopic(HelpMap helpMap) {
        helpMap.addTopic(new HelpTopicImpl());
    }

    public class HelpTopicImpl extends HelpTopic {

        HelpTopicImpl() {
            String _name = CommandAdaptorDirect.this.getName();
            if (_name.startsWith("/")) {
                this.name = _name;
            } else {
                this.name = "/" + _name;
            }

            String _desc = CommandAdaptorDirect.this.getDescription();
            int i = _desc.indexOf(10);
            if (i > 1) {
                this.shortText = _desc.substring(0, i - 1);
            } else {
                this.shortText = _desc;
            }

        }

        @Override
        public String getFullText(CommandSender forWho) {
            LanguageHelper helper = getHelper(forWho);
            String _desc = CommandAdaptorDirect.this.getDescription();
            String _usg = CommandAdaptorDirect.this.getUsage();

            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GOLD);
            sb.append(helper.trans("buildin.cmd.help_topic_description")).append(':').append(' ');
            sb.append(ChatColor.WHITE);
            sb.append(helper.trans(_desc));
            sb.append("\n");
            sb.append(ChatColor.GOLD);
            sb.append(helper.trans("buildin.cmd.help_topic_usage")).append(':').append(' ');
            sb.append(ChatColor.WHITE);
            sb.append(helper.trans(_usg, "command", this.name.substring(1)));
            if (CommandAdaptorDirect.this.getAliases().size() > 0) {
                sb.append("\n");
                sb.append(ChatColor.GOLD);
                sb.append(helper.trans("buildin.cmd.help_topic_aliases")).append(':').append(' ');
                sb.append(ChatColor.WHITE);
                sb.append(ChatColor.WHITE).append(StringUtils.join(CommandAdaptorDirect.this.getAliases(), ", "));
            }

            return sb.toString();
        }

        @Override
        public boolean canSee(CommandSender sender) {
            if (sender == null) return false;
            else if (!cmd.isRegistered()) return false;
            else if (sender instanceof ConsoleCommandSender) return true;
            else return amendedPermission == null ? testPermission(sender) : testPermission(sender, amendedPermission);
        }
    }
}
