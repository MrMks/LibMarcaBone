package com.github.mrmks.mc.marcabone.sheller;

import java.util.List;

public class Sheller {
    private final Operation[] _ops;
    private final int _tS;

    public Sheller(List<String> lst) {
        _tS = Integer.parseInt(lst.remove(0));
        _ops = Utils.bakeAll(lst);
    }

    public Object call(Object[] objects) {
        Object stack = null;
        DataTable table = new DataTable(_tS, objects);
        for (Operation op : _ops) {
            stack = op.invoke(stack, table);
        }
        table.release();
        return stack;
    }
}
