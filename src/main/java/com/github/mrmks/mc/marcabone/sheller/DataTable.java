package com.github.mrmks.mc.marcabone.sheller;

import java.util.Arrays;

class DataTable {
    private Object[] _init;
    private Object[] _append;
    private int _ni = 0;

    DataTable(int size, Object[] init) {
        this._init = init;
        this._append = new Object[Math.max(size - init.length, 0)];
    }

    Object get(int i) {
        if (_ni < 0) throw new IllegalStateException();
        return i < _init.length ? _init[i] : (i -= _init.length) < _ni ? _append[i] : null;
    }

    void add(Object obj) {
        if (_ni < 0) throw new IllegalStateException();
        if (_ni < _append.length) _append[_ni ++] = obj;
    }

    void release() {
        if (_ni >= 0) {
            Arrays.fill(_init, null);
            Arrays.fill(_append, null);
            _init = null;
            _append = null;
            _ni = -1;
        }
    }
}
