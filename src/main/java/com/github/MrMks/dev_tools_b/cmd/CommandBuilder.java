package com.github.MrMks.dev_tools_b.cmd;

import com.github.MrMks.dev_tools_b.lang.LanguageAPI;
import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandBuilder {
    private final String key;
    private List<String> alias = new LinkedList<>();
    private String desc, usg, perm, permMsg;
    private SenderType type = SenderType.ANYONE;

    public CommandBuilder(String key){
        this.key = key;
    }

    public CommandBuilder(String key, String[] alias){
        this(key, alias, "", "");
    }

    public CommandBuilder(String key, String desc, String usg) {
        this(key, desc, usg, "");
    }

    public CommandBuilder(String key, String[] alias, String desc, String usg) {
        this(key, alias, desc, usg, "");
    }

    public CommandBuilder(String key, String desc, String usg, String perm) {
        this(key, desc, usg, perm, "");
    }

    public CommandBuilder(String key, String[] alias, String desc, String usg, String perm) {
        this(key, alias, desc, usg, perm, "");
    }

    public CommandBuilder(String key, String desc, String usg, String perm, String permMsg) {
        this.key = key;
        this.desc = desc;
        this.usg = usg;
        this.perm = perm;
        this.permMsg = permMsg;
    }

    public CommandBuilder(String key, String[] alias, String desc, String usg, String perm, String permMsg){
        this.key = key;
        this.alias.addAll(Arrays.asList(alias));
        this.desc = desc;
        this.usg = usg;
        this.perm = perm;
        this.permMsg = permMsg;
    }

    public CommandBuilder alias(String[] alias) {
        this.alias.addAll(Arrays.asList(alias));
        return this;
    }

    public CommandBuilder desc(String desc) {
        this.desc = desc;
        return this;
    }

    public CommandBuilder usg(String usg) {
        this.usg = usg;
        return this;
    }

    public CommandBuilder perm(String perm) {
        this.perm = perm;
        return this;
    }

    public CommandBuilder permMsg(String permMsg) {
        this.permMsg = permMsg;
        return this;
    }

    public CommandBuilder type(SenderType type) {
        this.type = type;
        return this;
    }

    public FuncCommand build(LanguageAPI lapi, ICmdFunc func){
        Validate.notNull(func, "The func can't be null");

        if (alias.isEmpty()) alias.add(key);
        return new FuncCommand(key, alias, type, desc, usg, perm, permMsg, lapi, func);
    }

    public FuncCommand build(ICmdFunc func) {
        return build(LanguageAPI.DEFAULT, func);
    }

    public SubCommand build(LanguageAPI lapi, IChildCommand... subs) {
        Validate.notNull(subs, "The subs can't be null");
        for (IChildCommand sub : subs) Validate.notNull(sub, "Any sub can't be null");

        if (alias.isEmpty()) alias.add(key);
        SubCommand cmd = new SubCommand(key, alias, type, desc, usg, perm, permMsg, lapi);
        cmd.add(subs);
        return cmd;
    }

    public SubCommand build(IChildCommand... subs) {
        return build(LanguageAPI.DEFAULT, subs);
    }
}
