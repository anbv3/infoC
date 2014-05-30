package com.infoc.common;

/**
 * @author Naver
 * @date 2014-05-30
 * Copyright 2007 NHN Corp. All rights Reserved.
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
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
