package com.jcoroutine.test;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-15
 */
public enum  Gender {
    MAN("男", 1),
    WOMAN("女", 2)
    ;
    public String show;
    public int value;

    Gender(String show, int value) {
        this.show = show;
        this.value = value;
    }
}
