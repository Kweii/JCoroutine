package com.jcoroutine.core.callSite;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-16
 */
public class CallSiteNode implements Comparable<CallSiteNode> {
    public String caller;

    public String callee;

    private CallSiteNode(String caller, String callee) {
        this.caller = caller;
        this.callee = callee;
    }



    public static CallSiteNode build(String caller, String callee){
        return new CallSiteNode(caller, callee);
    }

    public int compareTo(CallSiteNode o) {
        return callee.compareTo(o.callee);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CallSiteNode)){
            return false;
        }
        if (obj==this){
            return true;
        }
        CallSiteNode other = (CallSiteNode)obj;
        return this.caller.equals(other.caller) && this.callee.equals(other.callee);
    }
}
