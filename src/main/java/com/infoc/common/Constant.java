package com.infoc.common;

public enum Constant {
    PAGE_SIZE(6);

    private final int val;

    Constant(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
