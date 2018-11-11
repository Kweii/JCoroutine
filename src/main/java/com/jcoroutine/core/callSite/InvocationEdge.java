package com.jcoroutine.core.callSite;

import com.jcoroutine.common.tool.JCoroutineTools;

/**
 * Created with IntelliJ IDEA.
 * User: guiliehua
 * Date: 2018/11/10
 * Time: 下午10:45
 * Desc: 表示一条调用信息
 */
public class InvocationEdge implements Comparable<InvocationEdge>{
    //调用者
    private String caller;
    //被调用者
    private String callee;
    //调用行数
    private Integer lineNum;
    //调用层次，从上往下依次增加
    private Integer callLevel = -1;
    //是否递归调用
    private Boolean isRecursive;

    InvocationEdge(String caller, String callee, Integer lineNum) {
        this.caller = caller;
        this.callee = callee;
        this.lineNum = lineNum;
        this.isRecursive = callee.equals(caller);
    }

    public void setCallLevel(Integer callLevel) {
        this.callLevel = callLevel;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public Integer getCallLevel() {
        return callLevel;
    }

    public Boolean getRecursive() {
        return isRecursive;
    }

    @Override
    public int compareTo(InvocationEdge o) {
        return this.caller.toString().compareTo(o.caller.toString())
                +this.callee.toString().compareTo(o.callee.toString());
    }
}
