package com.jcoroutine.core.analysis;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class Invocation implements Comparable<Invocation> {
    public String caller;

    public String callee;

    private Invocation(String caller, String callee) {
        this.caller = caller;
        this.callee = callee;
    }



    public static Invocation build(String caller, String callee){
        return new Invocation(caller, callee);
    }

    public int compareTo(Invocation o) {
        return callee.compareTo(o.callee);
    }

    @Override
    public boolean equals(Object obj) {
        Invocation other = (Invocation)obj;
        return this.caller.equals(other.caller) && this.callee.equals(other.callee);
    }
}
