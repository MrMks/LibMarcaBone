# DevToolsB
My bukkit code lib

Usage: 
There are three module available now: LanguageAPI, Command System, NBTItem


#### LanguageAPI:
```java
// you can obtain an instance of LanguageAPI in your plugin instance.
LanguageAPI api = new LanguageAPI(this);
// Then you can use it any where you want.

LanguageHelper helper = api.helper(); // you can use helper(uuid) or helper(player) to translate your message to player's locale.

helper.trans("message.code", "This is the message when we can't find it in translate file");
```
You should provide a language file in yaml, place it at ./lang/default.yml in jar, an example is [here](https://github.com/MrMks/DevToolsB/blob/dev/src/main/resources/lang/default.yml)


#### Command System
```java
PluginCommand pcmd = getCommand("tl");
CommandAdaptorSub cmd = new CommandAdaptorSub(api, "tl.cmd.root", pcmd);

// add your sub commands below
// "tl.cmd.get.usg" is used for language, this is the default message code of this command.
cmd.addChild(new FunctionCommand(api, "get", null, "tl.cmd.get.desc", "tl.cmd.get.usg", "tl.perm.cmd.get", "tl.cmd.get.permMsg"){
    public boolean execute(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> slice) {
        // your command logic here.
    }
    
    public List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        // tab complete logic here.
    }
});

// you may need a sub command under a sub command, like /root bag find <player>, then use SubCommand for 'bag'
// SubCommand scmd = new SubCommand(lapi, "bag", null, "pluginName.cmd.get.desc", "pluginName.cmd.get.usg", "pluginName.perm.cmd.get", "pluginName.cmd.get.permMsg");
// cmd.addChild(scmd);
// you can also add sub commands to scmd;
// scmd.addChild();

// then this is an optional feture, if you provide the config key when construct a command instance, then this feture will use it to load the command config.
// CommandConfiguration cmdCfg = new CommandConfiguration(this);
// cmdCfg.loadCommand(cmd);

// if your add sub commands to the SubCommand, you should load it manually
// cmd.addChild(cmdCfg.loadCommand(scmd));

// sometimes you may need a short cmd without any sub commands, then you can use CommandAdaptorDirect
CommandAdaptorDirect cmdDriect = new CommandAdaptorDirect(pcmd) {
    public boolean execute(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> slice) {
        // your command logic here.
    }
    
    public List<String> complete(CommandSender sender, String label, List<String> fLabel, ArraySlice<String> args) {
        // tab complete logic here.
    }
};

// finally register the executor
pcmd.setExecutor(cmd);
```
An example is [here](https://github.com/MrMks/TemplateLore/blob/da51c54c0dcdc41bc6152b21ee2650c2801ff40c/src/main/java/com/github/mrmks/mc/template/cmd/CommandManager.java)

#### NBTItem
```java
NBTItem nbtI = new NBTItem(itemStack);
// Then you should check whether it is modifible
if (nbtI.isModifible()) {
    // get a wrapper of NBTTagCompound
    TagCompound tag = nbtI.getTag();
    // then get or set anything you want
}
```

An example is [here](https://github.com/MrMks/TemplateLore/blob/da51c54c0dcdc41bc6152b21ee2650c2801ff40c/src/main/java/com/github/mrmks/mc/template/TemplateParser.java#L66)
